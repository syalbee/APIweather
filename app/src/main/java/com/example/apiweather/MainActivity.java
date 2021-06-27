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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apiweather.api.ApiService;
import com.example.apiweather.model.ModelCuaca;
import com.example.apiweather.model.ModelWaktu;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private final String TAG = "MainActivity";
    private final String APIKEY = "7f8e3b7e1c4ec0e9293195102356009e";
    private final String LANG = "en";
    private final String ImagaeURL = "https://openweathermap.org/img/wn/";

    private List<ModelCuaca.weather> results = new ArrayList<>();


    private LocationManager locationManager;
    ModelWaktu getDate = new ModelWaktu();
    TextView tvWaktu,tvLokasi,tvMain,tvDescription,tvSuhu,tvHumi;
    ImageView ivIcon;
    ProgressBar pbLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWaktu = findViewById(R.id.tvWaktu);
        tvLokasi = findViewById(R.id.tvLokasi);
        tvMain = findViewById(R.id.tvMain);
        tvDescription = findViewById(R.id.tvDescription);
        tvSuhu = findViewById(R.id.tvSuhu);
        tvHumi = findViewById(R.id.tvHumi);
        ivIcon = findViewById(R.id.ivIcon);
        pbLoad = findViewById(R.id.pbLoad);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);

        }
        tvWaktu.setText(getDate.getDateNow("E, dd MMMM"));
        showLoading(true);
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
                            showLoading(false);
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
        tvDescription.setText(res.getDescription());
        tvMain.setText(res.getMain());
        Glide.with(this).load(ImagaeURL + res.getIcon() + "@4x.png").into(ivIcon);
    }

    private void _mains(ModelCuaca.Main mlcuaca){

        double suhu = Math.round(mlcuaca.getTemp() - 273.15);
        tvSuhu.setText(String.valueOf(suhu) + "°c");
        tvHumi.setText(String.valueOf(mlcuaca.getHumidity()) + " %");
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
            tvLokasi.setText(kecamatan);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showLoading(Boolean loading){
        if (loading) {
            pbLoad.setVisibility(View.VISIBLE);
        } else {
            pbLoad.setVisibility(View.GONE);
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