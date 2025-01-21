object Analysis {
    const val ktlint = "0.43.0"
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:8.7.3"

    object Kotlin {
        private const val version = "1.9.0"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.9.0"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Android {
        private const val version = "1.13.0-alpha09"
        const val material = "com.google.android.material:material:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.7.0"
        const val appcompatResources = "androidx.appcompat:appcompat-resources:1.7.0"
        const val palette = "androidx.palette:palette:1.0.0"
        const val coreKtx = "androidx.core:core-ktx:1.15.0"

        object Activity {
            const val version = "1.9.3"
            const val activityKtx = "androidx.activity:activity-ktx:$version"
        }

        object Fragment {
            const val version = "1.8.5"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:$version"
            const val fragmentTesting = "androidx.fragment:fragment-testing:$version"
        }

        object Constraint {
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.2.0"
        }

        object Lifecycle {
            private const val version = "2.8.7"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val process = "androidx.lifecycle:lifecycle-process:$version"
        }

        object Navigation {
            private const val version = "2.8.5"
            const val navigationSafeArguments = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"

            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val commonKtx = "androidx.navigation:navigation-common-ktx:$version"
            const val dynamicFeaturesFragment = "androidx.navigation:navigation-dynamic-features-fragment:$version"
        }

        object Work {
            private const val version = "2.10.0"
            const val runtime = "androidx.work:work-runtime:$version"
            const val runtimeKtx = "androidx.work:work-runtime-ktx:$version"
        }

        object Room {
            private const val version = "2.6.1"
            const val runtime = "androidx.room:room-runtime:$version"
            const val ktx = "androidx.room:room-ktx:$version"
            const val compiler = "androidx.room:room-compiler:$version"
        }

        object Preference {
            const val version = "1.2.1"
            const val preferenceKtx = "androidx.preference:preference-ktx:$version"
        }
    }

    object Dagger {
        private const val version = "2.51.1"
        private const val versionWorker = "1.2.0"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
        const val hiltAndroidCompiler = "com.google.dagger:hilt-compiler:$version"
        const val hiltWorker = "androidx.hilt:hilt-work:$versionWorker"
        const val hiltKapt = "androidx.hilt:hilt-compiler:$versionWorker"
    }

    object OkHttp {
        private const val version = "4.12.0"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Retrofit {
        private const val version = "2.11.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$version"
    }

    object Timber {
        private const val version = "1.5.1"
        const val timber = "com.github.ajalt:timberkt:$version"
    }

    object Glide {
        private const val version = "4.15.1"
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Gson {
        private const val version = "2.10.1"
        const val gson = "com.google.code.gson:gson:$version"
    }

    object IOSocket {
        private const val version = "2.0.0"
        const val ioSocket = "io.socket:socket.io-client:$version"
    }

    object Play {
        const val featureDelivery = "com.google.android.play:feature-delivery:2.1.0"
        const val review = "com.google.android.play:review:2.0.2"
    }

    object LiveKit {
        private const val version = "2.11.0"
        const val android = "io.livekit:livekit-android:$version"
        const val cameraX = "io.livekit:livekit-android-camerax:$version"
    }

    object Protobuf {
        private const val version = "3.24.0"
        const val javalite = "com.google.protobuf:protobuf-javalite:$version"
    }

    object RippleBackground {
        private const val version = "1.0.1"
        const val rippleBackground = "com.skyfishjy.ripplebackground:library:$version"
    }

    object Volley {
        private const val version = "1.2.1"
        const val volley = "com.android.volley:volley:$version"
    }

    object LeakCanary {
        private const val version = "2.12"
        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:$version"
    }

    object AWS {
        private const val version = "2.66.0"
        const val awsS3 = "com.amazonaws:aws-android-sdk-s3:$version"
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:33.7.0"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val messaging = "com.google.firebase:firebase-messaging-ktx"
        const val database = "com.google.firebase:firebase-database"
        const val config = "com.google.firebase:firebase-config"
        const val auth = "com.google.firebase:firebase-auth"
    }
}

object TestDeps {
    object AndroidX {
        private const val version = "1.5.0"

        // AndroidX Test - JVM Testing
        const val coreKtx = "androidx.test:core-ktx:$version"
        const val rules = "androidx.test:rules:$version"
        const val coreTesting = "androidx.arch.core:core-testing:2.2.0"
        const val androidX_jUnit = "androidx.test.ext:junit-ktx:1.2.1"
        const val navigationTest = "androidx.navigation:navigation-testing:2.7.3"
    }

    object Coroutines {
        private const val version = "1.7.3"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object JUnit {
        private const val version = "4.13.2"
        const val junit = "junit:junit:$version"
    }

    object MockWebServer {
        private const val version = "4.11.0"
        const val mockwebserver = "com.squareup.okhttp3:mockwebserver:$version"
        const val okhttpIdlingResource = "com.jakewharton.espresso:okhttp3-idling-resource:1.0.0"
    }

    object MockK {
        const val mockK = "io.mockk:mockk:1.13.7"
    }

    object Mockito {
        private const val version = "5.5.0"
        const val core = "org.mockito:mockito-core:$version"
        const val inline = "org.mockito:mockito-inline:$version"
        const val android = "org.mockito:mockito-android:$version"
    }

    object RoboElectric {
        private const val version = "4.10.3"
        const val robolectric = "org.robolectric:robolectric:$version"
    }

    object Turbine {
        private const val version = "1.0.0"
        const val turbine = "app.cash.turbine:turbine:$version"
    }

    const val truth = "com.google.truth:truth:1.1.5"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.6.1"
}
