package com.example.yazlab3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Harita extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    LocationManager locatiin;
    LocationListener listeener;
    String isim,tur,firmaName;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    ArrayList<Firma> firmalar = new ArrayList<>();
    ArrayList<Firma> kucukler = new ArrayList<>();
    ArrayList<String> yeni = new ArrayList<>();
    ArrayList<String>gonderim = new ArrayList<>();
    Double mesafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harita);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle extras = getIntent().getExtras();
        isim=extras.getString("isimler");
        tur=extras.getString("kategori");
        firmaName=extras.getString("firmaisim");
        System.out.println("TURUMUZ  : "+tur);
        System.out.println("FİRMA ADIMIZ  : "+firmaName);
        DatabaseReference dbRef = firebaseDatabase.getReference();
        dbRef.child("Firma").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp :dataSnapshot.getChildren()){
                    yeni.add(dsp.getKey().toString());
                }
                for (int m=0;m<yeni.size();m++){
                    Firma sirket = new Firma();
                    sirket.firmaAdi=dataSnapshot.child(yeni.get(m)).child("FirmaAdi").getValue(String.class);
                    sirket.firmaId=dataSnapshot.child(yeni.get(m)).child("FirmaId").getValue(String.class);
                    sirket.firmaTuru=dataSnapshot.child(yeni.get(m)).child("Kategori").getValue(String.class);
                    String latlot =dataSnapshot.child(yeni.get(m)).child("Lokasyon").getValue(String.class);
                    String[] countryLines = latlot.split(" ");
                    sirket.lat=Double.parseDouble(countryLines[0]);
                    sirket.lot=Double.parseDouble(countryLines[1]);
                    firmalar.add(sirket);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        locatiin=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        listeener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();
                LatLng userLocation=new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Konumunuz"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,20));
                Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
                try{
                    List<Address> adresList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if(adresList!=null && adresList.size()>0){
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(isim);
                        dR.child("Lokasyon").setValue(adresList.get(0).getLatitude()+" "+adresList.get(0).getLongitude());
                        for (int i =0;i<firmalar.size();i++){
                            System.out.println("VERİTABININDAKİ : "+firmalar.get(i).firmaTuru);
                            if(tur.equalsIgnoreCase("SECİNİZ")&&firmaName.equalsIgnoreCase("")){
                                mesafe=distance(adresList.get(0).getLatitude(),adresList.get(0).getLongitude(),firmalar.get(i).lat,firmalar.get(i).lot);
                                if(mesafe<=1.0){
                                    kucukler.add(firmalar.get(i));
                                }
                            }
                            if (tur.equalsIgnoreCase("SECİNİZ")&& firmaName.equalsIgnoreCase(firmalar.get(i).firmaAdi)){
                                mesafe=distance(adresList.get(0).getLatitude(),adresList.get(0).getLongitude(),firmalar.get(i).lat,firmalar.get(i).lot);
                                if(mesafe<=1.0){
                                    kucukler.add(firmalar.get(i));
                                }
                            }
                            if (tur.equalsIgnoreCase(firmalar.get(i).firmaTuru)&&firmaName.equalsIgnoreCase("")){
                                mesafe=distance(adresList.get(0).getLatitude(),adresList.get(0).getLongitude(),firmalar.get(i).lat,firmalar.get(i).lot);
                                if(mesafe<=1.0){
                                    kucukler.add(firmalar.get(i));
                                }
                            }
                            if (tur.equalsIgnoreCase(firmalar.get(i).firmaTuru)&&firmaName.equalsIgnoreCase(firmalar.get(i).firmaAdi)){
                                mesafe=distance(adresList.get(0).getLatitude(),adresList.get(0).getLongitude(),firmalar.get(i).lat,firmalar.get(i).lot);
                                if(mesafe<=1.0){
                                    kucukler.add(firmalar.get(i));
                                }
                            }

                        }
                        for (int i=0;i<kucukler.size();i++){
                            LatLng konum = new LatLng(kucukler.get(i).lat,kucukler.get(i).lot);
                            mMap.addMarker(new MarkerOptions().position(konum).title(kucukler.get(i).firmaAdi));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(konum));

                        }

                    }

                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(Build.VERSION.SDK_INT>=23){
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                locatiin.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,50,listeener);
                Location lastLocation=locatiin.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastLocation!=null){
                    LatLng userLastLocation=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().title("Konumunuz").position(userLastLocation));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,20));
                }
            }
            else{
                locatiin.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,50,listeener);
                Location lastLocation=locatiin.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastLocation!=null){
                    LatLng userLastLocation=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().title("Konumunuz").position(userLastLocation));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,20));
                }


            }
        }

        mMap.setOnMapClickListener(this);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0){
            if(requestCode==1){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    locatiin.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,50,listeener);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
        String adres="";

        try {
            List<Address> adressList=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if(adressList!=null && adressList.size()>0){
                if(adressList.get(0).getThoroughfare()!=null){
                    adres+=adressList.get(0).getThoroughfare();
                    if(adressList.get(0).getSubThoroughfare()!=null){
                        adres+=adressList.get(0).getSubThoroughfare();
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("users").child(isim);
                        dR.child("Lokasyon").setValue(adressList.get(0).getLatitude()+" "+adressList.get(0).getLongitude());
                        for (int i =0;i<firmalar.size();i++){
                            System.out.println("VERİTABININDAKİ : "+firmalar.get(i).firmaTuru);
                            if(tur.equalsIgnoreCase("SECİNİZ")&&firmaName.equalsIgnoreCase("")){
                                mesafe=distance(adressList.get(0).getLatitude(),adressList.get(0).getLongitude(),firmalar.get(i).lat,firmalar.get(i).lot);
                                if(mesafe<=1.0){
                                    kucukler.add(firmalar.get(i));
                                }
                            }
                            if (tur.equalsIgnoreCase("SECİNİZ")&& firmaName.equalsIgnoreCase(firmalar.get(i).firmaAdi)){
                                mesafe=distance(adressList.get(0).getLatitude(),adressList.get(0).getLongitude(),firmalar.get(i).lat,firmalar.get(i).lot);
                                if(mesafe<=1.0){
                                    kucukler.add(firmalar.get(i));
                                }
                            }
                            if (tur.equalsIgnoreCase(firmalar.get(i).firmaTuru)&&firmaName.equalsIgnoreCase("")){
                                mesafe=distance(adressList.get(0).getLatitude(),adressList.get(0).getLongitude(),firmalar.get(i).lat,firmalar.get(i).lot);
                                if(mesafe<=1.0){
                                    kucukler.add(firmalar.get(i));
                                }
                            }
                            if (tur.equalsIgnoreCase(firmalar.get(i).firmaTuru)&&firmaName.equalsIgnoreCase(firmalar.get(i).firmaAdi)){
                                mesafe=distance(adressList.get(0).getLatitude(),adressList.get(0).getLongitude(),firmalar.get(i).lat,firmalar.get(i).lot);
                                if(mesafe<=1.0){
                                    kucukler.add(firmalar.get(i));
                                }
                            }

                        }
                        for (int i=0;i<kucukler.size();i++){
                            LatLng konum = new LatLng(kucukler.get(i).lat,kucukler.get(i).lot);
                            mMap.addMarker(new MarkerOptions().position(konum).title(kucukler.get(i).firmaAdi));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(konum));

                        }
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
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }
    public void kampanya(View view){
        for (int i=0;i<kucukler.size();i++){
            gonderim.add(kucukler.get(i).firmaAdi);
        }
        Intent intent = new Intent(this,Kampanyalar.class);
        intent.putStringArrayListExtra("firma",gonderim);
        startActivity(intent);
    }
}
