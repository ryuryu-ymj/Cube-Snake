package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.viewport.FitViewport

class PlayScreen(private val asset: AssetManager) : Screen {
    lateinit var stage: Stage
    lateinit var batch: SpriteBatch
    lateinit var stageUi: Stage
    lateinit var batchUi: SpriteBatch
    lateinit var camera: OrthographicCamera
    lateinit var font: BitmapFont

    var score = 0

    private var fieldMap = FieldMap()
    private val blocks = mutableListOf<Block>()
    lateinit var snake: Snake
    private lateinit var goal: Goal

    override fun show() {
        camera = OrthographicCamera(30f, (30 * Gdx.graphics.height / Gdx.graphics.width).toFloat())
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f)
        camera.update()
        batch = SpriteBatch()
        batch.projectionMatrix = camera.combined
        stage = Stage(FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera), batch)
        Gdx.input.inputProcessor = stage

        batchUi = SpriteBatch()
        stageUi = Stage(FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT), batchUi)

        font = asset.get("font.ttf")

        readStage(1)
    }

    override fun hide() {
    }

    override fun render(delta: Float) {
        camera.position.set(snake.head.x, camera.position.y, 0f)
        camera.update()
        batch.projectionMatrix = camera.combined

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.draw()

        batchUi.begin()
        font.draw(batchUi, "点数: $score", 20f, VIEWPORT_HEIGHT - 20)
        batchUi.end()

        stageUi.draw()

        snake.run {
            // 移動方向の制限
            movable[Direction.LEFT()] = !fieldMap[head.indexX - 1, head.indexY].let { it is Building || it is SnakeBody }
            movable[Direction.RIGHT()] = !fieldMap[head.indexX + 1, head.indexY].let { it is Building || it is SnakeBody }
            movable[Direction.DOWN()] = !fieldMap[head.indexX, head.indexY - 1].let { it is Building || it is SnakeBody }
            movable[Direction.UP()] = !fieldMap[head.indexX, head.indexY + 1].let { it is Building || it is SnakeBody }

            // 落下
            if (!bodies.any {
                        fieldMap[it.indexX, it.indexY - 1] is Building
                    }) fall()

            // ゴール
            if (head.indexX to head.indexY == goal.entranceIndexXAndY) {
                if (inputDir.let { it != null && it.reverse == goal.direction }) {
                    println("goal")
                }
            }
        }

        stage.act()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
        stageUi.viewport.update(width, height)
    }

    override fun dispose() {
        stage.dispose()
        batch.dispose()
        batchUi.dispose()
    }

    private fun readStage(stageNum: Int) {
        try {
            val file = Gdx.files.internal("stage${"%02d".format(stageNum)}.txt")
            val lines = file.reader().readLines().asReversed()
            fieldMap = FieldMap(lines.map { it.length / 2 }.max() ?: 0, lines.size)
            for ((iy, line) in lines.withIndex()) {
                for ((ix, cell) in line.chunked(2).withIndex()) {
                    when (cell) {
                        "b0" -> Block(asset, fieldMap, ix, iy).let {
                            blocks.add(it)
                            fieldMap.addActor(stage, it)
                        }
                        "p0" -> snake = Snake(asset, stage, fieldMap, ix, iy, 8)
                        "gl" -> goal = Goal(asset, fieldMap, ix, iy, Direction.LEFT).also {
                            fieldMap.addActor(stage, it)
                        }
                        "gr" -> goal = Goal(asset, fieldMap, ix, iy, Direction.RIGHT).also {
                            fieldMap.addActor(stage, it)
                        }
                        "gd" -> goal = Goal(asset, fieldMap, ix, iy, Direction.DOWN).also {
                            fieldMap.addActor(stage, it)
                        }
                        "gu" -> goal = Goal(asset, fieldMap, ix, iy, Direction.UP).also {
                            fieldMap.addActor(stage, it)
                        }
                    }
                }
            }
        } catch (err: GdxRuntimeException) {
            println("ステージファイルの読み取りに失敗しました ${err.message}")
        }
    }
}