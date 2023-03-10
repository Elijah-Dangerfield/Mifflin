

plugins {
    id("mifflin.android.feature")
}

android {
    namespace = "com.dangerfield.mifflin.features.profile"
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.google.truth)
}
