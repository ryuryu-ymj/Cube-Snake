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
import com.badlogic.gdx.utils.GdxRuntimeException


const val VIEWPORT_WIDTH = 2560f
const val VIEWPORT_HEIGHT = 1440f
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

    var font: BitmapFont? = null

    override fun create() {
        //アセットの読み込み
        asset.load("block.png", Texture::class.java)
        asset.load("goal.png", Texture::class.java)
        asset.load("snake_body.png", Texture::class.java)

        val resolver: FileHandleResolver = InternalFileHandleResolver()
        asset.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        asset.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))
        val myFont = FreeTypeFontLoaderParameter().apply {
            fontFileName = "font.ttf"
            fontParameters.apply {
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
            }
        }
        asset.load("font.ttf", BitmapFont::class.java, myFont)

        asset.finishLoading()

        setScreen(PlayScreen(asset))
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        asset.dispose()
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
}