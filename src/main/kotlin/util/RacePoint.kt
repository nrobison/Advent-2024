package advent2024.util



data class RacePoint(val char: Char) {
    val type = when (char) {
        '#' -> MapMarker.WALL
        'S' -> MapMarker.START
        'E' -> MapMarker.END
        '.' -> MapMarker.OPEN
        else -> throw IllegalArgumentException("Invalid map character: $char") // Handle invalid characters
    }
}
enum class MapMarker {
    START,
    END,
    WALL,
    OPEN
}
