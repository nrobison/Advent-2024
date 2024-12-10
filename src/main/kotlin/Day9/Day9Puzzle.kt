package advent2024.Day9

import advent2024.util.FileReaderUtil

class Day9Puzzle {

    data class DiskFile(val idNumber: Int, var value: Int, var isEmptySpace: Boolean, var alreadyMoved: Boolean){

    }
    fun solvePart1() {
        val fileReader = FileReaderUtil()
        val input = FileReaderUtil().convertLineOfTextToInts(fileReader.readFileAsText("day9.txt"))
        var diskMap = mutableListOf<DiskFile>()
        var isSpace = false
        var idNumber = 0
        for(value in input.indices){
            val currentItem = DiskFile(idNumber,input[value],isSpace,false)
            for(index in 0 until currentItem.value){
                diskMap.add(currentItem)
            }
            if(!isSpace) idNumber++
            isSpace = !isSpace
        }
        var checkSum = 0L
        val sortedMap = part1Sort(diskMap.toMutableList())
        for(item in sortedMap.indices){
            if(sortedMap[item].isEmptySpace) break
            checkSum += sortedMap[item].idNumber * item
        }
        println("Checksum: $checkSum")
        val listOfUniqueValues = diskMap.map { it.idNumber }.toSet().sortedDescending()
        val indexOfValues = mutableListOf<Pair<Int,Int>>()
       //Grab each all values (of desc order so all 9's, 8's etc move those in an ordered way
        for(values in listOfUniqueValues) {
            var index = diskMap.lastIndex
            //Reverse this go end to start
            while (index >= 0) {
                //if we found a match add it
                if (diskMap[index].idNumber == values && !diskMap[index].isEmptySpace) {
                    val length = findLengthOfFile(diskMap, index)
                    indexOfValues.add(Pair(index, length))
                    index -= length
                } else index--
            }
        }
        //Using the unsorted list and the set of moves, move them
        val part2Sorted = part2Sort(diskMap.toMutableList(),indexOfValues)
        //printDisk(diskMap)
       // printDisk(part2Sorted)
        checkSum = 0L
        for(item in part2Sorted.indices){
            if(!part2Sorted[item].isEmptySpace) checkSum += part2Sorted[item].idNumber * item
        }
        println("Checksum: $checkSum")
        println()

    }

    fun part2Sort(diskMap: MutableList<DiskFile>, listOfIndex: List<Pair<Int,Int>>) : MutableList<DiskFile>{
        for(index in listOfIndex){
            val length = index.second
            val emptySpaceStart = findNextIndexsOfFreeSpace(index.second,diskMap,index.first)
            if(emptySpaceStart != -1 && (emptySpaceStart + length) < diskMap.size && !diskMap[index.first].alreadyMoved) {
                for (i in 0 until length) {
                    val emptyFile = DiskFile(0,0,true,false)
                    val holder = diskMap[index.first - i]
                    holder.alreadyMoved = true
                    diskMap[emptySpaceStart + i] = holder
                    diskMap[index.first - i] = emptyFile
                }
            }
        }
        return diskMap
    }

    fun findLengthOfFile(diskMap: MutableList<DiskFile>, startPosition: Int) : Int{
        val value = diskMap[startPosition]
        var length = 0
        var position = startPosition
        while(diskMap[position] == value){
            if(diskMap[position] != value) break
            length++
            position -=1
            if(position < 0) break
        }
        return length
    }

    fun findNextIndexsOfFreeSpace(neededExemptSpace: Int, diskMap: MutableList<DiskFile>, maxRightLook: Int): Int{
        var emptySpaces = 0
        var emptySpaceStart = -1
        for(index in diskMap.indices){
            if(index >= maxRightLook) return emptySpaceStart
            if(diskMap[index].isEmptySpace) {
                if(emptySpaceStart == -1) emptySpaceStart = index
                emptySpaces++
            }
            else{
                emptySpaces = 0
                emptySpaceStart = -1
            }
            if(emptySpaces == neededExemptSpace) return emptySpaceStart
        }
        return emptySpaceStart
    }

    fun part1Sort(diskToSort: MutableList<DiskFile>) : MutableList<DiskFile> {
        var left = 0
        var right = diskToSort.lastIndex
        while(left < right) {
            while (right >= 0 && diskToSort[right].isEmptySpace) right--
            if(right < 0) break
            while (left < diskToSort.size && !diskToSort[left].isEmptySpace) left++

            if(left >= diskToSort.size) break

            val holder = diskToSort[left]
            diskToSort[left] = diskToSort[right]
            diskToSort[right] = holder
            left++
            right--
        }
        return diskToSort
    }

    fun printDisk(diskMap: MutableList<DiskFile>) {
        var line = ""
        for(map in diskMap){
            if(map.isEmptySpace) line += "."
            else line += map.idNumber
        }
        println("$line")
    }
}