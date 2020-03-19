package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

class Block(ix: Int, iy: Int) : MyActor(ix, iy) {
    private val texture = Texture("block.png")

    init {
        /* 拡大・縮小時も滑らかにする. */
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        width = texture.width.toFloat()
        height = texture.height.toFloat()
        setPosition(ix.toFloat() * 100, iy.toFloat() * 100)
    }

    fun dispose() {
        clear()
        texture.dispose()
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.run {
            setColor(color.r, color.g, color.b, color.a * parentAlpha)
            draw(texture, x - width / 2, y - height / 2)
        }
    }
}