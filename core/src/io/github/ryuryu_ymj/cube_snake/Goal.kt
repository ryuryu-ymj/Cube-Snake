package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Goal : MyActor() {
    private val texture: TextureRegion by lazy { TextureRegion(Texture("goal.png")) }
    private var direction = Direction.LEFT

    fun create(panelX: Int, panelY: Int, direction: Direction) {
        /* 拡大・縮小時も滑らかにする. */
        texture.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        width = texture.texture.width.toFloat()
        height = texture.texture.height.toFloat()
        this.panelX = panelX
        this.panelY = panelY
        this.direction = direction
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