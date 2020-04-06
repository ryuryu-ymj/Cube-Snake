package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.assets.AssetManager

abstract class Building(asset: AssetManager, fieldMap: FieldMap, indexX: Int, indexY: Int)
    : MyActor(asset, fieldMap, indexX, indexY)