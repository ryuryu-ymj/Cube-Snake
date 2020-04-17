package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import kotlin.math.sqrt

open class SnakeAutoProceed(asset: AssetManager, stage: Stage, fieldMap: FieldMap, indexX: Int, indexY: Int, bodyCnt: Int)
    : Snake(asset, stage, fieldMap, indexX, indexY, bodyCnt) {
    var proceedDir = Direction.RIGHT

    override fun act(delta: Float) {
        if (movable.all { !it }) die() // 自死

        // 落下
        if (fallHeight > 0 && !tale.hasActions()) {
            isFalling = true
            bodies.forEach {
                it.addAction(Actions.moveBy(0f, -fallHeight * PANEL_UNIT, sqrt(fallHeight.toFloat()) * 0.05f, Interpolation.pow2In))
            }
        }
        if (isFalling) {
            if (fallHeight > 0) bodies.forEach {
                it.moveBy(0, -1)
            } else isFalling = false
        }

        inputDir?.let { if (movable[it()]) proceedDir = it }
        if (!tale.hasActions()) {
            proceedDir.let {
                if (movable[it()]) {
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
}