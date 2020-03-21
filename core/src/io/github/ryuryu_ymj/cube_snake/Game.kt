package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.viewport.FitViewport

const val VIEWPORT_WIDTH = 2560f
const val VIEWPORT_HEIGHT = 1440f

class Game : ApplicationAdapter() {
    private lateinit var stage: Stage
    private lateinit var batch: SpriteBatch
    private lateinit var stageUi: Stage
    private lateinit var batchUi: SpriteBatch
    private lateinit var camera: OrthographicCamera

    private var font: BitmapFont? = null
    private var score = 0

    private val blocks = mutableListOf<Block>()
    private val snake = Snake()

    override fun create() {
        //batch.projectionMatrix = stage.camera.combined
        camera = OrthographicCamera(30f, (30 * Gdx.graphics.height / Gdx.graphics.width).toFloat())
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        camera.update()
        batch = SpriteBatch()
        batch.projectionMatrix = camera.combined
        stage = Stage(FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera), batch)
        Gdx.input.inputProcessor = stage

        batchUi = SpriteBatch()
        stageUi = Stage(FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT), batchUi)

        readStage(1)

        createFont()
    }

    override fun render() {
        draw()
        update()
    }

    private fun draw() {
        camera.position.set(snake.x, camera.position.y, 0f)
        camera.update()
        batch.projectionMatrix = camera.combined

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.draw()

        batchUi.begin()
        font?.draw(batchUi, "点数: $score", 20f, VIEWPORT_HEIGHT - 20)
        batchUi.end()

        stageUi.draw()
    }

    private fun update() {
        /*touchPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        stage.screenToStageCoordinates(touchPoint)*/

        stage.act()

        if (!snake.bodies.any { body ->
                    blocks.any { it.iy == body.iy - 1 && it.ix == body.ix }
                }) snake.fall()

    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }

    override fun dispose() {
        stage.dispose()
        batch.dispose()
        font?.dispose()
    }

    private fun createFont() {
        try {
            val generator = FreeTypeFontGenerator(Gdx.files.internal("font.ttf"))
            font = generator.generateFont(
                    FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                        size = 48
                        color = Color.CYAN
                        incremental = true
                        magFilter = Texture.TextureFilter.Linear
                        minFilter = Texture.TextureFilter.Linear
                        borderWidth = 2f
                        borderColor = Color.DARK_GRAY
                        shadowColor = Color.BROWN
                        shadowOffsetX = 7
                        shadowOffsetY = 7
                    })
        } catch (err: GdxRuntimeException) {
            println("フォントファイルの読み取りに失敗しました ${err.message}")
        }
    }

    private fun readStage(stageNum: Int) {
        try {
            val file = Gdx.files.internal("stage${"%02d".format(stageNum)}.txt")
            for ((iy, line) in file.reader().readLines().asReversed().withIndex()) {
                for ((ix, cell) in line.split(",").withIndex()) {
                    when (cell) {
                        "b" -> Block(ix, iy).let {
                            blocks.add(it)
                            stage.addActor(it)
                        }
                        "p" -> snake.let {
                            stage.addActor(it)
                            it.create(ix, iy, 8, blocks)
                        }
                    }
                }
            }
        } catch (err: GdxRuntimeException) {
            println("ステージファイルの読み取りに失敗しました ${err.message}")
        }
    }
}