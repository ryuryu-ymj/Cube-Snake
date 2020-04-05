package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture

class Cherry(asset: AssetManager, fieldMap: FieldMap, indexX: Int, indexY: Int)
    : Item(asset.get("cherry.png"), asset, fieldMap, indexX, indexY) {
}