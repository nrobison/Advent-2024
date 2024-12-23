package advent2024.Day22

import advent2024.util.FileReaderUtil
import kotlin.time.measureTime

class Day22Puzzle {

    fun solve(){
        val input =  FileReaderUtil().readFileAsLines("Day22.txt")!!.map { it.toLong() }
        val monkeysPriceOrders = mutableMapOf<List<Long>,Long>()
        var totalSecrets = 0L
        for(monkeyBuyer in input){
            //First generated number is the first input
            var generatedNumber = monkeyBuyer
            val generatedSecrets = mutableListOf<Long>()
            val prices = mutableListOf<Long>()
            repeat(2000){
                val newSecret = generateNextSecretNumber(generatedNumber)
                generatedSecrets.add(newSecret)
                //Just need to get the last digit
                prices.add(newSecret % 10)
                //set for next number
                generatedNumber = newSecret
            }

            val priceChanges = mutableListOf<Long>()
            //Possible zip?
            for(next in 0 until generatedSecrets.size-1){
                priceChanges.add(prices[next+1]-prices[next])
            }
            //make sure we only add the first
            val knownSequences = HashSet<List<Long>>()
            //Windowed take 4 elements so we can get the sequence and then for each index do something
            priceChanges.windowed(4).forEachIndexed { index, sequence ->
                //Add either succeeds (it's not there) or fails (it was already there)
                if(knownSequences.add(sequence)){
                    //update our map with the new sell value to keep running totals of the sequences
                    monkeysPriceOrders[sequence] = monkeysPriceOrders.getOrDefault(sequence,0) + prices[index+4]
                }
            }
            totalSecrets += generatedSecrets.last()
        }

        println("Part 1: Total of all secrets: $totalSecrets")
        //Use the nice maxOf to get the largest sequence total in our list
        println("Part 2: Max sell value is ${monkeysPriceOrders.maxOf { it.value }}")
    }


    private fun generateNextSecretNumber(secretNumber: Long): Long {
        var newSecret = secretNumber * 64
        newSecret = mixAndPrune(secretNumber, newSecret)
        newSecret = mixAndPrune(newSecret, newSecret.floorDiv(32))
        return mixAndPrune(newSecret, newSecret * 2048)
    }

    private fun mixAndPrune(startingNumber: Long, modifiedNumber: Long) : Long {
        return (startingNumber xor modifiedNumber) % 16777216
    }
}