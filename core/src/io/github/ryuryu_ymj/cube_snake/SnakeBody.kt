package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

class SnakeBody(ix: Int, iy: Int) : MyActor(ix, iy) {
    companion object {
        private val texture = Texture("snake_body.png")
    }
    var direction = Direction.RIGHT

    init {
        /* 拡大・縮小時も滑らかにする. */
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        width = texture.width.toFloat()
        height = texture.height.toFloat()
        setPosition((ix * 100).toFloat(), (iy * 100).toFloat())
    }

    fun dispose() {
        clear()
        texture.dispose()
    }

    override fun act(delta: Float) {
        super.act(delta)
        x = ix.toFloat() * 100
        y = iy.toFloat() * 100
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.run {
            setColor(color.r, color.g, color.b, color.a * parentAlpha)
            draw(texture, x - width / 2, y - height / 2)
        }
    }
}