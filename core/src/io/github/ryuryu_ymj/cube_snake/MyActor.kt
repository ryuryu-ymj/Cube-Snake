package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.scenes.scene2d.Actor

abstract class MyActor(protected val fieldMap: FieldMap, indexX: Int = 0, indexY: Int = 0) : Actor() {
    var indexX = indexX
    var indexY = indexY
    init {
        setPosition((indexX * PANEL_UNIT).toFloat(), (indexY * PANEL_UNIT).toFloat())
    }

    abstract fun dispose()

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