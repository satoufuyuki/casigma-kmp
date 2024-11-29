import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.exposed.core)
            implementation(libs.exposed.jdbc)
            implementation(libs.exposed.dao)
            implementation(libs.exposed.java.time)

            // JDBC Drivers
            implementation("org.postgresql:postgresql:42.7.4")
            implementation("org.mariadb.jdbc:mariadb-java-client:3.5.0")
            implementation("com.mysql:mysql-connector-j:9.1.0")

            // Coil
            implementation("io.coil-kt.coil3:coil-compose:3.0.4")
            implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")

            // Koin
            val koinVersion = "4.0.0"
            implementation("io.insert-koin:koin-compose:$koinVersion")
            implementation("io.insert-koin:koin-compose-viewmodel:$koinVersion")
            implementation("io.insert-koin:koin-compose-viewmodel-navigation:$koinVersion")

            implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")
            implementation("org.springframework.security:spring-security-crypto:6.0.3")
            implementation("com.zaxxer:HikariCP:6.0.0")
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation("org.jetbrains.compose.material3:material3:1.7.1")
            implementation("org.jetbrains.compose.ui:ui-util:1.7.1")
            implementation("org.jetbrains.compose.material3:material3-window-size-class:1.7.1")
            implementation("org.jetbrains.compose.material3:material3-adaptive-navigation-suite:1.7.1")
            implementation("org.jetbrains.compose.ui:ui-text:1.7.1")
        }
    }
}


compose.desktop {
    application {
        mainClass = "dev.pbt.casigma.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.pbt.casigma"
            packageVersion = "1.0.0"
        }
    }
}
