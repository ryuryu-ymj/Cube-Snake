package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Goal(asset: AssetManager, fieldMap: FieldMap, indexX: Int, indexY: Int, val direction: Direction)
    : Building(asset, fieldMap, indexX, indexY) {
    private val region = TextureRegion(asset.get<Texture>("goal.png"))
    val entranceIndexXAndY
        get() = indexX + when (direction) {
            Direction.RIGHT -> 1
            Direction.LEFT -> -1
            else -> 0
        } to indexY + when (direction) {
            Direction.UP -> 1
            Direction.DOWN -> -1
            else -> 0
        }

    init {
        /* 拡大・縮小時も滑らかにする. */
        region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        width = region.texture.width.toFloat()
        height = region.texture.height.toFloat()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.run {
            setColor(color.r, color.g, color.b, color.a * parentAlpha)
            draw(region, x - width / 2, y - height / 2, width / 2, height / 2, width, height, 1f, 1f, direction.degree)
        }
    }
}