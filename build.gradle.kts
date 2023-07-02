plugins {
    id("java-library")
    id("net.labymod.gradle")
    id("net.labymod.gradle.addon")
}

group = "dev.wector11211.labymod"
version = "1.0.0"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

labyMod {
    defaultPackageName = "dev.wector11211.labymod.vanillafixes"
    addonInfo {
        namespace = "vanillafixes"
        displayName = "Vanilla Fixes for 1.8"
        author = "Wector11211"
        description = "Brings fixes for some annoying 1.8 bugs"
        minecraftVersion = "1.8.9"
        version = System.getenv().getOrDefault("VERSION", "0.0.1")
    }

    minecraft {
        registerVersions("1.8.9") { version, provider ->
            configureRun(provider, version)
        }

        subprojects.forEach {
            if (it.name != "game-runner") {
                filter(it.name)
            }
        }
    }

    addonDev {
        snapshotRelease()
    }
}

subprojects {
    plugins.apply("java-library")
    plugins.apply("net.labymod.gradle")
    plugins.apply("net.labymod.gradle.addon")

    repositories {
        maven("https://libraries.minecraft.net/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
}

fun configureRun(provider: net.labymod.gradle.core.minecraft.provider.VersionProvider, gameVersion: String) {
    provider.runConfiguration {
        mainClass = "net.minecraft.launchwrapper.Launch"
        jvmArgs("-Dnet.labymod.running-version=${gameVersion}")
        jvmArgs("-Dmixin.debug=true")
        jvmArgs("-Dnet.labymod.debugging.all=true")
        jvmArgs("-Dmixin.env.disableRefMap=true")

        args("--tweakClass", "net.labymod.core.loader.vanilla.launchwrapper.LabyModLaunchWrapperTweaker")
        args("--labymod-dev-environment", "true")
        args("--addon-dev-environment", "true")
    }

    provider.javaVersion = when (gameVersion) {
        else -> {
            JavaVersion.VERSION_17
        }
    }

    provider.mixin {
        minVersion = "0.6.6" // addon is 1.8.9 only
    }
}
