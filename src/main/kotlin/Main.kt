package advent2024

import advent2024.Day1.Day1Puzzle
import advent2024.day2Puzzle.Day2Puzzle
import advent2024.day3.Day3Puzzle

fun main() {
    val day1 = Day1Puzzle()
    println("Day1 Part 2: Total Distance ${day1.solvePart2()}")
    val day2 = Day2Puzzle()
    println("Day2 Part 2: Number of Safe rows ${day2.solvePart2()}")

    val day3 = Day3Puzzle()
    println("Day 3 Part 1+2  ${day3.solveDay3()}")
}