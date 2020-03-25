package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class Snake : MyActor() {
    private val bodies = mutableListOf<SnakeBody>()
    private var cnt = 0
    private lateinit var blocks: MutableList<Block>
    var isAlive = true
        private set

    fun create(panelX: Int, panelY: Int, bodyCnt: Int, blocks: MutableList<Block>) {
        for (i in 0 until bodyCnt) {
            SnakeBody(panelX - i, panelY).let {
                stage.addActor(it)
                bodies.add(it)
            }
        }
        this.blocks = blocks
    }

    override fun dispose() {
        bodies.forEach { it.dispose() }
    }

    override fun act(delta: Float) {
        super.act(delta)
        val actorList = bodies + blocks
        val movable = Array(4) { true }
        movable[Direction.RIGHT()] = !actorList.any { it.panelX == panelX + 1 && it.panelY == panelY }
        movable[Direction.LEFT()] = !actorList.any { it.panelX == panelX - 1 && it.panelY == panelY }
        movable[Direction.UP()] = !actorList.any { it.panelX == panelX && it.panelY == panelY + 1 }
        movable[Direction.DOWN()] = !actorList.any { it.panelX == panelX && it.panelY == panelY - 1 }
        if (movable.all { !it }) die()
        if (cnt <= 0) {
            // 入力による移動
            when {
                (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
                        && movable[Direction.LEFT()] -> {
                    chaseHead()
                    bodies[0].run {
                        panelX--
                        direction = Direction.LEFT
                    }
                }
                (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
                        && movable[Direction.RIGHT()] -> {
                    chaseHead()
                    bodies[0].run {
                        panelX++
                        direction = Direction.RIGHT
                    }
                }
                (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
                        && movable[Direction.DOWN()] -> {
                    chaseHead()
                    bodies[0].run {
                        panelY--
                        direction = Direction.DOWN
                    }
                }
                (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
                        && movable[Direction.UP()] -> {
                    chaseHead()
                    bodies[0].run {
                        panelY++
                        direction = Direction.UP
                    }
                }
            }
        }
        panelX = bodies[0].panelX
        panelY = bodies[0].panelY

        // 落下
        if (!bodies.any { body ->
                    blocks.any { it.panelY == body.panelY - 1 && it.panelX == body.panelX }
                }) fall()
        if (cnt > 0) cnt--
    }

    private fun chaseHead() {
        for (i in bodies.size - 1 downTo 1) {
            bodies[i].panelX = bodies[i - 1].panelX
            bodies[i].panelY = bodies[i - 1].panelY
            bodies[i].direction = bodies[i - 1].direction
        }
        cnt = 20
    }

    private fun fall() {
        bodies.forEach {
            it.panelY--
        }
        panelY--
    }

    fun die() {
        isAlive = false
        println("player died")
    }
}