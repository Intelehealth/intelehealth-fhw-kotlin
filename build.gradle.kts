import io.gitlab.arturbosch.detekt.Detekt

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
    apply(plugin = Plugins.DETEKT)

    tasks.withType<Detekt>().configureEach {
        config.setFrom(files("$rootDir/detekt/detekt.yml"))
        parallel = true
        autoCorrect = true
        buildUponDefaultConfig = true
        setSource(files("src/main/java", "src/main/kotlin"))
//        include("**/*.kt", "**/*.kts")
        exclude { file ->
            file.path.contains("buildSrc")
        }
        reports {
            html.required.set(true)
            html.outputLocation.set(layout.buildDirectory.file("reports/detekt/${project.name}.html"))
            xml.required.set(false)
            txt.required.set(false)
            sarif.required.set(false)
            md.required.set(false)
        }
    }
}
