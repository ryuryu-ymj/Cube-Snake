package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch

abstract class Item(val texture: Texture, asset: AssetManager, fieldMap: FieldMap, indexX: Int, indexY: Int)
    : MyActor(asset, fieldMap, indexX, indexY) {

    init {
        width = texture.width.toFloat()
        height = texture.height.toFloat()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.run {
            setColor(color.r, color.g, color.b, color.a * parentAlpha)
            draw(texture, x - width / 2, y - height / 2)
        }
    }
}