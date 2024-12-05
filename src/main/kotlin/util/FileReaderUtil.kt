package advent2024.util
import java.io.File

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

    fun convertLinesToArrayOfChars(lines: List<String>) : Array<CharArray> {
        return lines.map { it.toCharArray() }.toTypedArray()
    }
    
}