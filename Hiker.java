package com.example.smeet.hikersapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager =(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Toast.makeText(MainActivity.this, location.toString(), Toast.LENGTH_LONG).show();
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,20,locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
           if(lastLocation!=null ){
                updateLocation(lastLocation);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }

    public void startListening(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 20, locationListener);
        }
    }
   public void updateLocation(Location location) {
       TextView lat=findViewById(R.id.Lat);
       TextView longi=findViewById(R.id.Longi);
       TextView acc=findViewById(R.id.Acc);
       TextView addr=findViewById(R.id.Addr);

        lat.setText("Latitude: "+ Double.toString(location.getLatitude()));
        longi.setText("Longitude: "+ Double.toString(location.getLongitude()));
        acc.setText("Accuracy: "+ Float.toString(location.getAccuracy()));

       String address="No Address Found!";
       Geocoder geocoder=new Geocoder(this, Locale.getDefault());
       try {
           List<Address> la = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
           if(la!=null && la.size()>0){
               address="Address:\n";
           }
           if(la.get(0).getThoroughfare()!=null)
           {
               address+=la.get(0).getThoroughfare()+" \n";
           }
           if(la.get(0).getLocality()!=null)
           {
               address+=la.get(0).getLocality()+" \n";
           }if(la.get(0).getPostalCode()!=null)
           {
               address+=la.get(0).getPostalCode()+" \n";
           }if(la.get(0).getAdminArea()!=null)
           {
               address+=la.get(0).getAdminArea()+" ";
           }
       }catch(Exception e){
           e.printStackTrace();
       }

       addr.setText(address);

    }
}
