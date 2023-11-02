package app.itmaster.mobile.superweather;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WeatherAPI {

    public interface WeatherCallback {
        void onWeatherInfoReady(boolean dataIsAvailable, WeatherInfo info);
    }

    public static void getWeatherInfo(Activity context, Location location, WeatherCallback callback) {
        String url = "https://api.openweathermap.org/data/2.5/weather?appid=85ad76cb241ddd400e9c40d1b59c5f74"
                + "&lat=" + String.valueOf(location.getLatitude())
                + "&lon=" + String.valueOf(location.getLongitude())
                + "&units=metric";

        Log.d("URL", url);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //TODO: Mostrar mensaje de error
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                processWeatherResponse(response, context, callback);
            }
        });
    }

    public static void getWeatherInfo(Activity context, String cityName, WeatherCallback callback) {
        String url = "https://api.openweathermap.org/data/2.5/weather?appid=85ad76cb241ddd400e9c40d1b59c5f74&q="
                + cityName + "&units=metric";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //TODO: Mostrar mensaje de error
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                processWeatherResponse(response, context, callback);
            }
        });
    }

    private static void processWeatherResponse(Response response, Activity context, WeatherCallback callback) throws IOException {
        String jsonString = response.body().string();
        Log.d("JSON", jsonString);

        String output = "";
        // Hilo de trabajo
        try {
            JSONObject json = new JSONObject(jsonString);
            int code = json.getInt("cod");
            if (code==200) {
                JSONObject main = json.getJSONObject("main");
                output = String.valueOf(main.getDouble("temp")) + "⁰ C";

                WeatherInfo info = new WeatherInfo();
                info.setTemperature(main.getDouble("temp"));
                info.setFeelsLike(main.getDouble("feels_like"));
                info.setHumidity(main.getDouble("humidity"));
                info.setCityName(json.getString("name"));
                info.setCountryCode(json.getJSONObject("sys").getString("country"));
                context.runOnUiThread(() -> callback.onWeatherInfoReady(true, info));

            } else {
                output = "Error getting temperature";
                sendError(context, callback, output);
            }
        } catch (JSONException e) {
            output = "Error reading data";
            sendError(context, callback, output);

        } catch (Exception e) {
            output = "Internal Error";
            sendError(context, callback, output);

            //TODO: Mandar info a Crashlytics
        } finally {
        }
    }

    private static void sendError(Activity context, WeatherCallback callback, String output) {
        Log.e("LocationError", output);
        context.runOnUiThread(() -> callback.onWeatherInfoReady(false, null));
    }

}
