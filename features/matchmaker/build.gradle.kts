plugins {
    id("mifflin.android.feature")
    id("mifflin.android.library.compose")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "com.dangerfield.mifflin.features.matchmaker"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:analytics"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:users:api"))
    implementation(project(":core:common"))
    implementation(project(":core:config:api"))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.coil)
    implementation(libs.timber)

    testImplementation(project(":core:test"))
    androidTestImplementation(project(":core:test"))
}
