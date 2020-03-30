package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor

abstract class MyActor(private val asset: AssetManager, private val fieldMap: FieldMap, var indexX: Int = 0, var indexY: Int = 0) : Actor() {
    init {
        setPosition((indexX * PANEL_UNIT).toFloat(), (indexY * PANEL_UNIT).toFloat())
    }

    override fun act(delta: Float) {
        super.act(delta)
        setPosition((indexX * PANEL_UNIT).toFloat(), (indexY * PANEL_UNIT).toFloat())
    }

    fun move(indexX: Int, indexY: Int) {
        fieldMap.moveActor(this, indexX, indexY)
    }

    fun moveByChange(dIndexX: Int, dIndexY: Int) {
        fieldMap.moveActorByChange(this, dIndexX, dIndexY)
    }
}