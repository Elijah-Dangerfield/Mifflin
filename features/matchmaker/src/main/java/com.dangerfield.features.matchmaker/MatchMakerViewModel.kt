package com.dangerfield.features.matchmaker

import api.ProfileSection
import api.User
import api.UserRepository
import com.dangerfield.core.common.runCancellableCatching
import com.dangerfield.core.ui.UdfViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MatchMakerViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val analytics: MatchMakerAnalytics,
    private val profilePictureImageLoader: ProfilePictureImageLoader,
    profileConfig: ProfileConfig,
) : UdfViewModel<MatchMakerViewModel.State, MatchMakerViewModel.Action>() {

    private val matchableUsers: MutableSet<User> = LinkedHashSet()

    override val initialState = State(UserResult.Idle, profileConfig.profileSectionOrder)

    override val initialAction: Action = Action.LoadUsers

    override fun transformActionFlow(actionFlow: Flow<Action>): Flow<State> {

        return actionFlow.flatMapMerge {
            flow {
                when (it) {
                    Action.LoadUsers -> handleLoadUsers()
                    is Action.LoadNextUser -> handleLoadNextUser(it.previousUser)
                    Action.SetErrorHandled -> emit(state.copy(userResult = UserResult.Idle))
                    is Action.TrackProfileScroll -> analytics.trackProfileScroll(it.scrollPercent, it.id)
                }
            }
        }
    }

    /**
     * Loads the next person from the queue into the view state
     * @param previousUserId the id of the previous person shown before the next person
     */
    fun loadNextUser(previousUser: User) {
        analytics.trackNextClick()
        submitAction(Action.LoadNextUser(previousUser))
    }

    /**
     * Loads users from the repository
     */
    fun loadUsers() {
        submitAction(Action.LoadUsers)
    }

    /**
     * Marks the People Status error as handled to remove it from the state.
     * see: https://developer.android.com/topic/architecture/ui-layer/events
     */
    fun onErrorHandled() {
        submitAction(Action.SetErrorHandled)
    }

    /**
     * Tracks the profile scroll percentage of a profile
     * @param percentScroll the percentage the user has scrolled (0 = top, 100 = bottom)
     * @param id the id of the profile being scrolled on
     *
     *
     * This function is an unfortunate workaround to not being able to inject analytics into the view. This is not
     * inherently the view models responsibility.
     */
    fun trackProfileScroll(percentScroll: Int, id: Int) {
        submitAction(Action.TrackProfileScroll(percentScroll, id))
    }

    private suspend fun FlowCollector<State>.handleLoadUsers() {
        if (matchableUsers.isNotEmpty() || state.userResult is UserResult.Loading) return
        emit(state.copy(userResult = UserResult.Loading))
        runCancellableCatching {
            val users = userRepository.getNextUsers()
            check(users.isNotEmpty())
            users.also { matchableUsers.addAll(it) }
        }
            .onFailure {
                Timber.e(it)
                emit(state.copy(userResult = UserResult.Failed(it)))
            }
            .onSuccess {
                profilePictureImageLoader.prefetchImages(it.mapNotNull { user -> user.photo })
                pollUserQueue()
            }
    }

    private suspend fun FlowCollector<State>.pollUserQueue() {
        val nextUser = matchableUsers.elementAtOrNull(0)

        val userResult = nextUser?.let {
            matchableUsers.remove(it)
            UserResult.Loaded(it)
        } ?: UserResult.Empty

        if (userResult is UserResult.Loaded) {
            analytics.trackProfileImpression(userResult.user.id)
        }

        emit(state.copy(userResult = userResult))
    }

    private suspend fun FlowCollector<State>.handleLoadNextUser(previousUser: User) {
        userRepository.setUserSeen(previousUser.id)
        previousUser.photo?.let { profilePictureImageLoader.deleteImage(it) }
        pollUserQueue()
    }

    sealed class Action {
        object LoadUsers : Action()
        class LoadNextUser(val previousUser: User) : Action()
        object SetErrorHandled : Action()
        class TrackProfileScroll(val scrollPercent: Int, val id: Int) : Action()
    }

    sealed class UserResult {
        object Idle : UserResult()
        object Loading : UserResult()
        object Empty : UserResult()
        class Failed(val throwable: Throwable) : UserResult()
        class Loaded(val user: User) : UserResult()
    }

    data class State(
        val userResult: UserResult,
        val profileOrder: List<ProfileSection>,
    )
}
