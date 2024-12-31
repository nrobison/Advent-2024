package advent2024.Day25

import advent2024.util.FileReaderUtil
import kotlin.math.abs

class Day25Puzzle {

    fun solve(){
        val input  = FileReaderUtil().readFileAsLines("Day25.txt")!!.filterNot { it.isEmpty() }.map { it -> it.toCharArray() }
        val keys = mutableListOf<List<Int>>()
        val locks = mutableListOf<List<Int>>()
        for(item in input.indices step 7){
            val holder = input.subList(item,item+7)
            val maxColumns = holder.maxOfOrNull { it.size } ?: 0
            val combinedRows = (0 until maxColumns).map { col ->
                holder.mapNotNull { row -> row.getOrNull(col) }
            }
            val isLock = combinedRows[0][0] == '#'
            val pins = mutableListOf<Int>()
            combinedRows.forEach { pins.add(getHeightsOf(it,isLock)) }
            if(isLock) locks.add(pins)
            else keys.add(pins)
        }
        findPairs(keys,locks)

    }
    private fun findPairs(keys: List<List<Int>>, locks: List<List<Int>>){
        val items = locks.map { lock -> val matches = keys.filter { key -> compareKeyToLock(key,lock) }
            Pair(lock, matches)  }
        val counts = items.sumOf { it.second.size }
        println("Part 1 Number of unique lock + key combos with no overlap: $counts")
    }

    private fun compareKeyToLock(key: List<Int>, lock: List<Int>) : Boolean{
        for(height in key.indices){
            if(key[height] +  lock[height] > 5) return false
        }
        return true
    }

    private fun getHeightsOf(input: List<Char>, isLock: Boolean) :Int{
        if(isLock){
            return input.indexOfLast { it == '#' }
        }
        return (input.size-1) - input.indexOfFirst { it == '#' }
    }
}