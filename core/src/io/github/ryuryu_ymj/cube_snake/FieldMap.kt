package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.scenes.scene2d.Stage

class FieldMap(val sizeX: Int = 0, val sizeY: Int = 0) {
    private var fieldMap = Array<Array<MyActor?>>(sizeX) { arrayOfNulls(sizeY) }

    fun addActor(stage: Stage, actor: MyActor, indexX: Int = actor.indexX, indexY: Int = actor.indexY) {
        try {
            actor.let {
                fieldMap[indexX][indexY] = it
                it.indexX = indexX
                it.indexY = indexY
                stage.addActor(it)
            }
        } catch (err: ArrayIndexOutOfBoundsException) {
            println("filedMapのaddActor($stage, $actor, $indexX, $indexY)がsizeX:$sizeX, sizeY:$sizeY の範囲を超えました")
        }
    }

    fun moveActor(actor: MyActor, indexX: Int, indexY: Int) {
        try {
            actor.let {
                fieldMap[it.indexX][it.indexY] = null
                fieldMap[indexX][indexY] = it
                it.indexX = indexX
                it.indexY = indexY
            }
        } catch (err: ArrayIndexOutOfBoundsException) {
            println("filedMapのmoveActor($actor, $indexX, $indexY)がsizeX:$sizeX, sizeY:$sizeY の範囲を超えました")
        }
    }

    /**
     * 複数のactorを同時に動かすときはこれを使って
     */
    fun moveActors(actors: Array<MyActor>, indexXs: IntArray, indexYs: IntArray) {
        actors.forEachIndexed { i, actor ->
            fieldMap[actor.indexX][actor.indexY] = null
            actor.indexX = indexXs[i]
            actor.indexY = indexYs[i]
        }
        actors.forEach {
            fieldMap[it.indexX][it.indexY] = it
        }
    }

    fun moveActorByChange(actor: MyActor, dIndexX: Int = 0, dIndexY: Int = 0) {
        try {
            actor.let {
                fieldMap[it.indexX][it.indexY] = null
                it.indexX += dIndexX
                it.indexY += dIndexY
                fieldMap[it.indexX][it.indexY] = it
            }
        } catch (err: ArrayIndexOutOfBoundsException) {
            println("filedMapのaddActor($actor, $dIndexX, $dIndexY)がsizeX:$sizeX, sizeY:$sizeY の範囲を超えました")
        }
    }

    /**
     * 複数のactorを同時に動かすときはこれを使って
     */
    fun moveActorsByChange(actors: Array<MyActor>, dIndexXs: IntArray = IntArray(actors.size) {0},
                           dIndexYs: IntArray = IntArray(actors.size) {0}) {
        actors.forEachIndexed { i, actor ->
            fieldMap[actor.indexX][actor.indexY] = null
            actor.indexX += dIndexXs[i]
            actor.indexY += dIndexYs[i]
        }
        actors.forEach {
            fieldMap[it.indexX][it.indexY] = it
        }
    }

    fun deleteActor(actor: MyActor) {
        actor.let {
            it.remove()
            fieldMap[it.indexX][it.indexY] = null
        }
    }

    operator fun get(indexX: Int, indexY: Int): MyActor? {
        return try {
            fieldMap[indexX][indexY]
        } catch (err: ArrayIndexOutOfBoundsException) {
            println("filedMapのget($indexX, $indexY)がsizeX:$sizeX, sizeY:$sizeY の範囲を超えました")
            null
        }
    }
}