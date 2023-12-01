buildscript {
    dependencies {
        //classpath("com.google.gms:google-services:4.3.10")
        classpath("com.android.tools.build:gradle:3.4.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
        classpath ("com.google.gms:google-services:4.4.0")

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
}