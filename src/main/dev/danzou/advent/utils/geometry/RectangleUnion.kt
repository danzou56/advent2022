package dev.danzou.advent.utils.geometry

import dev.danzou.advent.utils.*
import kotlin.math.max
import kotlin.math.min

internal typealias Rect = Pair<Point, Point>

open class RectangleUnion(rectangles: List<Rect>) : Polygon {
    // rectangles are sorted by their highest point
    // pieces on top of stack are first
    protected val _rectangles = rectangles.sortedByDescending { it.upper(1) }
    override val height = 1 + _rectangles.maxOf { it.upper(1) } - _rectangles.minOf { it.lower(1) }
    override val width = 1 + _rectangles.maxOf { it.upper(0) } - _rectangles.minOf { it.lower(0) }
    override val pos = Pos(
        _rectangles.minOf { it.lower(0) },
        _rectangles.minOf { it.lower(1) },
    )

    constructor(rectangle: Rect) : this(listOf(rectangle))

    companion object {
        fun fromRectangles(rectangles: List<Rectangle>): RectangleUnion =
            RectangleUnion(rectangles.map { it._rectangles.first() })
        fun fromRectangles(vararg rectangles: Rectangle): RectangleUnion =
            RectangleUnion(rectangles.map { it._rectangles.first() })
    }

    override fun contains(p: Pos): Boolean =
        !_rectangles.map { rect -> (0 until DIMENSION).map { dim ->
            p[dim] < rect.lower(dim) || p[dim] > rect.upper(dim)
        }.reduce(Boolean::or) }.reduce(Boolean::or)

    override fun contains(other: Polygon): Boolean {
        TODO("Not yet implemented")
    }

    override fun isDisjointFrom(other: Polygon): Boolean {
        TODO("Not yet implemented")
    }

    override fun intersects(other: Polygon): Boolean =
        when (other) {
            is RectangleUnion -> this._rectangles.asReversed().any { thisRect ->
                other._rectangles.any { otherRect ->
                    thisRect.intersects(otherRect)
                }
            }
            is EmptyPolygon -> false
            else -> throw NotImplementedError()
        }

    override fun union(other: Polygon): Polygon =
        when (other) {
            is RectangleUnion -> RectangleUnion(_rectangles + other._rectangles)
            is EmptyPolygon -> this
            else -> throw NotImplementedError()
        }

    override fun plus(p: Pos): RectangleUnion =
        RectangleUnion(_rectangles.map { rect ->
            Pair(rect.first + p, rect.second + p)
        })

    protected fun Rect.lower(dim: Int): Int {
        return min(this.first[dim], this.second[dim])
    }

    protected fun Rect.upper(dim: Int): Int {
        return max(this.first[dim], this.second[dim])
    }

    protected fun Rect.fullBounds(): List<Point> =
        listOf(
            Pair(this.lower(0), this.lower(1)),
            Pair(this.lower(0), this.upper(1)),
            Pair(this.upper(0), this.lower(1)),
            Pair(this.upper(0), this.upper(1)),
        )

    @JvmName("containsPoint")
    protected fun Rect.contains(p: Point): Boolean =
        !(0 until DIMENSION).map { dim ->
            p[dim] < this.lower(dim) || p[dim] > this.upper(dim)
        }.reduce(Boolean::or)

    @JvmName("containsRect")
    protected fun Rect.contains(other: Rect): Boolean =
        this.contains(other.first) && this.contains(other.second)

    protected fun Rect.isDisjointFrom(other: Rect): Boolean =
        !this.intersects(other)

    protected fun Rect.intersects(other: Rect): Boolean =
        other.fullBounds().any { this.contains(it) }
}