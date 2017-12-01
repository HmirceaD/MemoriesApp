package com.example.maruta.memories;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Location crrLoc;
    private double[] coord;


    //Ui
    private GoogleMap mMap;
    private Button btnToAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        coord = new double[3];

        crrLoc = new Location(LocationManager.GPS_PROVIDER);

        btnToAdd = findViewById(R.id.btnAdd);

        Intent it = new Intent(this, MainActivity.class);

        btnToAdd.setOnClickListener((View event) ->{

            if(crrLoc.getLatitude() != 0.0 && crrLoc.getLongitude() != 0.0){

                coord[0] = crrLoc.getLatitude();
                coord[1] = crrLoc.getLongitude();

                it.putExtra("Coordinates", coord);
                startActivity(it);

            } else {

                Toast.makeText(this, "Select A Location pls thx", Toast.LENGTH_SHORT).show();

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

        if(getIntent().getStringExtra("Address")!= null){

            String add = getIntent().getStringExtra("Address");

            LatLng startLoc = getLocFromAddress(add);

            mMap.addMarker(new MarkerOptions().position(startLoc).title("Your memory"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(startLoc));
        }

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Yo mama's marker"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                crrLoc.setLatitude(latLng.latitude);
                crrLoc.setLongitude(latLng.longitude);
            }
        });
    }

    private LatLng getLocFromAddress(String add) {

        LatLng temp;

        Geocoder geoTemp = new Geocoder(this);
        List<Address> tempAdd;

        try{

            tempAdd = geoTemp.getFromLocationName(add, 5);

            temp = new LatLng(tempAdd.get(0).getLatitude(), tempAdd.get(0).getLongitude());

            return temp;

        }catch (IOException ex){

            ex.printStackTrace();
            return null;
        }

    }
}
