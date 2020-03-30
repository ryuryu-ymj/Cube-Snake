package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport

class StageStartScreen(asset: AssetManager): Screen {
    private val batch = SpriteBatch()
    private val stage = Stage(FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT), batch)
    private val font = asset.get<BitmapFont>("font.ttf")
    private var stageNum = 1
    private var count = 0

    override fun show() {
        count = 0
    }

    override fun hide() {
        stageNum++
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        font.draw(batch, "stage $stageNum", VIEWPORT_WIDTH / 2 - font.spaceXadvance * "stage $stageNum".length / 2,
                VIEWPORT_HEIGHT / 2 - font.capHeight / 2)
        batch.end()

        stage.draw()

        count++
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        batch.dispose()
        stage.dispose()
    }

    fun toPlayScreen() = count > 60
}