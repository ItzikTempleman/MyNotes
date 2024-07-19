plugins {
    id("com.android.application") version "8.5.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false
}

buildscript {
    dependencies {
        classpath (libs.hilt.android.gradle.plugin)
        classpath (libs.kotlin.gradle.plugin)
    }

}
