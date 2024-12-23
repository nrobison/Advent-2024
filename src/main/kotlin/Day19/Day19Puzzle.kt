package advent2024.Day19

import advent2024.util.FileReaderUtil

class Day19Puzzle {

    val availableTowels = mutableListOf<String>()
    val neededCombos = mutableListOf<String>()
    var foundCombos = 0
    fun solve(){
        val fileReader = FileReaderUtil()
        val linesOfText = fileReader.readFileAsLines("Day19.txt")!!.filterNot { it == "\n" || it.isEmpty() }
        for(i in linesOfText.indices){
            if(i == 0){
                linesOfText[i].split(',').forEach { chars ->
                    availableTowels.add(chars.trim())
                }
            }
            else{
                neededCombos.add(linesOfText[i].trim())
            }
        }

        neededCombos.forEach { findCombo(it)}
        println("Number of Possible Combos : $foundCombos")
        foundCombos = 0
        val item = neededCombos.sumOf { backTrackAll(it) }
        println("Number of all possible #2: $foundCombos")
    }

    fun backTrackAll(target: String, map: MutableMap<String,Long> = mutableMapOf()) : Long=
        if (target.isEmpty()) 1
        //using memorization to keep track of already solved/used we either get or put the target in our map
        else map.getOrPut(target) {
            //Filter the available towels for which target starts with it and then sum
            availableTowels.filter { target.startsWith(it) }.sumOf {
                //Recursion call where we remove the match prefix (it) from the target
                backTrackAll(target.removePrefix(it), map)
            }
        }

    fun findCombo(combo: String){
        if(backTracking(combo,"")){
            foundCombos ++
        }
    }

    fun backTracking(targetCombo: String, currentString: String) : Boolean {

        if(currentString == targetCombo){
            //println("Found True: looking for $targetCombo and found $currentString")
            return true
        }
        //String too long
        if(currentString.length > targetCombo.length) return false
        //Current string is not a pre-fix
        if(!targetCombo.startsWith(currentString) && currentString.isNotEmpty()) return false

        availableTowels.forEach { towel ->
            if(backTracking(targetCombo, currentString.plus(towel))) return true
            //else canSolve = false
        }
        return false
    }
}