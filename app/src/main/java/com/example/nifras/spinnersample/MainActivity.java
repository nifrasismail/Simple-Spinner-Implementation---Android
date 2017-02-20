package com.example.nifras.spinnersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    JSONArray jsonTemp;
    private ArrayList<String> list;
    private Spinner spinner;
    private ArrayAdapter<String> adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);


        list = new ArrayList<String>();
        adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adp);
        LoadCountries();
        spinner.setOnItemSelectedListener(this);
    }

    private void LoadCountries() {
        OkHttpClient client = new OkHttpClient();

        okhttp3.Request request = new Request.Builder()
                .url("https://demo8816124.mockable.io/spinner_sample")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException ex) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();

                    JSONArray jsonArray = new JSONArray(responseString);
                    jsonTemp = new JSONArray(responseString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        final CountryBean countryBean = gson.fromJson(jsonObject.toString(), CountryBean.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI(countryBean);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void updateUI(CountryBean countryBean) {
        if (countryBean != null) {
            list.add(countryBean.getCode());
            adp.notifyDataSetChanged();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        JSONObject jsonObject = null;
        try {
            jsonObject = jsonTemp.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        final CountryBean countryBean = gson.fromJson(jsonObject.toString(), CountryBean.class);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadCities(countryBean);
            }
        });

    }

    private void LoadCities(CountryBean countryBean) {
        Toast.makeText(getApplicationContext(), countryBean.getName(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
