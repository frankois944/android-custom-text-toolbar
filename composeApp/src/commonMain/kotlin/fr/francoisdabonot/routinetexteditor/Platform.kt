package fr.francoisdabonot.routinetexteditor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
