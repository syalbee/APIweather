package com.example.apiweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.apiweather.api.ApiService;
import com.example.apiweather.model.ModelCuaca;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private List<ModelCuaca.weather> results = new ArrayList<>();
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tvCek);
        getApi();
    }

    private void getApi(){
        String lat, lon, apikey, lang;
        lat = "-6.54524";
        lon = "107.44515";
        apikey = "7f8e3b7e1c4ec0e9293195102356009e";
        lang = "id";
        ApiService.endpoint().getData(lat, lon, apikey, lang)
                .enqueue(new Callback<ModelCuaca>() {
                    @Override
                    public void onResponse(Call<ModelCuaca> call, Response<ModelCuaca> response) {
                        Log.d(TAG, response.toString());
                        if(response.isSuccessful()){
                            ArrayList<ModelCuaca.weather> results = response.body().getWeather();
                            cetak(results);
                            Log.d(TAG, results.toString());


                        }
                    }

                    @Override
                    public void onFailure(Call<ModelCuaca> call, Throwable t) {

                    }
                });
    }

    private void cetak(List<ModelCuaca.weather> modelCuacas){
        ModelCuaca.weather res = modelCuacas.get(0);
        textView.setText("Cek : " + res.getDescription());
    }
}