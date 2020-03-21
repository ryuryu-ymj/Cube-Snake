package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class Snake : MyActor() {
    val bodies = mutableListOf<SnakeBody>()
    private var cnt = 0
    private val canGo = mutableMapOf(Direction.RIGHT to true, Direction.LEFT to true,
            Direction.UP to true, Direction.DOWN to true)
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

    override fun act(delta: Float) {
        super.act(delta)

        val actorList = bodies + blocks
        canGo[Direction.RIGHT] = !actorList.any { it.ix == ix + 1 && it.iy == iy }
        canGo[Direction.LEFT] = !actorList.any { it.ix == ix - 1 && it.iy == iy }
        canGo[Direction.UP] = !actorList.any { it.ix == ix && it.iy == iy + 1 }
        canGo[Direction.DOWN] = !actorList.any { it.ix == ix && it.iy == iy - 1 }
        if (canGo.all { !it.value }) die()

        when {
            (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
                    && canGo[Direction.LEFT] ?: true -> {
                bodies[0].direction = Direction.LEFT
            }
            (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
                    && canGo[Direction.RIGHT] ?: true -> {
                bodies[0].direction = Direction.RIGHT
            }
            (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
                    && canGo[Direction.DOWN] ?: true -> {
                bodies[0].direction = Direction.DOWN
            }
            (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
                    && canGo[Direction.UP] ?: true -> {
                bodies[0].direction = Direction.UP
            }
        }
        if (cnt <= 0) {
            bodies[0].run {
                if (canGo[direction] ?: true) {
                    chaseHead()
                    when (direction) {
                        Direction.RIGHT -> ix++
                        Direction.UP -> iy++
                        Direction.LEFT -> ix--
                        Direction.DOWN -> iy--
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