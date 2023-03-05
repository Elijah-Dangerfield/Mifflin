// Generated by Dagger (https://dagger.dev).
package com.dangerfield.features.matchmaker;

import com.dangerfield.core.people.api.PeopleRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MatchMakerViewModel_Factory implements Factory<MatchMakerViewModel> {
  private final Provider<PeopleRepository> peopleRepositoryProvider;

  private final Provider<ProfileConfig> profileConfigProvider;

  public MatchMakerViewModel_Factory(Provider<PeopleRepository> peopleRepositoryProvider,
      Provider<ProfileConfig> profileConfigProvider) {
    this.peopleRepositoryProvider = peopleRepositoryProvider;
    this.profileConfigProvider = profileConfigProvider;
  }

  @Override
  public MatchMakerViewModel get() {
    return newInstance(peopleRepositoryProvider.get(), profileConfigProvider.get());
  }

  public static MatchMakerViewModel_Factory create(
      Provider<PeopleRepository> peopleRepositoryProvider,
      Provider<ProfileConfig> profileConfigProvider) {
    return new MatchMakerViewModel_Factory(peopleRepositoryProvider, profileConfigProvider);
  }

  public static MatchMakerViewModel newInstance(PeopleRepository peopleRepository,
      ProfileConfig profileConfig) {
    return new MatchMakerViewModel(peopleRepository, profileConfig);
  }
}