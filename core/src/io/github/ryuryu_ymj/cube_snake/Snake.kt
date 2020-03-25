package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class Snake : MyActor() {
    val bodies = mutableListOf<SnakeBody>()
    private var cnt = 0
    private lateinit var blocks: MutableList<Block>
    var isAlive = true
        private set

    fun create(ix: Int, iy: Int, bodyCnt: Int, blocks: MutableList<Block>) {
        for (i in 0 until bodyCnt) {
            SnakeBody(ix - i, iy).let {
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
        movable[Direction.RIGHT()] = !actorList.any { it.ix == ix + 1 && it.iy == iy }
        movable[Direction.LEFT()] = !actorList.any { it.ix == ix - 1 && it.iy == iy }
        movable[Direction.UP()] = !actorList.any { it.ix == ix && it.iy == iy + 1 }
        movable[Direction.DOWN()] = !actorList.any { it.ix == ix && it.iy == iy - 1 }
        if (movable.all { !it }) die()
        if (cnt <= 0) {
            // 入力による移動
            when {
                (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
                        && movable[Direction.LEFT()] -> {
                    chaseHead()
                    bodies[0].run {
                        ix--
                        direction = Direction.LEFT
                    }
                }
                (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
                        && movable[Direction.RIGHT()] -> {
                    chaseHead()
                    bodies[0].run {
                        ix++
                        direction = Direction.RIGHT
                    }
                }
                (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
                        && movable[Direction.DOWN()] -> {
                    chaseHead()
                    bodies[0].run {
                        iy--
                        direction = Direction.DOWN
                    }
                }
                (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
                        && movable[Direction.UP()] -> {
                    chaseHead()
                    bodies[0].run {
                        iy++
                        direction = Direction.UP
                    }
                }
            }
        }
        ix = bodies[0].ix
        iy = bodies[0].iy
        if (cnt > 0) cnt--
    }

    private fun chaseHead() {
        for (i in bodies.size - 1 downTo 1) {
            bodies[i].ix = bodies[i - 1].ix
            bodies[i].iy = bodies[i - 1].iy
            bodies[i].direction = bodies[i - 1].direction
        }
        cnt = 20
    }

    fun fall() {
        bodies.forEach {
            it.iy--
        }
        iy--
    }

    fun die() {
        isAlive = false
        println("player died")
    }
}