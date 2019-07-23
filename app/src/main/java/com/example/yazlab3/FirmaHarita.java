package com.example.yazlab3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FirmaHarita extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    LocationManager locatiin;
    LocationListener listeener;
    String isim;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma_harita);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle extras = getIntent().getExtras();
        isim=extras.getString("isimler");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(this);
        // Add a marker in Sydney and move the camera

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0){
            if(requestCode==1){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    locatiin.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,50,listeener);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        String adres="";

        try {
            List<Address> adressList=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if(adressList!=null && adressList.size()>0){
                if(adressList.get(0).getThoroughfare()!=null){
                    adres+=adressList.get(0).getThoroughfare();
                    if(adressList.get(0).getSubThoroughfare()!=null){
                        adres+=adressList.get(0).getSubThoroughfare();
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Firma").child(isim);
                        dR.child("Lokasyon").setValue(adressList.get(0).getLatitude()+" "+adressList.get(0).getLongitude());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(adres.matches("")){
            adres="Adres Bulunamadı";
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title(adres));
    }
}
