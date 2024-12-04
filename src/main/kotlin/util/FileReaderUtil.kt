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
    
}