package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.scenes.scene2d.Actor

open class MyActor(var ix: Int = 0, var iy: Int = 0) : Actor() {
    init {
        setPosition((ix * 100).toFloat(), (iy * 100).toFloat())
    }

    override fun act(delta: Float) {
        setPosition((ix * 100).toFloat(), (iy * 100).toFloat())
    }
}