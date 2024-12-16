package advent2024.util

data class Point(var x: Int, var y: Int) : Cloneable{

    public override fun clone(): Point {
        return super.clone() as Point
    }

     fun plus (pointToAdd : Point){
        x += pointToAdd.x
        y += pointToAdd.y
     }

    fun sub (pointToSub: Point){
        x -= pointToSub.x
        y -= pointToSub.y
    }

    override fun equals(other: Any?): Boolean {
        if(other is Point){
            return ((other.x == x ) && (other.y == y))
        }
        return false
    }
}