package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage

class Snake : Actor() {
    val bodies = mutableListOf<SnakeBody>()
    val head
        get() = bodies[0]
    private var cnt = 0
    var isAlive = true
        private set
    val movable = Array(4) { true }

    fun create(indexX: Int, indexY: Int, bodyCnt: Int, fieldMap: FieldMap, stage: Stage) {
        stage.addActor(this)
        for (i in 0 until bodyCnt) {
            SnakeBody(fieldMap, indexX - i, indexY).let {
                fieldMap.addActor(stage, it)
                bodies.add(it)
            }
        }
    }

    fun dispose() {
        bodies.forEach { it.dispose() }
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (movable.all { !it }) die() // 自死
        if (cnt <= 0) {
            // 入力による移動
            when {
                (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
                        && movable[Direction.LEFT()] -> {
                    proceed(Direction.LEFT)
                }
                (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
                        && movable[Direction.RIGHT()] -> {
                    proceed(Direction.RIGHT)
                }
                (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
                        && movable[Direction.DOWN()] -> {
                    proceed(Direction.DOWN)
                }
                (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
                        && movable[Direction.UP()] -> {
                    proceed(Direction.UP)
                }
            }
        }

        if (cnt > 0) cnt--
    }

    private fun proceed(direction: Direction) {
        var goalX = head.indexX + when(direction) {
            Direction.RIGHT -> 1
            Direction.LEFT -> -1
            else -> 0
        }
        var goalY = head.indexY + when(direction) {
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
        cnt = 20
    }

    fun fall() {
        bodies.forEach {
            it.moveByChange(0, -1)
        }
    }

    fun die() {
        isAlive = false
        println("player died")
    }
}