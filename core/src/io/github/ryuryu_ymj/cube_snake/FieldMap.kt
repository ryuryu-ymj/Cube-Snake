package io.github.ryuryu_ymj.cube_snake

import com.badlogic.gdx.scenes.scene2d.Stage

class FieldMap(val sizeX: Int = 0, val sizeY: Int = 0) {
    private val fieldMap = Array<Array<MyActor?>>(sizeX) { arrayOfNulls(sizeY) }
    private val change = mutableListOf<Triple<MyActor, Int, Int>>()

    fun begin() {
        change.clear()
    }

    fun end() {
        change.forEach { (actor, ix, iy) ->
            fieldMap[actor.indexX][actor.indexY] = null
            actor.indexX = ix
            actor.indexY = iy
        }
        change.forEach { (actor, ix, iy) ->
            fieldMap[ix][iy] = actor
        }
    }

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
        change.add(Triple(actor, indexX, indexY))
    }

    fun moveActorBy(actor: MyActor, dIndexX: Int = 0, dIndexY: Int = 0) {
        moveActor(actor, actor.indexX + dIndexX, actor.indexY + dIndexY)
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