package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.scenes.scene2d.Actor

abstract class MyActor(var panelX: Int = 0, var panelY: Int = 0) : Actor() {
    init {
        setPosition((panelX * PANEL_UNIT).toFloat(), (panelY * PANEL_UNIT).toFloat())
    }

    abstract fun dispose()

    override fun act(delta: Float) {
        super.act(delta)
        setPosition((panelX * PANEL_UNIT).toFloat(), (panelY * PANEL_UNIT).toFloat())
    }
}