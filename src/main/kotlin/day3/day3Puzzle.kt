package advent2024.day3

import advent2024.util.FileReaderUtil

class day3Puzzle {

    fun solvePart1(){
        val input = FileReaderUtil().readFileAsText("Day3.txt")
        val regex = Regex("(mul\\(\\d{1,3},\\d{1,3}\\))|(do(?:n't)?\\(\\))")
        val matches = regex.findAll(input)
        var total = 0
        var enabled = true
        for (match in matches){
            if(match.value == "do()"){
                println("We hit do() with ${match.value}")
                enabled = true
            }
            else if(match.value == "don't()") {
                println("We hit don't() with ${match.value}")
                enabled = false
            }
            else {
                val values = match.value.substringAfter("(").substringBefore(")").split(",")
                val num1 = values[0].toInt()
                val num2 = values[1].toInt()
                val product = num1 * num2
                println("$num1 * $num2 = $product")
                if(enabled) total += product
            }
        }
        println(total)
    }
}