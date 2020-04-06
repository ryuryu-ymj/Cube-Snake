package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage

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
        if (cnt <= 0 && canInput) {
            // 入力による移動
            inputDir.let {
                if (it != null && movable[it()]) {
                    if (canGrow) {
                        SnakeBody(asset, fieldMap, 0, 0, false).let {
                            fieldMap.addActor(stage, it)
                            bodies.add(it)
                        }
                        canGrow = false
                    }
                    proceed(it)
                }
            }
        }

        if (cnt > 0) cnt--
    }

    private fun proceed(direction: Direction) {
        var dir1 = head.direction
        head.direction = direction
        head.moveBy(when (direction) {
            Direction.RIGHT -> 1; Direction.LEFT -> -1; else -> 0
        }, when (direction) {
            Direction.UP -> 1; Direction.DOWN -> -1; else -> 0
        })
        for (i in 1 until bodies.size) {
            bodies[i].move(bodies[i - 1].indexX, bodies[i - 1].indexY)
            val dir2 = bodies[i].direction
            bodies[i].direction = dir1
            dir1 = dir2
        }
        cnt = 15
    }

    fun fall() {
        bodies.forEach { it.moveBy(0, -1) }
    }

    fun die() {
        isAlive = false
        println("player died")
    }

    fun grow() {
        canGrow = true
    }
}