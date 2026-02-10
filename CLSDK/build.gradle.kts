plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    // Room 编译器
//    id("com.google.devtools.kapt") version "2.1.0"
//    id("kotlin-kapt")
//    kotlin("android") version "2.1.0"
}

android {
    namespace = "com.example.clsdk"
    compileSdk = 34


    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildToolsVersion = "33.0.0"

    buildFeatures {
        viewBinding = true // 启用 ViewBinding
    }

    buildFeatures {
        dataBinding = true
    }

//    kotlin {
//        jvmToolchain(11)
//    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
//    implementation("androidx.room:room-compiler:2.8.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.github.zhaokaiqiang.klog:library:1.6.0")
    implementation("com.nostra13.universalimageloader:universal-image-loader:1.9.5")
    implementation("com.google.code.gson:gson:2.13.2")

    // Room 数据库
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    // 协程、Flow 支持
    implementation("androidx.room:room-ktx:$roomVersion")
//    implementation("androidx.room:room-runtime:$roomVersion")
//    implementation("androidx.room:room-ktx:$roomVersion")
//    annotationProcessor("androidx.room:room-compiler:$roomVersion")
//    kapt("androidx.room:room-compiler:$roomVersion")
    // Lifecycle (ViewModel & LiveData)
    val ktxVersion = "2.4.2"
    val lifecycleVersion = "2.6.1"
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$ktxVersion")
//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$ktxVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata:$lifecycleVersion")

    // 协程 (核心依赖)
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    // 基础组件
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

}