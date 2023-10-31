package app.itmaster.mobile.superweather.model

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.itmaster.mobile.superweather.R
import app.itmaster.mobile.superweather.adapters.CitiesAdapter
import java.util.Arrays

fun String.estaVacio() : Boolean {
    return this.length==0
}

class Curso {
    var nombre: String = ""

    fun sumarleIVA(precio: Double): Double {
        return precio * 1.21
    }

    fun sumarleIVAimpuestoPAIS(precio: Double, porcentajePAIS: Double): Double {
        return precio * 1.21 * (1+porcentajePAIS/100)
    }



    fun test() {
        val precioTotal = sumarleIVAimpuestoPAIS(porcentajePAIS = 30.0, precio = 40.0)

        var c = Curso()
        c.nombre = "Kotlin 101"

        c.nombre.estaVacio()

        "".estaVacio()



    }

}