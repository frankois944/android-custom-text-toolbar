package fr.francoisdabonot.routinetexteditor

enum class CurrentOS {
    IOS,
    Android,
}

interface Platform {
    val name: String
    val system: CurrentOS
}

expect fun getPlatform(): Platform
