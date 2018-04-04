package com.example.hp.hospitalondemand;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hp.hospitalondemand.DatabaseHelper.Viewholder;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class StomachwashMap extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    GoogleMap mgooglemap;
    Marker mLocationMarker;
    GoogleApiClient mgoogleapiClient;
    Firebase hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stomachwash_map);

        Firebase.setAndroidContext(this);
        hospital = new Firebase("https://bd-hospital.firebaseio.com/stomachwash");
        initmap();
    }
    private void initmap() {
        MapFragment mapfragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment_stomachwash);
        mapfragment.getMapAsync(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mlocationRequest = LocationRequest.create();
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationRequest.setInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleapiClient, mlocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null){
            Toast.makeText(StomachwashMap.this, "Can't get the location", Toast.LENGTH_SHORT).show();
        }else {


            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(ll);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mLocationMarker=mgooglemap.addMarker(markerOptions);
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,13);

            mgooglemap.moveCamera(update);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgooglemap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mgooglemap.setMyLocationEnabled(true);
        mgoogleapiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mgoogleapiClient.connect();

        loadMarker();
    }
    private void loadMarker() {
        hospital.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data=dataSnapshot.getChildren();
                mgooglemap.clear();
                for(DataSnapshot datasnap:data){
                    Viewholder viewholder=datasnap.getValue(Viewholder.class);
                    mgooglemap.addMarker(new MarkerOptions().position(new LatLng(viewholder.getLatitude(),
                            viewholder.getLongitude())).title(viewholder.getHospitalName()).snippet(viewholder.getDescription()));
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    LocationRequest mlocationRequest;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
