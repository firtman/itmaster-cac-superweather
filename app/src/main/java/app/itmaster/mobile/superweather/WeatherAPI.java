package app.itmaster.mobile.superweather;


import android.app.Activity;
import android.content.Context;
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

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
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
                        context.runOnUiThread(() -> callback.onWeatherInfoReady(true, info));

                    } else {
                        output = "Error getting temperature";
                        sendError();
                    }
                } catch (JSONException e) {
                    output = "Error reading data";
                    sendError();

                } catch (Exception e) {
                    output = "Internal Error";
                    sendError();

                    //TODO: Mandar info a Crashlytics
                } finally {
                }

            }

            private void sendError() {
                context.runOnUiThread(() -> callback.onWeatherInfoReady(false, null));
            }
        });
    }

}
