package io.github.ryuryu_ymj.cube_snake.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import io.github.ryuryu_ymj.cube_snake.Game

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            resizable = true
            width = 1280
            height = 720
        }
        LwjglApplication(Game(), config)
    }
}