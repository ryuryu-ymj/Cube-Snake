package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Actor

class Snake : MyActor() {
    val bodies = mutableListOf<SnakeBody>()
    private var cnt = 0
    private var canGoRight = true
    private var canGoUp = true
    private var canGoLeft = true
    private var canGoDown = true
    private lateinit var blocks: MutableList<Block>

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
        /*if (bodies[0].direction == Direction.RIGHT) canGoLeft = false
        if (bodies[0].direction == Direction.LEFT) canGoRight = false
        if (bodies[0].direction == Direction.UP) canGoDown = false
        if (bodies[0].direction == Direction.DOWN) canGoUp = false*/
        val actorList = bodies + blocks
        canGoRight = !actorList.any { it.ix == ix + 1 && it.iy == iy }
        canGoLeft = !actorList.any { it.ix == ix - 1 && it.iy == iy }
        canGoUp = !actorList.any { it.ix == ix && it.iy == iy + 1 }
        canGoDown = !actorList.any { it.ix == ix && it.iy == iy - 1 }
        if (cnt <= 0) {
            // 入力による移動
            when {
                (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
                        && canGoLeft -> {
                    chaseHead()
                    bodies[0].run {
                        ix--
                        direction = Direction.LEFT
                    }
                }
                (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
                        && canGoRight -> {
                    chaseHead()
                    bodies[0].run {
                        ix++
                        direction = Direction.RIGHT
                    }
                }
                (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
                        && canGoDown -> {
                    chaseHead()
                    bodies[0].run {
                        iy--
                        direction = Direction.DOWN
                    }
                }
                (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
                        && canGoUp -> {
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
        cnt = 15
    }

    fun fall() {
        bodies.forEach {
            it.iy--
        }
    }
}