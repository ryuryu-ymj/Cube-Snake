package io.github.ryuryu_ymj.cube_snake

enum class Direction(val index: Int, val degree: Float) {
    LEFT(0, 180f), RIGHT(1, 0f), DOWN(2, 270f), UP(3, 90f);

    /** index */
    operator fun invoke() = index

    val reverse
        get() = when (this) {
            LEFT -> RIGHT
            RIGHT -> LEFT
            DOWN -> UP
            UP -> DOWN
        }

    fun getDIndex() =
            when (this) {
                RIGHT -> 1; LEFT -> -1; else -> 0
            } to when (this) {
                UP -> 1; DOWN -> -1; else -> 0
            }

    companion object {
        fun getDirFromInitial(initial: Char) =
                when (initial) {
                    'l' -> LEFT
                    'r' -> RIGHT
                    'd' -> DOWN
                    'u' -> UP
                    else -> null
                }
    }
}