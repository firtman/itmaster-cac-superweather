package app.itmaster.mobile.superweather.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.itmaster.mobile.superweather.R;
import app.itmaster.mobile.superweather.WeatherAPI;
import app.itmaster.mobile.superweather.adapters.CitiesAdapter;
import app.itmaster.mobile.superweather.model.City;

public class ListActivity extends AppCompatActivity {

    List<City> cities = null;
    RecyclerView recyclerCities;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // cada vez que el SO necesita dibujar el menú de opciones
        new MenuInflater(this).inflate(R.menu.list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.action_new_city) {
            Intent intent = new Intent(this, NewCityActivity.class);
            startActivity(intent);
        } else if (item.getItemId()==R.id.action_settings) {
            // INTENT EXPLICITO
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        cities = Arrays.asList(
                new City("Buenos Aires", "🇦🇷"),
                new City("Madrid", "🇪🇸"),
                new City("London", "🇬🇧"),
                new City("Paris", "🇫🇷"),
                new City("Doha", "🇶🇦"),
                new City("New York", "🇺🇸"),
                new City("Los Angeles", "🇺🇸"),
                new City("Tokyo", "🇯🇵"),
                new City("Beijing", "🇨🇳"),
                new City("Shanghai", "🇨🇳"),
                new City("Mumbai", "🇮🇳"),
                new City("Delhi", "🇮🇳"),
                new City("Cairo", "🇪🇬"),
                new City("Moscow", "🇷🇺"),
                new City("São Paulo", "🇧🇷"),
                new City("Rio de Janeiro", "🇧🇷"),
                new City("Mexico City", "🇲🇽"),
                new City("Lima", "🇵🇪"),
                new City("Bogotá", "🇨🇴"),
                new City("Johannesburg", "🇿🇦"),
                new City("Sydney", "🇦🇺"),
                new City("Melbourne", "🇦🇺"),
                new City("Bangkok", "🇹🇭"),
                new City("Jakarta", "🇮🇩"),
                new City("Seoul", "🇰🇷"),
                new City("Singapore", "🇸🇬"),
                new City("Manila", "🇵🇭"),
                new City("Karachi", "🇵🇰"),
                new City("Istanbul", "🇹🇷"),
                new City("Dubai", "🇦🇪"),
                new City("Rome", "🇮🇹"),
                new City("Barcelona", "🇪🇸"),
                new City("Berlin", "🇩🇪"),
                new City("Athens", "🇬🇷"),
                new City("Prague", "🇨🇿"),
                new City("Vienna", "🇦🇹"),
                new City("Budapest", "🇭🇺"),
                new City("Warsaw", "🇵🇱"),
                new City("Dublin", "🇮🇪"),
                new City("Brussels", "🇧🇪"),
                new City("Zurich", "🇨🇭"),
                new City("Stockholm", "🇸🇪"),
                new City("Oslo", "🇳🇴"),
                new City("Copenhagen", "🇩🇰"),
                new City("Amsterdam", "🇳🇱"),
                new City("Helsinki", "🇫🇮"),
                new City("Lisbon", "🇵🇹"),
                new City("Porto", "🇵🇹"),
                new City("Reykjavik", "🇮🇸"),
                new City("Hong Kong", "🇭🇰"),
                new City("Kuala Lumpur", "🇲🇾"),
                new City("Santiago", "🇨🇱"),
                new City("Caracas", "🇻🇪"),
                new City("Montevideo", "🇺🇾"),
                new City("Havana", "🇨🇺"),
                new City("Kingston", "🇯🇲"),
                new City("Brasília", "🇧🇷"),
                new City("San Salvador", "🇸🇻"),
                new City("Tegucigalpa", "🇭🇳"),
                new City("San José", "🇨🇷"),
                new City("Panama City", "🇵🇦"),
                new City("Georgetown", "🇬🇾"),
                new City("Asunción", "🇵🇾")
        );



        CitiesAdapter adapter = new CitiesAdapter(cities, this);
        recyclerCities = findViewById(R.id.recyclerCities);
        recyclerCities.setAdapter(adapter);
        recyclerCities.setLayoutManager(new LinearLayoutManager(this));

        this.setTitle("Cities");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Button btnNew = new Button(this);
//        btnNew.setText("New City");
//        btnNew.setOnClickListener(view -> {
//            Intent intent = new Intent(this, NewCityActivity.class);
//            startActivity(intent);
//        });
//        toolbar.addView(btnNew);








    }
}