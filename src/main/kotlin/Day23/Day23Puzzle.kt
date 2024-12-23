package advent2024.Day23

import advent2024.util.FileReaderUtil

class Day23Puzzle {

    fun solve() {
        val adjacentPCs = mutableMapOf<String, MutableSet<String>>()
        val pcsWithT = HashSet<String>()
        val pairs = FileReaderUtil().readFileAsLines("Day23.txt")!!.map {
            val firstPort = it.subSequence(0, 2).toString()
            val secondPort = it.subSequence(3, 5).toString()
            //Generate all the links
            adjacentPCs.getOrPut(firstPort) { mutableSetOf() }.add(secondPort)
            adjacentPCs.getOrPut(secondPort) { mutableSetOf() }.add(firstPort)
            //Might as well get all the keys with T while we are here
            if (it[0] == 't') pcsWithT.add(firstPort)
            if (it[3] == 't') pcsWithT.add(secondPort)
            Pair(firstPort,secondPort)
        }
        val graphOfConnectedPCs = mutableMapOf<String,Set<String>>()
        val setsOfThree = mutableSetOf<Set<String>>()
        //Generate the network map as well as all 3 connected nodes
        adjacentPCs.keys.forEach {
            val connectedNodes = adjacentPCs[it]
            if( connectedNodes!= null) {
               //Part 2 for algo
                graphOfConnectedPCs.putIfAbsent(it, connectedNodes.toSet())
                if (connectedNodes.size > 2) {
                    val connectedNodesList = connectedNodes.toList()
                    for(i in 0 until connectedNodesList.size -1){
                        for(j in i+1 until connectedNodesList.size){
                            setsOfThree.add(setOf(it,connectedNodesList[i],connectedNodesList[j]))
                        }
                    }
                }
            }
        }
        //Part One all sets of 3
        var numberOfTConnected = mutableSetOf<Set<String>>()
        //Go through and get a list of the threes that are all connected
        setsOfThree.forEach {
            if(areAllConnected(it.toList(),pairs)) numberOfTConnected.add(it)
        }
        var numberOfTConnectedThree = numberOfTConnected.filter { set ->  pcsWithT.any { tPC -> set.contains(tPC)}}.size
        println("Part 1: Number of 3 connected PCs with a t computer: $numberOfTConnectedThree")
        //Part 2 Bron-Kerbosch to find all the closed loops. Differs from part 1 which only looks for 3 connected
        val largest = bronKerbosch(graphOfConnectedPCs, mutableSetOf(), graphOfConnectedPCs.keys.toMutableSet(), mutableSetOf()).maxBy { it.size }.sortedBy{ it }
        println("Part 2: The order of the list of the largest computers that all connect to each other :")
        println(largest)
        println()
    }

    fun areAllConnected(pcs: List<String>, pairs: List<Pair<String,String>>): Boolean {
        //If the PC doesn't have any connected
        if (pcs.size < 2) return true
        //Go through all fo them and make sure a pair of the two points connect
        for (i in 0 until pcs.size - 1) {
            for (j in i + 1 until pcs.size) {
                if (!(pairs.contains(Pair(pcs[i], pcs[j])) || pairs.contains(Pair(pcs[j], pcs[i])))) {
                    return false
                }
            }
        }
        return true
    }


    //Implementation of Bron-Kerbosch Algorithm to find all the cliques (closed loops of connected PCs)
    fun bronKerbosch(networkMap: Map<String, Set<String>>, r: MutableSet<String>, p: MutableSet<String>, x: MutableSet<String>): List<Set<String>> {
        val result = mutableListOf<Set<String>>()
        if (p.isEmpty() && x.isEmpty()) {
            result.add(r.toSet())
            return result
        }

        for (v in p.toSet()) {
            val newR = r.toMutableSet()
            newR.add(v)
            val newP = p.filter { networkMap[v]?.contains(it) ?: false }.toMutableSet()
            val newX = x.filter { networkMap[v]?.contains(it) ?: false }.toMutableSet()
            result.addAll(bronKerbosch(networkMap, newR, newP, newX))
            p.remove(v)
            x.add(v)
        }
        return result
    }


}
