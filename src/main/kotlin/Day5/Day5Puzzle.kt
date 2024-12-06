package advent2024.Day5

import advent2024.util.FileReaderUtil

class Day5Puzzle {

    fun solvePart1() : Int {
        val fileReader = FileReaderUtil()
        val ruleLines = fileReader.readFileAsLines("Day5-rules.txt")
        val ruleList = mutableListOf<Pair<Int,Int>>()
        for(line in ruleLines!!){
            ruleList.add(fileReader.convertLineToPairOfNumbers(line))
        }
        val codeLinesRead = fileReader.readFileAsLines("Day5.txt")
        val codeLines = mutableListOf<List<Int>>()
        for(line in codeLinesRead!!){
            codeLines.add(fileReader.convertLineToListOfNumbers(line))
        }
        val middlePagesCorrect = mutableListOf<Int>()
        val incorrectPages = mutableListOf<Int>()
        for(line in codeLines){
            if(checkLineForRules(ruleList,line)){
                middlePagesCorrect.add(line[line.size /2])
            }
            else{
                //Part Two
                val item = orderInvalidRowByRules(line,ruleList)
                if(item.isNotEmpty()) incorrectPages.add(item[item.size/2])

            }
        }
        println("Part 2: ${incorrectPages.sum()}")
        return middlePagesCorrect.sum()
    }

    fun checkLineForRules(rules: List<Pair<Int,Int>>, codeLine : List<Int>): Boolean{
        for(rule in rules){
            val leftRule = codeLine.indexOf(rule.first)
            val rightRule = codeLine.indexOf(rule.second)
            //If the rule is broken AND both items exists
            if((rightRule < leftRule && leftRule != -1 && rightRule != -1) ) return false
        }
        return true
    }


    fun orderInvalidRowByRules(numbers: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
        val graph = mutableMapOf<Int, MutableList<Int>>()
        val inDegree = mutableMapOf<Int, Int>()

        // Initialize graph and in-degree
        for (number in numbers) {
            graph[number] = mutableListOf()
            inDegree[number] = 0
        }

        // Build graph and calculate in-degree
        for ((first, second) in rules) {
            //Don't include rules that don't contain both numbers
            if(numbers.contains(first) && numbers.contains(second)) {
                graph[first]?.add(second)
                inDegree[second] = (inDegree[second] ?: 0) + 1
            }
        }

        val queue = ArrayDeque<Int>()
        val result = mutableListOf<Int>()

        // Add nodes with in-degree 0 to the queue
        for ((number, degree) in inDegree) {
            if (degree == 0) {
                queue.add(number)
            }
        }


        /** Perform topological sort - Kahn's Algorithm
         * result is empty List that will contain all the sorted elements
         * Queue - Set of all nodes with no incoming edges
         **/
        while (queue.isNotEmpty()) {
            //Remove a node n from queue
            val current = queue.removeFirst()
            // Add node n to result
            result.add(current)

            //for each node m with an edge e from n to m
            for (neighbor in graph[current]!!) {
                //remove edge e from graphe
                inDegree[neighbor] = inDegree[neighbor]!! - 1
                //if m has no other incoming edges then
                if (inDegree[neighbor] == 0) {
                    //add m into queue
                    queue.add(neighbor)
                }
            }
        }
        //if result and numbers (original ordered list) are the same size return the newly ordered list,
        // otherwise an empty list because an error occurred
        return if (result.size == numbers.size) result else listOf() // Return empty list if cycle exists
    }

}