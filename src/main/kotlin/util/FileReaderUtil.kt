package advent2024.util

class FileReaderUtil {

    fun readFileAsText(fileName: String):String {
        val inputFile = object {}.javaClass.getResourceAsStream("/$fileName")?.bufferedReader()?.readLines()
        return inputFile.toString()
    }

    fun readFileAsLines (fileName: String) : List<String>? {
        return object {}.javaClass.getResourceAsStream("/$fileName")?.bufferedReader()?.readLines()
    }

    fun convertLineToListOfNumbers(lineOfText: String): List<Int>  {
        return lineOfText.split(Regex("\\D+")) // Split by non-digit characters
            .filter { it.isNotEmpty() } // Remove empty strings
            .map { it.toInt() } // Convert to integers
    }

    fun convertLineToListOfLongs(lineOfText: String): List<Long>  {
        return lineOfText.split(Regex("\\D+")) // Split by non-digit characters
            .filter { it.isNotEmpty() } // Remove empty strings
            .map { it.toLong() } // Convert to integers
    }


    fun convertLinesToArrayOfChars(lines: List<String>) : Array<CharArray> {
        return lines.map { it.toCharArray() }.toTypedArray()
    }

    fun convertLineToPairOfNumbers(lineOfText: String): Pair<Int,Int>  {
      val split =  lineOfText.split(Regex( "\\|")).filter { it.isNotEmpty() }
      return Pair(split[0].toInt(), split[1].toInt())
    }

    fun convertLineOfTextToInts(lineOfText: String): IntArray {
        return lineOfText.filterNot { it == '[' || it == ']' }.map { it.digitToInt() }.toIntArray()
    }

    fun readLineIntoTwoPairsOfInts(lineOfText: String) : Pair<Point,Point>?{
        val parts = lineOfText.split(" ")
        if (parts.size != 2) return null

        val aStr = parts[0].substring(2) // Remove "{char}="
        val bStr = parts[1].substring(2) // Remove "{char}="

        try {
            val aCoords = aStr.split(",").map { it.toInt() }
            val bCoords = bStr.split(",").map { it.toInt() }

            if (aCoords.size != 2 || bCoords.size != 2) return null

            return Pair(Point(aCoords[0], aCoords[1]), Point(bCoords[0], bCoords[1]))
        } catch (e: NumberFormatException) {
            return null
        }
    }

}