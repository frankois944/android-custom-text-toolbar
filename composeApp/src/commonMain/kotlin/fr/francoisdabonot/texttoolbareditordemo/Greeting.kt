package fr.francoisdabonot.texttoolbareditordemo

class Greeting {
    private val platform = getPlatform()

    fun greet(): String = "Hello, ${platform.name}!"
}
