package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter


const val VIEWPORT_WIDTH = 1500f * 16 / 9
const val VIEWPORT_HEIGHT = 1500f
const val PANEL_UNIT = 100
val inputDir: Direction?
    get() = when {
        Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) -> Direction.LEFT
        Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D) -> Direction.RIGHT
        Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S) -> Direction.DOWN
        Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W) -> Direction.UP
        else -> null
    }

class Game : Game() {
    private val asset = AssetManager()
    private lateinit var playScreen: PlayScreen
    private lateinit var stageStartScreen: StageStartScreen

    override fun create() {
        //画像の読み込み
        asset.load("block.png", Texture::class.java)
        asset.load("goal.png", Texture::class.java)
        asset.load("snake_body.png", Texture::class.java)
        asset.load("snake_head.png", Texture::class.java)
        asset.load("cherry.png", Texture::class.java)

        //フォントの読み込み
        val resolver: FileHandleResolver = InternalFileHandleResolver()
        asset.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        asset.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))
        val myFont = FreeTypeFontLoaderParameter().apply {
            fontFileName = "font.ttf"
            fontParameters.apply {
                size = 48
                color = Color.BLACK
                incremental = true
                magFilter = Texture.TextureFilter.Linear
                minFilter = Texture.TextureFilter.Linear
            }
        }
        asset.load("font.ttf", BitmapFont::class.java, myFont)

        asset.finishLoading() //アセットの読み込みを完了する

        playScreen = PlayScreen(asset)
        stageStartScreen = StageStartScreen(asset)
        setScreen(stageStartScreen)
    }

    override fun render() {
        super.render()
        when (screen) {
            is StageStartScreen -> {
                if (stageStartScreen.toPlayScreen()) {
                    setScreen(playScreen)
                }
            }
            is PlayScreen -> {
                if (playScreen.toNextStage) {
                    setScreen(stageStartScreen)
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        asset.dispose()
    }
}