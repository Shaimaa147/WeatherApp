package com.example.albadr.weatherapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CityPopActivity extends Activity {

    private static final String TAG = "CityPopActivity";

    //    http://api.openweathermap.org/data/2.5/weather?lat=0&lon=0&appid=c6e381d8c7ff98f0fee43775817cf6ad
    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String LAT = "lat=";
    private static final String LNG = "&lon=";
    private static final String APPID = "&appid=";
    private static final String WEATHER_API_KEY = "c6e381d8c7ff98f0fee43775817cf6ad";
    private static final String GET_WEATHER_ICON_URL = "http://openweathermap.org/img/w/";
    public static String LOG_TAG = "my_log";

    LatLng latLng;
    String country,name,sunrise,sunset,description,temperature,humidity,windSpeed;
    Double coordLat, coordLong;
    TextView countrySHResult, citySHResult, sunriseSHResult, sunsetSHResult, descriptionSHResult, temperatureSHResult, humiditySHResult, windSpeedSHResult;
    ImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog, pTDialog;
    ImageView closeImgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 50;

        getWindow().setAttributes(params);



        Intent intent = getIntent();

        if (intent.hasExtra("lat")&& intent.hasExtra("lng")) {
            latLng = new LatLng(Double.parseDouble(getIntent().getStringExtra("lat")), Double.parseDouble(getIntent().getStringExtra("lng")));
        } else {
            Log.d("extraa", "no extra found");
        }

        countrySHResult = (TextView) findViewById(R.id.countrySHResult);
        citySHResult = (TextView) findViewById(R.id.citySHResult);
        sunriseSHResult = (TextView) findViewById(R.id.sunriseSHResult);
        sunsetSHResult = (TextView) findViewById(R.id.sunsetSHResult);
        descriptionSHResult = (TextView) findViewById(R.id.descriptionSHResult);
        temperatureSHResult = (TextView) findViewById(R.id.temperatureSHResult);
        humiditySHResult = (TextView) findViewById(R.id.humiditySHResult);
        windSpeedSHResult = (TextView) findViewById(R.id.windSpeedSHResult);
        img = (ImageView) findViewById(R.id.img);
        closeImgButton = (ImageView) findViewById(R.id.closeImgButton);
        closeImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new ParseTask().execute();
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pTDialog = new ProgressDialog(CityPopActivity.this);
            pTDialog.setMessage("Loading text data ....");
            pTDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL(WEATHER_URL + LAT + latLng.latitude + LNG + latLng.longitude + APPID + WEATHER_API_KEY);
                Log.d("url", url.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            JSONObject dataJsonObj = null;

            try {
                dataJsonObj = new JSONObject(strJson);
                JSONObject coord = dataJsonObj.getJSONObject("coord");
                JSONObject sys = dataJsonObj.getJSONObject("sys");
                JSONObject main = dataJsonObj.getJSONObject("main");
                JSONArray weatherAr = dataJsonObj.getJSONArray("weather");
                JSONObject weather = weatherAr.getJSONObject(0);
                JSONObject wind = dataJsonObj.getJSONObject("wind");

                coordLong = coord.getDouble("lon");
                coordLat = coord.getDouble("lat");

                country = sys.getString("country");
                countrySHResult.setText(country);

                name = dataJsonObj.getString("name");
                citySHResult.setText(name);

                Integer tempRise = sys.getInt("sunrise");
                sunrise = Conversion.getTime((long) tempRise);
                sunriseSHResult.setText(sunrise);

                Integer tempSet = sys.getInt("sunset");
                sunset = Conversion.getTime((long) tempSet);
                sunsetSHResult.setText(sunset);

                description = weather.getString("description");
                descriptionSHResult.setText(description);

                String iconName = weather.getString("icon");

                Double temp = main.getDouble("temp");
                Double tempC = Conversion.convertToCelsius(temp);
                temperature = tempC.toString() + " C";
                temperatureSHResult.setText(temperature);

                humidity = main.getString("humidity");
                humidity = humidity + "%";
                humiditySHResult.setText(humidity);
                windSpeed = wind.getString("speed");
                windSpeed = windSpeed + " meter/s";
                windSpeedSHResult.setText(windSpeed);

                new LoadImage().execute(GET_WEATHER_ICON_URL + iconName + ".png");

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(CityPopActivity.this,
                        "Such City Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }

            pTDialog.dismiss();

        }

        protected class LoadImage extends AsyncTask<String, String, Bitmap> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(CityPopActivity.this);
                pDialog.setMessage("Loading Image ....");
                pDialog.show();
            }

            protected Bitmap doInBackground(String... args) {
                try {
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            protected void onPostExecute(Bitmap image) {
                if (image != null) {
                    img.setImageBitmap(image);
                    pDialog.dismiss();
                } else {
                    pDialog.dismiss();
                    Toast.makeText(CityPopActivity.this, "Image Does Not exist or Network Error",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
