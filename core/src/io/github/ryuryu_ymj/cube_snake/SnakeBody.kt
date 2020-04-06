package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class SnakeBody(asset: AssetManager, fieldMap: FieldMap, ix: Int, iy: Int, isHead: Boolean) : MyActor(asset, fieldMap, ix, iy) {
    private val region = TextureRegion(asset.get<Texture>(if (isHead) "snake_head.png" else "snake_body.png"))
    var direction = Direction.RIGHT

    init {
        /* 拡大・縮小時も滑らかにする. */
        region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        width = region.texture.width.toFloat()
        height = region.texture.height.toFloat()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.run {
            setColor(color.r, color.g, color.b, color.a * parentAlpha)
            if (direction == Direction.LEFT) draw(region, x + width / 2, y - height / 2, -width, height)
            else draw(region, x - width / 2, y - height / 2, width / 2, height / 2, width, height, 1f, 1f, direction.degree)
        }
    }
}