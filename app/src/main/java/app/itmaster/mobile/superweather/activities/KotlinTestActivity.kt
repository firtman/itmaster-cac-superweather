package app.itmaster.mobile.superweather.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import app.itmaster.mobile.superweather.R
import app.itmaster.mobile.superweather.model.City
import app.itmaster.mobile.superweather.model.Curso

class KotlinTestActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_test)

        val curso = Curso();
        val city = City("Buenos Aires", "🇦🇷");
        city.countryEmoji = "💃"


        val button = findViewById<Button>(R.id.btnKotlinButton)
        val textview = findViewById<TextView>(R.id.lblKotlinTexto)

        textview.text = "Hola desde Kotlin"
        button.setOnClickListener {
            textview.text = "Ouch! me dolió"
        }

    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
    }
}