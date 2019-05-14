package com.example.user_vs.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference pointsRef = rootRef.collection("exchange");
        HashMap<String, String> markerMap = new HashMap<String, String>();

        pointsRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot documentSnapshot : task.getResult())
                            if (documentSnapshot.exists()) {
                                Exchange exchange = documentSnapshot.toObject(Exchange.class);
                                GeoPoint geo = exchange.getGeo();
                                String name = exchange.getName();
                                double lat = geo.getLatitude();
                                double lng = geo.getLongitude();
                                LatLng latLng = new LatLng(lat, lng);
                                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                                MarkerAndExchange markerEx = new MarkerAndExchange(marker.getId(), documentSnapshot.getId());
                                db.collection("markers")
                                        .document(marker.getId())
                                        .set(markerEx);

                                markerMap.put(marker.getId(), exchange.getExchangeId());
                            }
                    }
                });
        mMap.setOnInfoWindowClickListener(marker1 -> {

            db.collection("markers")
                    .document(marker1.getId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot item = task.getResult();
                            MarkerAndExchange markerAndExchange = item.toObject(MarkerAndExchange.class);

                            DetailsExchangeFragment detailsExchangeFr = new DetailsExchangeFragment();
                            Bundle b = new Bundle();
                            b.putString("exchange_id", markerAndExchange.getExchangeId());
                            detailsExchangeFr.setArguments(b);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.map, detailsExchangeFr).addToBackStack(null).commit();
                        }
                    });
        });
    }
}
