package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions

class Snake(val asset: AssetManager, stage: Stage, val fieldMap: FieldMap, indexX: Int, indexY: Int, bodyCnt: Int) : Actor() {
    val bodies = mutableListOf<SnakeBody>()
    val head
        get() = bodies[0]
    private var cnt = 0
    var isAlive = true
        private set
    val movable = Array(4) { true }
    var canInput = true
    private var canGrow = false

    init {
        stage.addActor(this)
        for (i in 0 until bodyCnt) {
            SnakeBody(asset, fieldMap, indexX - i, indexY, i == 0).let {
                fieldMap.addActor(stage, it)
                bodies.add(it)
            }
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (movable.all { !it }) die() // 自死
        if (cnt <= 0 && head.actions.size == 0) {
            // 入力による移動
            inputDir.let {
                if (it != null && movable[it()]) {
                    val dir = bodies.last().direction
                    proceed(it)
                    if (canGrow) {
                        bodies.last().let {
                            SnakeBody(asset, fieldMap, it.indexX, it.indexY, false, dir).let {
                            fieldMap.addActor(stage, it)
                            bodies.add(it)
                        } }
                        canGrow = false
                    }
                }
            }
        }

        if (cnt > 0) cnt--
    }

    private fun proceed(direction: Direction) {
        var dir1 = head.direction
        head.direction = direction
        bodies.forEach {
            val (dix, diy) = it.direction.getDIndex()
            it.moveBy(dix, diy)
            it.addAction(Actions.moveBy(dix * PANEL_UNIT.toFloat(), diy * PANEL_UNIT.toFloat(), 0.5f))
            val dir2 = it.direction
            it.direction = dir1
            dir1 = dir2
        }
        cnt = 15
    }

    fun fall() {
        bodies.forEach {
            it.moveBy(0, -1)
            it.addAction((Actions.moveBy(0f, -PANEL_UNIT.toFloat(), 0.5f)))
        }
    }

    fun die() {
        isAlive = false
        println("player died")
    }

    fun grow() {
        canGrow = true
    }
}