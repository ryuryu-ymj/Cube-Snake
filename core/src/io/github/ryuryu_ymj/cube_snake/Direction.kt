package io.github.ryuryu_ymj.cube_snake

enum class Direction(val index: Int) {
    LEFT(0), RIGHT(1), DOWN(2), UP(3);

    /** index */
    operator fun invoke() = index
}