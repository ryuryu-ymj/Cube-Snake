package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Goal(fieldMap: FieldMap, indexX: Int, indexY: Int, val direction: Direction) : Building(fieldMap, indexX, indexY) {
    private val texture = TextureRegion(Texture("goal.png"))
    val entranceIndexXY
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
        texture.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        width = texture.texture.width.toFloat()
        height = texture.texture.height.toFloat()
    }

    override fun dispose() {
        texture.texture.dispose()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.run {
            setColor(color.r, color.g, color.b, color.a * parentAlpha)
            draw(texture, x - width / 2, y - height / 2, width / 2, height / 2, width, height, 1f, 1f, direction.degree)
        }
    }
}