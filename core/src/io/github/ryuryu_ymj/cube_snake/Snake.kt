package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions

class Snake(val asset: AssetManager, stage: Stage, val fieldMap: FieldMap, indexX: Int, indexY: Int, bodyCnt: Int) : Actor() {
    val bodies = mutableListOf<SnakeBody>()
    val head
        get() = bodies[0]
    var isAlive = true
        private set
    val movable = Array(4) { true }
    private var canGrow = false
    private var isProceeding = false
    private var isFalling = false

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

        if (isFalling && !isProceeding) {
            bodies.forEach {
                it.moveBy(0, -1)
                it.addAction(Actions.sequence(Actions.moveBy(0f, -PANEL_UNIT, 0.1f, Interpolation.pow2In)))
            }
        }

        if (!isFalling && !isProceeding) {
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
                            }
                        }
                        canGrow = false
                    }
                }
            }
        }
        println(isProceeding)
    }

    private fun proceed(direction: Direction) {
        var dir1 = direction
        bodies.forEachIndexed { i, it ->
            val dir2 = it.direction
            it.direction = dir1
            dir1 = dir2
            val (dix, diy) = it.direction.getDIndex()
            it.moveBy(dix, diy)
            if (i == bodies.size - 1) it.addAction(Actions.sequence(
                    Actions.delay(0.05f * i),
                    Actions.moveBy(dix * PANEL_UNIT, diy * PANEL_UNIT, 0.05f),
                    Actions.run { isProceeding = false }))
            else it.addAction(Actions.sequence(
                    Actions.delay(0.05f * i),
                    Actions.moveBy(dix * PANEL_UNIT, diy * PANEL_UNIT, 0.05f)))
        }
        isProceeding = true
    }

    fun fall() {
        isFalling = true
    }

    fun die() {
        isAlive = false
        println("player died")
    }

    fun grow() {
        canGrow = true
    }
}