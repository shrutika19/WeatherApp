package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    public void getWeather(View view){

        try {
            DownloadTask task = new DownloadTask();

            String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");

            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=d86565559f63b7af3abda954302de8b2");

            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not find weather!", Toast.LENGTH_SHORT).show();
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String>{

        URL url;
        HttpURLConnection urlConnection;
        String result = "";

        @Override
        protected String doInBackground(String...urls) {

            try{

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1){
                    char currentData = (char) data;
                    result += currentData;
                    data = reader.read();
                }

                return result;
            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not find weather!", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Ino:",weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                String message = "";

                for(int i = 0; i< arr.length(); i++){
                    JSONObject jsonObj2 = arr.getJSONObject(i);

                    String main = jsonObj2.getString("main");
                    String description = jsonObj2.getString("description");

                    if(!main.equals("") && !description.equals("")){
                        message += main + " : " +description +"\r\n";
                    }

                }

                if(!message.equals("")){
                    textView.setText(message);
                }else{
                    Toast.makeText(getApplicationContext(), "Could not find weather!", Toast.LENGTH_SHORT).show();

                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.resultTextView);

    }
}