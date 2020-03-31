package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage

class Snake(asset: AssetManager, stage: Stage, fieldMap: FieldMap, indexX: Int, indexY: Int, bodyCnt: Int) : Actor() {
    val bodies = mutableListOf<SnakeBody>()
    val head
        get() = bodies[0]
    private var cnt = 0
    var isAlive = true
        private set
    val movable = Array(4) { true }
    var canInput = true

    init {
        stage.addActor(this)
        for (i in 0 until bodyCnt) {
            SnakeBody(asset, fieldMap, indexX - i, indexY).let {
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
                if (it != null && movable[it()]){
                    proceed(it)
                    print("input ")
                    bodies.forEach { print("(${it.indexX}, ${it.indexY})") }
                    println()
                }
            }
        }

        if (cnt > 0) cnt--
    }

    private fun proceed(direction: Direction) {
        var goalX = head.indexX + when (direction) {
            Direction.RIGHT -> 1
            Direction.LEFT -> -1
            else -> 0
        }
        var goalY = head.indexY + when (direction) {
            Direction.UP -> 1
            Direction.DOWN -> -1
            else -> 0
        }
        bodies.forEach {
            val indexX = it.indexX
            val indexY = it.indexY
            it.move(goalX, goalY)
            goalX = indexX
            goalY = indexY
        }
        cnt = 60
    }

    fun fall() {
        print("fall ")
        bodies.forEach {
            it.moveByChange(0, -1)
            print("(${it.indexX}, ${it.indexY})")
        }
        println()
    }

    fun die() {
        isAlive = false
        println("player died")
    }
}