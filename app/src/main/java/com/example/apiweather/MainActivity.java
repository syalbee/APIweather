package com.example.apiweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apiweather.api.ApiService;
import com.example.apiweather.model.ModelCuaca;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private final String TAG = "MainActivity";
    private final String APIKEY = "7f8e3b7e1c4ec0e9293195102356009e";
    private final String LANG = "id";
    private final String ImagaeURL = "https://openweathermap.org/img/wn/10d@4x.png";

    private List<ModelCuaca.weather> results = new ArrayList<>();


    private LocationManager locationManager;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tvCek);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);

        }
        getLocation();
//        onLocationChanged();

    }

    private void getApi(String lat, String lon){
        ApiService.endpoint().getData(lat, lon, APIKEY, LANG)
                .enqueue(new Callback<ModelCuaca>() {
                    @Override
                    public void onResponse(Call<ModelCuaca> call, Response<ModelCuaca> response) {
                        Log.d(TAG, response.toString());
                        if(response.isSuccessful()){
                            ArrayList<ModelCuaca.weather> results = response.body().getWeather();
                            ModelCuaca.Main mlcuaca = response.body().getMain();

                            _weather(results);
                            _mains(mlcuaca);
                            Log.d(TAG, mlcuaca.toString());

                        }
                    }

                    @Override
                    public void onFailure(Call<ModelCuaca> call, Throwable t) {

                    }
                });
    }

    private void _weather(List<ModelCuaca.weather> modelCuacas){
        ModelCuaca.weather res = modelCuacas.get(0);
        textView.setText("Cek : " + res.getDescription());
    }

    private void _mains(ModelCuaca.Main mlcuaca){


    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, (LocationListener) MainActivity.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, ""+location.getLatitude()+"/"+location.getLongitude(), Toast.LENGTH_SHORT).show();
        String lat = String.valueOf(location.getLatitude());
        String lon = String.valueOf(location.getLongitude());
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            String kecamatan = addresses.get(0).getLocality();
            getApi(lat, lon);
            textView.setText(kecamatan);
//            textView_location.setText(address);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Toast.makeText(this, ""+ provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}