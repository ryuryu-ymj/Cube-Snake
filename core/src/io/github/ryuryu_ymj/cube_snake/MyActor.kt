package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor

abstract class MyActor(asset: AssetManager, private val fieldMap: FieldMap, var indexX: Int = 0, var indexY: Int = 0) : Actor() {
    init {
        setPosition((indexX * PANEL_UNIT).toFloat(), (indexY * PANEL_UNIT).toFloat())
    }

    override fun act(delta: Float) {
        super.act(delta)
        //setPosition((indexX * PANEL_UNIT).toFloat(), (indexY * PANEL_UNIT).toFloat())
    }

    fun moveTo(indexX: Int, indexY: Int) {
        fieldMap.moveActorTo(this, indexX, indexY)
    }

    fun moveBy(dIndexX: Int = 0, dIndexY: Int = 0) {
        fieldMap.moveActorBy(this, dIndexX, dIndexY)
    }
}