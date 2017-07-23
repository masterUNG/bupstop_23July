package com.example.faster.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransitMode;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.TransitDetail;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class Bus_details extends ActionBarActivity implements OnMapReadyCallback,
        View.OnClickListener, DirectionCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String TAG = "Bus_details";
    private TextView mOutputTextView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double latSearch;
    double lngSearch;
    double latFirst;
    double lngFirst;
    private GoogleMap googleMap;
    private String serverKey = "AIzaSyANztP01h4SRxFBJjiKLrxm5uWP1yCQr4E";
    /*private LatLng camera = new LatLng(latFirst, lngFirst);
    private LatLng origin = new LatLng(latFirst, lngFirst);
    private LatLng destination = new LatLng(latSearch, lngSearch);*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        mOutputTextView = (TextView) findViewById(R.id.textView30);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            latSearch = bundle.getDouble("latSearch");
            lngSearch = bundle.getDouble("lngSearch");
            latFirst = bundle.getDouble("latFirst");
            lngFirst = bundle.getDouble("lngFirst");
            requestDirection();
        }
        requestGoogleApiClient();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latFirst, lngFirst), 16));
        LatLng cc = new LatLng(13.777069, 100.511260);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cc, 16));

    }

    public void requestGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void requestDirection() {
        LatLng origin = new LatLng(13.778512, 100.507915);
        LatLng destination = new LatLng(13.787855, 100.490267);
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                //.from(new LatLng(latFirst, lngFirst))
                .to(new LatLng(latSearch, lngSearch))
                //.to(destination)
                .language(Language.THAI)
                .transportMode(TransportMode.TRANSIT)
                .unit(Unit.METRIC)
                .transitMode(TransitMode.BUS)
                .execute(this);
        //Toast.makeText(getApplicationContext(), "ตำแหน่งแรก" + latFirst + " " + lngFirst + "ตำแหน่งที่ค้นหา" + latSearch + " " + lngSearch, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        String status = direction.getStatus();
        if (status.equals(RequestResult.OK)) {
            Toast toast = Toast.makeText(this, "OK", Toast.LENGTH_LONG);
            toast.show();


            ArrayList<LatLng> sectionPositionList = direction.getRouteList().get(0).getLegList().get(0).getSectionPoint();
            for (LatLng position : sectionPositionList) {
                googleMap.addMarker(new MarkerOptions().position(position));
            }

            List<Step> stepList = direction.getRouteList().get(0).getLegList().get(0).getStepList();
            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(this, stepList, 5, Color.RED, 3, Color.BLUE);
            for (PolylineOptions polylineOption : polylineOptionList) {
                googleMap.addPolyline(polylineOption);
            }

            TextView tg1 = (TextView) findViewById(R.id.textView2);
            TextView tg2 = (TextView) findViewById(R.id.textView3);
            TextView tg3 = (TextView) findViewById(R.id.textView4);
            TextView tg4 = (TextView) findViewById(R.id.textView5);
            TextView tg5 = (TextView) findViewById(R.id.textView6);
            TextView tg6 = (TextView) findViewById(R.id.textView7);
            TextView tg7 = (TextView) findViewById(R.id.textView8);
            /*String x1 = stepList.get(0).getTransitDetail().getLine().getShortName();
            String x2 = stepList.get(0).getTransitDetail().getStopNumber();
            String x3 = stepList.get(0).getTransitDetail().getLine().getName();
            String x4 = stepList.get(0).getDistance().getText();*/

            String x1 = direction.getRouteList().get(0).getLegList().get(0).getStepList().get(0).getTransitDetail().getLine().getShortName();
            String x2 = direction.getRouteList().get(0).getLegList().get(0).getStepList().get(0).getTransitDetail().getStopNumber();
            String x3 = direction.getRouteList().get(0).getLegList().get(0).getStepList().get(0).getTransitDetail().getLine().getName();
            String x4 = direction.getRouteList().get(0).getLegList().get(0).getStepList().get(0).getDistance().getText();
            String x5 = direction.getRouteList().get(0).getLegList().get(0).getStepList().get(0).getTransitDetail().getDepartureStopPoint().getName();
            String x6 = direction.getRouteList().get(0).getLegList().get(0).getStepList().get(0).getTransitDetail().getArrivalStopPoint().getName();
            //String x7 = direction.getRouteList().get(0).getLegList().get(0).getStepList().get(0).getTransitDetail().getLine().;

            /*แสดง สายรถประจำทาง String x1 = direction.getRouteList().get(0).getLegList().get(0).getStepList().get(0).getTransitDetail().getLine().getShortName();
            แสดง จำนวนป้ายที่ผ่าน String x2 = direction.getRouteList().get(0).getLegList().get(0).getStepList().get(0).getTransitDetail().getStopNumber();
            แสดง ชื่อป้ายต้นสาย-ปลายสาย String x3 = direction.getRouteList().get(0).getLegList().get(0).getStepList().get(0).getTransitDetail().getLine().getName();
            แสดง กม. String x4 = direction.getRouteList().get(0).getLegList().get(0).getStepList().get(0).getDistance().getText();
            แสดง ป้ายเริ่มต้น String x5 = direction.getRouteList().get(0).getLegList().get(0).getStepList().get(0).getTransitDetail().getDepartureStopPoint().getName();*/
            tg1.setText("รถประจำทางสาย " + x1);
            tg2.setText("ต้องผ่านทั้งหมด " + x2 + " ป้าย");
            tg3.setText("วิ่งจาก " + x3);
            tg4.setText("อีก " + x4);
            tg5.setText("ป้ายรถประจำทางเริ่มต้น " +x5);
            tg6.setText("ป้ายรถประจำทางปลายทาง "+x6);
            //tg7.setText(x7);


        } else if (status.equals(RequestResult.NOT_FOUND)) {
            Toast toast = Toast.makeText(this, "NOT_FOUND", Toast.LENGTH_LONG);
            toast.show();
        } else if (status.equals(RequestResult.ZERO_RESULTS)) {
            Toast toast = Toast.makeText(this, "ไม่มีเส้นทาง", Toast.LENGTH_LONG);
            toast.show();
        } else if (status.equals(RequestResult.MAX_WAYPOINTS_EXCEEDED)) {
            Toast toast = Toast.makeText(this, "MAX_WAYPOINTS_EXCEEDE", Toast.LENGTH_LONG);
            toast.show();
        } else if (status.equals(RequestResult.REQUEST_DENIED)) {
            Toast toast = Toast.makeText(this, "REQUEST_DENIED", Toast.LENGTH_LONG);
            toast.show();
        } else if (status.equals(RequestResult.UNKNOWN_ERROR)) {
            Toast toast = Toast.makeText(this, "UNKNOWN_ERROR", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Toast toast = Toast.makeText(this, "No", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button1) {
            finish();
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: error code = " + connectionResult.getErrorCode());

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    @Override
    public void onLocationChanged(Location location) {
        String text = String.format("Latitude: %s, \nLongitude: %s\n\n",
                location.getLatitude(), location.getLongitude());
        mOutputTextView.append(text);
        LatLng pp = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(pp)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        //Toast.makeText(getApplicationContext(), "ปัจุบัน" + location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }
}

