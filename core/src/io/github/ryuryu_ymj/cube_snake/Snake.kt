package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Actor

class Snake : MyActor() {
    val bodies = mutableListOf<SnakeBody>()
    private var cnt = 0

    fun create(ix: Int, iy: Int, bodyCnt: Int) {
        for (i in 0 until bodyCnt) {
            SnakeBody(ix - i, iy).let {
                stage.addActor(it)
                bodies.add(it)
            }
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (cnt <= 0) {
            // 入力による移動
            when {
                (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
                        && bodies[0].direction != Direction.RIGHT -> {
                    chaseHead()
                    bodies[0].run {
                        ix--
                        direction = Direction.LEFT
                    }
                }
                (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
                        && bodies[0].direction != Direction.LEFT -> {
                    chaseHead()
                    bodies[0].run {
                        ix++
                        direction = Direction.RIGHT
                    }
                }
                (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
                        && bodies[0].direction != Direction.UP -> {
                    chaseHead()
                    bodies[0].run {
                        iy--
                        direction = Direction.DOWN
                    }
                }
                (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))
                        && bodies[0].direction != Direction.DOWN -> {
                    chaseHead()
                    bodies[0].run {
                        iy++
                        direction = Direction.UP
                    }
                }
            }
        }
        x = bodies[0].x
        y = bodies[0].y
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

    fun drop() {
        bodies.forEach {
            it.iy--
        }
    }
}