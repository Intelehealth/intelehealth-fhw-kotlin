import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.navigation.safe.args) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.androidx.dynamic.feature) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.detekt.plugin) apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    tasks.withType<Detekt>().configureEach {
        reports {
            html {
                required.set(true)
                outputLocation.set(file("$projectDir/reports/detekt/detekt.html"))
            }
            xml {
                required.set(true)
                outputLocation.set(file("$projectDir/reports/detekt/detekt.xml"))
            }
            sarif {
                required.set(true)
                outputLocation.set(file("$projectDir/reports/detekt/detekt.sarif"))
            }
            md {
                required.set(true)
                outputLocation.set(file("$projectDir/reports/detekt/detekt.md"))
            }
        }
    }
}
