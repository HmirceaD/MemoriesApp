package com.example.maruta.memories;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private double coord[];
    static private List<String> list1 = new ArrayList<>();
    static private ArrayAdapter listAdp;

    //Ui
    private Button btnToMap;
    private ListView memoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();

        btnToMap.setOnClickListener((View event) -> {

            Intent it = new Intent(this, MapsActivity.class);
            startActivity(it);

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getDoubleArrayExtra("Coordinates") != null){

            coord = getIntent().getDoubleArrayExtra("Coordinates");
            addMemory(coord);

        }
    }

    private void addMemory(double[] coord) {

        Geocoder geoCode = new Geocoder(this);
        List<Address> addresses;

        listAdp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list1);

        try {

            addresses = geoCode.getFromLocation(coord[0], coord[1], 1);

            String ad = addresses.get(0).getAddressLine(0);

            list1.add(ad);

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    private void initUi() {

        btnToMap = findViewById(R.id.btnToMap);
        coord = new double[3];

        listAdp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list1);
        memoriesList = findViewById(R.id.memoriesList);
        memoriesList.setAdapter(listAdp);

        memoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent it = new Intent(MainActivity.this, MapsActivity.class);
                it.putExtra("Address", listAdp.getItem(position).toString());
                startActivity(it);

            }
        });

    }
}
