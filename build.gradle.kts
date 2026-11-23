plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    application
}


dependencies {
    implementation(project(":cli"))
}

application {
    mainClass.set("fileworker.MainKt")
}

