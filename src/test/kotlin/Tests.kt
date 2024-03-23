import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


data class Bambi(val haha: Int = 3, val bread: Bread = Breads.BROODJE)

interface Bread {
    val id: String
}
enum class Breads : Bread {
    BROODJE,
    SAUS
    ;
    override val id: String
        get() = name
}

object BreadTypeAdapter : TypeAdapter<Bread>() {
    override fun write(writer: JsonWriter, bread: Bread) {
        writer.value(bread.id)
    }

    override fun read(reader: JsonReader): Bread {
        return Breads.valueOf(reader.nextString())
    }
}

fun main() {

    val b = Bambi(123, Breads.BROODJE)

    val gson = GsonBuilder().registerTypeAdapter(Bread::class.java, BreadTypeAdapter).create()

    val json = gson.toJson(b)
    println(json)

    val obj = gson.fromJson(json, Bambi::class.java)
    println(obj)
}