package app.itmaster.mobile.superweather.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.io.IOException;

import app.itmaster.mobile.superweather.R;
import app.itmaster.mobile.superweather.WeatherAPI;

public class NewCityActivity extends AppCompatActivity {

    final static String KEY_PREFS_LAST_CITY = "lastcity";
    final static String PREFS_CACHE_NAME = "citycache";

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Ciclo de Vida", "onCreate");
        setContentView(R.layout.activity_main);

        this.setTitle("Get Weather");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences(PREFS_CACHE_NAME, Context.MODE_PRIVATE);
        String lastCity = prefs.getString(KEY_PREFS_LAST_CITY, "");
        Log.d("Cache", lastCity);
        EditText editCityName = findViewById(R.id.editCityName);
        editCityName.setText(lastCity);

        Button btnLocateMe = findViewById(R.id.btn_locate_me);
        btnLocateMe.setOnClickListener(view -> {
            acquireLocation();
        });

        Button btnGetWeather = findViewById(R.id.btnGetWeather);
        btnGetWeather.setOnClickListener(view -> {
            String cityName = editCityName.getText().toString();

            // Guardar la ciudad para caché futuro
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_PREFS_LAST_CITY, cityName);
            editor.apply(); // graba los cambios en simultáneo con el main thread

            updateOutputMessage("⏳");
            WeatherAPI.getWeatherInfo(this, cityName, (dataIsAvailable, info) -> {
                if (dataIsAvailable) {
                    updateOutputMessage(String.valueOf(info.getTemperature()) + " ⁰C");
                } else {
                    updateOutputMessage("Error :(");
                }
            });
        });

        registerActivityForPermissionResult();

    }
    ActivityResultLauncher<String[]> locationPermissionRequest;
    void registerActivityForPermissionResult() {
        // Recibe la respuesta del permiso
        locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                    // Código que se ejecuta cuando el SO responde con el resultado del diálogo de
                    // permiso
                    Boolean coarseLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION,false);
                    if (coarseLocationGranted != null && coarseLocationGranted) {
                        // Only approximate location access granted.
                        Log.d("Permission", "Permiso otorgado");

                    } else {
                        // No location access granted.
                        Log.d("Permission", "Permiso denegado");
                    }
                }
                );
    }

    private void acquireLocation() {
        // Pide el permiso
        locationPermissionRequest.launch(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void updateOutputMessage(String finalOutput) {
        TextView lblOutput = findViewById(R.id.lblOutput);
        Log.d("MainActivity", finalOutput);
        lblOutput.setText(finalOutput);
    }

    void puedeFallar() throws IOException {
        Double precio = null;

    }

    void test() throws JSONException {
//        puedeFallar();

        double random = Math.random();
//        TextView lblCityName = findViewById(R.id.lblCityName);
//        lblCityName.setText(lblCityName.getText() + String.valueOf(random));

        Button btnGetWeather = findViewById(R.id.btnGetWeather);
        btnGetWeather.setOnClickListener(v -> {
//            Toast va al Sistema Operativo
            Toast.makeText(this, "Tocaste el botón", Toast.LENGTH_LONG).show();

//            Snackbar es interno de mi app y es parte de Material
            Snackbar.make(this,findViewById(R.id.layoutRoot),
                            "Tocaste el botón", Snackbar.LENGTH_LONG)
                    .setAction("Aceptar",v1 -> {})
                    .show();

//            Alerta es parte de nuestra app pero modal
//            new AlertDialog.Builder(this)
//                    .setMessage("Tocaste el botón")
//                    .setPositiveButton("Aceptar", null)
//                    .create()
//                    .show();

            Log.d("MainActivity", "Tocaste el botón");
        });
    }

    @Override
    protected void onPause() {
        Log.d("Ciclo de Vida", "onPause");

        super.onPause();
    }
}