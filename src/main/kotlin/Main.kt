package advent2024

import advent2024.Day1.Day1Puzzle
import advent2024.Day10.Day10Puzzle
import advent2024.Day11.Day11Puzzle
import advent2024.Day12.Day12Puzzle
import advent2024.Day13.Day13Puzzle
import advent2024.Day14.Day14Puzzle
import advent2024.Day15.Day15Puzzle
import advent2024.Day16.Day16Puzzle
import advent2024.Day17.Day17Puzzle
import advent2024.Day18.Day18Puzzle
import advent2024.Day19.Day19Puzzle
import advent2024.Day20.Day20Puzzle
import advent2024.Day21.Day21Puzzle
import advent2024.Day22.Day22Puzzle
import advent2024.Day23.Day23Puzzle
import advent2024.Day24.Day24Puzzle
import advent2024.Day25.Day25Puzzle
import advent2024.Day4.Day4Puzzle
import advent2024.Day5.Day5Puzzle
import advent2024.Day6.Day6Puzzle
import advent2024.Day7.Day7Puzzle
import advent2024.Day8.Day8Puzzle
import advent2024.Day9.Day9Puzzle
import advent2024.day2Puzzle.Day2Puzzle
import advent2024.day3.Day3Puzzle

fun main() {
    val day1 = Day1Puzzle()
    println("Day1 Part 2: Total Distance ${day1.solvePart2()}")
    val day2 = Day2Puzzle()
    println("Day2 Part 2: Number of Safe rows ${day2.solvePart2()}")

    val day3 = Day3Puzzle()
    println("Day 3 Part 1+2  ${day3.solveDay3()}")

    val day4 = Day4Puzzle()
    println("Day 4 Part 1 ${day4.solvePart1()}")
    println("Day 4 Part 2 ${day4.solvePart2()}")

    val day5 = Day5Puzzle()
    println("Day 5 Part 1: ${day5.solvePart1()}")

    val day6 = Day6Puzzle()
    day6.solve()
    val day7 = Day7Puzzle()
    println("Day7 Part 1 total of solvable ${day7.solvePart1()}")
    println("Day 7 Part 2 total: ${day7.solvePart2()}")
    val day8 = Day8Puzzle()
    day8.solve()
    val day9 = Day9Puzzle()
    day9.solvePart1()
    val day10 = Day10Puzzle()
    day10.solve(false)
    day10.solve(true)
    val day11 = Day11Puzzle()
    //day11.solve(25)
    day11.solvePart2(75)
    val day12 = Day12Puzzle()
    day12.solve()
    val day13 = Day13Puzzle()
    day13.solve(true)
    val day14 = Day14Puzzle()
    day14.solve()
    val day15 = Day15Puzzle()
    day15.solve()
    Day16Puzzle().solve()
    val day17 = Day17Puzzle()
    day17.solve()
    Day18Puzzle().solve()
    val day19 = Day19Puzzle()
    day19.solve()
    val day20 = Day20Puzzle()
    day20.solve()
    val day21 = Day21Puzzle()
 //   day21.solve()
    Day22Puzzle().solve()
    Day23Puzzle().solve()
    Day24Puzzle().solve()
    Day25Puzzle().solve()
}