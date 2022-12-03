package dev.danzou.kotlin

import dev.danzou.kotlin.utils.AdventTestRunner

internal class Day1 : AdventTestRunner() {
    override fun part1(input: String): Number =
        input.split("\n\n")
            .map { it.split("\n").map { it.toLong() } }
            .map { it.sum() }
            .max()

    override fun part2(input: String): Number =
        input.split("\n\n")
            .map { it.split("\n").map { it.toLong() } }
            .map { it.sum() }
            .sorted()
            .takeLast(3)
            .sum()
}