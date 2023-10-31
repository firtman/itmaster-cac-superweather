package app.itmaster.mobile.superweather.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.itmaster.mobile.superweather.R;
import app.itmaster.mobile.superweather.WeatherAPI;
import app.itmaster.mobile.superweather.model.City;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder> {

    List<City> cities = null;
    Activity context = null;

    int contador = 0;

    public CitiesAdapter(List<City> cities, Activity context) {
        this.cities = cities;
        this.context = context;
    }



    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        contador++;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent,
                false);

        Log.d("Adapter", "Creando el VH #" + String.valueOf(contador));

        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = this.cities.get(position);

        // Limpiar el View Holder
        holder.itemView.setBackgroundColor(0);

        Log.d("Adapter", "Enlazando datos en un VH: " + position);

        TextView lblItemCityName = holder.itemView.findViewById(R.id.lblItemCityName);
        TextView lblItemCityTemperature = holder.itemView.findViewById(R.id.lblItemCityTemperature);
        lblItemCityName.setText(city.getCountryEmoji() + " " + city.getName());
        lblItemCityTemperature.setText("");

        WeatherAPI.getWeatherInfo(this.context, city.getName(), (dataIsAvailable, info) -> {
            if (dataIsAvailable) {
                lblItemCityTemperature.setText(String.valueOf(info.getTemperature()) + "⁰ C");
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(String cityName) {

        }
    }
}
