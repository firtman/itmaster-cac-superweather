package app.itmaster.mobile.superweather.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.io.IOException;

import app.itmaster.mobile.superweather.R;
import app.itmaster.mobile.superweather.WeatherAPI;

public class NewCityActivity extends AppCompatActivity {

    final static String KEY_PREFS_LAST_CITY = "lastcity";
    final static String PREFS_CACHE_NAME = "citycache";

    private FusedLocationProviderClient locationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

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

        // Configura la Toolbar
        this.setTitle("Get Weather");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Lee el cache de Shared Preferences
        SharedPreferences prefs = getSharedPreferences(PREFS_CACHE_NAME, Context.MODE_PRIVATE);
        String lastCity = prefs.getString(KEY_PREFS_LAST_CITY, "");
        EditText editCityName = findViewById(R.id.editCityName);
        editCityName.setText(lastCity);

        // Configura listeners de los botones
        Button btnLocateMe = findViewById(R.id.btn_locate_me);
        btnLocateMe.setOnClickListener(view -> {
            startLocationRequest();
        });
        Button btnGetWeather = findViewById(R.id.btnGetWeather);
        btnGetWeather.setOnClickListener(view -> {
            getWeatherButtonClick(prefs, editCityName);
        });

        // Configuramos objetos necesarios para location
        registerActivityForPermissionResult();
        locationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void getWeatherButtonClick(SharedPreferences prefs, EditText editCityName) {
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
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                                Log.d("Permission", "Permiso otorgado");
                                acquireLocation();
                            } else {
                                // No location access granted.
                                Log.d("Permission", "Permiso denegado");
                            }
                        }
                );
    }

    @SuppressLint("MissingPermission")
    private void acquireLocation() {
        // Tenemos el permiso, vamos a pedir la ubicación
        // 1) Android SDK: Location Provider
        //   - Todos los teléfonos: GPS_PROVIDER
        //   - Sólo en equipos con GMS: NETWORK_PROVIDER
        // 2) Fused Location Provider (librería de Google) <-- USAMOS ESTE

        // Obtener una ubicación nueva
//        registerForLocationUpdates(); // usamos la cacheada por ahora

        // Obtener la última ubicación cacheada
        this.locationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location!=null) {
                        Log.d("Location", "Latitud: " + String.valueOf(location.getLatitude()));
                        Log.d("Location", "Longitud: " + String.valueOf(location.getLongitude()));
                        updateOutputMessage("🕸️");
                        WeatherAPI.getWeatherInfo(this, location, (dataIsAvailable, info) -> {
                            if (dataIsAvailable) {
                                updateOutputMessage(String.valueOf(info.getTemperature()) + " ⁰C\n"
                                + info.getCityName() + " " + info.getCountryCode());
                            } else {
                                updateOutputMessage("Error :(");
                            }
                        });
                    } else {
                        Log.d("Location", "Location is null");
                    }
                })
                .addOnSuccessListener(location -> {

                })
                .addOnFailureListener(e -> {

                });

    }

    private void registerForLocationUpdates() {
        locationRequest = new LocationRequest.Builder(300).build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Log.d("Location", "Latitud: " + String.valueOf(location.getLatitude()));
                        Log.d("Location", "Longitud: " + String.valueOf(location.getLongitude()));
                    }
                }
            }
        };
        locationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    protected void onStop() {
        locationClient.removeLocationUpdates(locationCallback);

        super.onStop();
    }

    private void startLocationRequest() {
        // Pide el permiso
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION
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