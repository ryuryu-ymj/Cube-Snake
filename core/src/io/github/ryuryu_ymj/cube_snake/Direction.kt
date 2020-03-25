package io.github.ryuryu_ymj.cube_snake

enum class Direction(val index: Int, val degree: Float) {
    LEFT(0, 180f), RIGHT(1, 0f), DOWN(2, 270f), UP(3, 90f);

    /** index */
    operator fun invoke() = index
}