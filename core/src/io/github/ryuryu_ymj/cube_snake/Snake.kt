package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import kotlin.math.sqrt

class Snake(val asset: AssetManager, stage: Stage, val fieldMap: FieldMap, indexX: Int, indexY: Int, bodyCnt: Int) : Actor() {
    val bodies = mutableListOf<SnakeBody>()
    val bodyGroup = Group()
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
        stage.addActor(bodyGroup)
        for (i in 0 until bodyCnt) {
            SnakeBody(asset, fieldMap, indexX - i, indexY, i == 0).let {
                bodies.add(it)
                bodyGroup.addActor(it)
            }
        }
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (movable.all { !it }) die() // 自死

        // 落下
        // TODO 落下中にトゲとの当たり判定ができていないため修正求む
        if (fallHeight > 0 && !tale.hasActions()) {
            bodies.forEach {
                it.moveBy(0, -fallHeight)
                it.addAction(Actions.moveBy(0f, -fallHeight * PANEL_UNIT, sqrt(fallHeight.toFloat()) * 0.05f, Interpolation.pow2In))
            }
        }

        if (!tale.hasActions()) {
            inputDir.let {
                if (it != null && movable[it()]) {
                    // アイテム取得後の初入力で成長
                    if (canGrow) {
                        SnakeBody(asset, fieldMap, head.indexX, head.indexY, false, head.direction).let {
                            fieldMap.addActor(it)
                            bodyGroup.addActor(it)
                            bodies.add(1, it)
                        }
                        val (dix, diy) = it.getDIndex()
                        head.direction = it
                        head.moveBy(dix, diy)
                        head.addAction(Actions.moveBy(dix * PANEL_UNIT, diy * PANEL_UNIT, 0.4f, Interpolation.exp10In))
                        head.toFront()
                        tale.addAction(Actions.delay(0.7f))
                        canGrow = false
                    } else proceed(it) // 入力による移動
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