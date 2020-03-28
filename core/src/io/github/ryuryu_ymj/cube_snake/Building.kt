package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager

abstract class Building(asset: AssetManager, fieldMap: FieldMap, indexX: Int = 0, indexY: Int = 0)
    : MyActor(asset, fieldMap, indexX, indexY)