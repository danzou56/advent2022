package dev.danzou.kotlin

import dev.danzou.kotlin.utils.AdventTestRunner

internal class Day3 : AdventTestRunner() {
    fun priorityOf(char: Char): Int =
        if (char.isUpperCase()) char - 'A' + 27
        else char - 'a' + 1

    override fun part1(input: String): Number {
        return input.split("\n")
            .map { it.toList() }
            .map { Pair(it.subList(0, it.size / 2), it.subList(it.size / 2, it.size)) }
            .map { Pair(it.first.toSet(), it.second.toSet()) }
            .map { it.first.intersect(it.second) }
            .map { priorityOf(it.toList().first()) }
            .sum()
    }

    override fun part2(input: String): Number {
        return input.split("\n")
            .map { it.toList() }
            .chunked(3)
            .map { it.map { it.toSet() } }
            .map { it.reduce(Set<Char>::intersect) }
            .map { priorityOf(it.toList().first()) }
            .sum()
    }
}