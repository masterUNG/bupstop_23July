package com.example.faster.test;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class System extends AppCompatActivity {

    // กำหนดค่าเริ่มต้นของสถานะเป็น false
    Boolean isInternetPresent = false;

    // เรียกใช้งาน  ConnectionDetector
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Toast.makeText(this,"เลือกระบบการใช้งาน ออนไลน์ กดด้านซ้ายของหน้าจอ ออฟไลน์ กดด้านขวาของหน้าจอ",Toast.LENGTH_SHORT).show();


        //Check GPS
        checkGPS();


    }   // Main Method

    private void checkGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean bolGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!bolGPS) {
            //Not Open GPS
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void OnclickOnline(View view) {
        Button bt_Online = (Button) findViewById(R.id.buttonOnline);
        cd = new ConnectionDetector(getApplicationContext());
        // get สถานะการเชื่อมต่ออินเตอร์เน็ต
        isInternetPresent = cd.isConnectingToInternet();
        // ตรวจสอบสถานะการเชื่อมต่ออินเตอร์เน็ต
        if (isInternetPresent) {
            //หากเชื่อมต่ออินเตอร์เน็ต
            isInternetPresent = true;
            Toast.makeText(this,"คุณกำลังเชื่อมต่ออินเตอร์เน็ต",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,Input.class);
            startActivity(intent);
        }
        else {
            // หากไม่ได้เชื่อมต่ออินเตอร์เน็ต
            isInternetPresent = false;
            Toast.makeText(this,"คุณไม่ได้เชื่อมต่ออินเตอร์เน็ต กรุณาเชื่อมต่ออินเตอร์เน็ตของท่าน",Toast.LENGTH_SHORT).show();
        }
    }
    public void OnclickOffline(View view) {
        Button bt_Onclickoffline = (Button) findViewById(R.id.buttonOffline);
        Intent intent = new Intent(this, Inputoffline.class);
        startActivity(intent);
    }

}
