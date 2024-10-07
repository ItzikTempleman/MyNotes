plugins {

    kotlin("jvm") version "1.9.0"

    id("com.android.application") version "8.7.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.google.devtools.ksp") version "2.0.20-1.0.24" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}


buildscript {
    dependencies {
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
    }

}