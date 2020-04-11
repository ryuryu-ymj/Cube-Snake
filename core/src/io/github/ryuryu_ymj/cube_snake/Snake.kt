package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import kotlin.math.sqrt

class Snake(val asset: AssetManager, stage: Stage, val fieldMap: FieldMap, indexX: Int, indexY: Int, bodyCnt: Int) : Actor() {
    val bodies = mutableListOf<SnakeBody>()
    val head
        get() = bodies[0]
    val tale
        get() = bodies.last()
    var isAlive = true
        private set
    val movable = Array(4) { true }
    private var canGrow = false
    private var fallHeight = 0

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

        // 落下
        if (fallHeight > 0 && tale.actions.size == 0) {
            bodies.forEach {
                it.moveBy(0, -fallHeight)
                it.addAction(Actions.moveBy(0f, -fallHeight * PANEL_UNIT, sqrt(fallHeight.toFloat()) * 0.05f, Interpolation.pow2In))
            }
        }

        if (tale.actions.size == 0) {
            inputDir.let {
                if (it != null && movable[it()]) {
                    val dir = tale.direction
                    // 入力による移動
                    proceed(it)

                    // アイテム取得後の初入力で成長
                    if (canGrow) {
                        tale.let {
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
    }

    private fun proceed(direction: Direction) {
        var dir1 = direction
        bodies.forEachIndexed { i, it ->
            val dir2 = it.direction
            it.direction = dir1
            dir1 = dir2
            val (dix, diy) = it.direction.getDIndex()
            it.moveBy(dix, diy)
            it.addAction(Actions.sequence(
                    Actions.delay(0.05f * i),
                    Actions.moveBy(dix * PANEL_UNIT, diy * PANEL_UNIT, 0.05f)))
        }
    }

    fun fall(fallHeight: Int) {
        this.fallHeight = fallHeight
    }

    fun die() {
        isAlive = false
        println("player died")
    }

    fun grow() {
        canGrow = true
    }
}