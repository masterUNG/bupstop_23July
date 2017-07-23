package com.example.faster.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Load extends AppCompatActivity {
    private Handler handler;
    private Runnable runnable;
    private MyOpenHelper mHelper;
    private SQLiteDatabase mDb;

    private BusstopTABLE objBusstopTABLE;
    private busTABLE objBusTABLE;
    private busrouteTABLE objBusrouteTABLE;
    // กำหนดค่าเริ่มต้นของสถานะเป็น false
    Boolean isInternetPresent = false;

    // เรียกใช้งาน  ConnectionDetector
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();

        runnable = new Runnable() {
            public void run() {
                Intent intent = new Intent(Load.this, System.class);
                startActivity(intent);
                finish();
            }
        };

        objBusstopTABLE = new BusstopTABLE(this);
        objBusTABLE = new busTABLE(this);
        objBusrouteTABLE = new busrouteTABLE(this);

        //TestAddValue();
        cd = new ConnectionDetector(getApplicationContext());
        // get สถานะการเชื่อมต่ออินเตอร์เน็ต
        isInternetPresent = cd.isConnectingToInternet();
        // ตรวจสอบสถานะการเชื่อมต่ออินเตอร์เน็ต\
        if(isInternetPresent) {
            isInternetPresent = true;
            deleteAllData();
            synJSonTOSQLite();
            synJSonTOSQLiteatBusTable();
            synJSONTOSQLiteAtBusroute();
            Toast.makeText(this,"คุณกำลังเชื่อมมต่ออินเตอร์เน็ต อัพเดทฐานข้อมูลสำเร็จ",Toast.LENGTH_SHORT).show();
        }
        else{
            // หากไม่ได้เชื่อมต่ออินเตอร์เน็ต
            isInternetPresent = false;
            Toast.makeText(this,"คุณไม่ได้เชื่อมต่ออินเตอร์เน็ต",Toast.LENGTH_SHORT).show();
        }
    }
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000);
    }

    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
    private void synJSONTOSQLiteAtBusroute() {

        if(Build.VERSION.SDK_INT >9){

            StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(myPolicy);
        }
        InputStream objInputSreamBusroute = null;
        String strJSONBUSROUTE = "";

        try{
            HttpClient objHttpClient = new DefaultHttpClient();
            HttpPost objHttpPost = new HttpPost("http://jsontosqlite.esy.es/inner%20join.php");
            HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
            HttpEntity objHttpEntity = objHttpResponse.getEntity();
            objInputSreamBusroute = objHttpEntity.getContent();

        }catch (Exception e){
            Log.d("Busroute","Error From InputStream ==>"+ e.toString());
        }
        try {

            BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputSreamBusroute, "UTF-8"));
            StringBuilder objStringBuilder = new StringBuilder();
            String strLinebusroute = null;

            while ((strLinebusroute = objBufferedReader.readLine()) != null ) {
                objStringBuilder.append(strLinebusroute);
            }   // while

            objInputSreamBusroute.close();
            strJSONBUSROUTE = objStringBuilder.toString();

        } catch (Exception e) {
            Log.d("Busroute", "Error Create String ==> " + e.toString());
        }

        try {

            final JSONArray objJSONArrayBUSROUTE = new JSONArray(strJSONBUSROUTE);
            for (int i = 0; i < objJSONArrayBUSROUTE.length(); i++) {

                JSONObject objJSONBUSObject = objJSONArrayBUSROUTE.getJSONObject(i);
                String strbus = objJSONBUSObject.getString("bus");
                String strNamebusstop = objJSONBUSObject.getString("Namebusstop");
                String strxX = objJSONBUSObject.getString("X");
                String stryY = objJSONBUSObject.getString("Y");
                busrouteTABLE objBusrouteTABLE = new busrouteTABLE(this);
                long ValeBusroute = objBusrouteTABLE.addValueToBusroute(strbus,strNamebusstop,strxX,stryY);

            }   // for

        } catch (Exception e) {
            Log.d("Busroute", "Error Up Value ==> " + e.toString());
        }

    }

    private void synJSonTOSQLiteatBusTable() {

        if(Build.VERSION.SDK_INT >9){

            StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(myPolicy);
        }
        InputStream objInputSreamBus = null;
        String strJSONBUS = "";

        try{
            HttpClient objHttpClient = new DefaultHttpClient();
            HttpPost objHttpPost = new HttpPost("http://jsontosqlite.esy.es/php_get_data_bustable.php");
            HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
            HttpEntity objHttpEntity = objHttpResponse.getEntity();
            objInputSreamBus = objHttpEntity.getContent();

        }catch (Exception e){
            Log.d("Bus","Error From InputStream ==>"+ e.toString());
        }
        try {

            BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputSreamBus, "UTF-8"));
            StringBuilder objStringBuilder = new StringBuilder();
            String strLinebus = null;

            while ((strLinebus = objBufferedReader.readLine()) != null ) {
                objStringBuilder.append(strLinebus);
            }   // while

            objInputSreamBus.close();
            strJSONBUS = objStringBuilder.toString();

        } catch (Exception e) {
            Log.d("Bus", "Error Create String ==> " + e.toString());
        }

        try {

            final JSONArray objJSONArrayBUS = new JSONArray(strJSONBUS);
            for (int i = 0; i < objJSONArrayBUS.length(); i++) {

                JSONObject objJSONBUSObject = objJSONArrayBUS.getJSONObject(i);
                String strbus = objJSONBUSObject.getString("bus");
                String strbus_details = objJSONBUSObject.getString("bus_details");
                busTABLE objBusTABLE = new busTABLE(this);
                long ValeBus = objBusTABLE.addValueToBus(strbus, strbus_details);

            }   // for

        } catch (Exception e) {
            Log.d("Bus", "Error Up Value ==> " + e.toString());
        }
    }

    private void synJSonTOSQLite() {

        if(Build.VERSION.SDK_INT >9){

            StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(myPolicy);
        }

        InputStream objInputStream = null;
        String strJSON ="";

        try{
            HttpClient objHttpClient = new DefaultHttpClient();
            HttpPost objHttpPost = new HttpPost("http://jsontosqlite.esy.es/php_get_data_busstoptable.php");
            HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
            HttpEntity objHttpEntity = objHttpResponse.getEntity();
            objInputStream = objHttpEntity.getContent();

        }catch (Exception e){
            Log.d("Busstop","Error From InputStream ==>"+ e.toString());
        }
        try {

            BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
            StringBuilder objStringBuilder = new StringBuilder();
            String strLine = null;

            while ((strLine = objBufferedReader.readLine()) != null ) {
                objStringBuilder.append(strLine);
            }   // while

            objInputStream.close();
            strJSON = objStringBuilder.toString();

        } catch (Exception e) {
            Log.d("Busstop", "Error Create String ==> " + e.toString());
        }

        try {

            final JSONArray objJSONArray = new JSONArray(strJSON);
            for (int i = 0; i < objJSONArray.length(); i++) {

                JSONObject objJSONObject = objJSONArray.getJSONObject(i);
                String strX = objJSONObject.getString("X");
                String strY = objJSONObject.getString("Y");
                String strNamebusstop = objJSONObject.getString("Namebusstop");
                String strbuspassing  = objJSONObject.getString("buspassing");
                long insertVale = objBusstopTABLE.addValueToBusstop(strX, strY, strNamebusstop,strbuspassing);
            }

        } catch (Exception e) {
            Log.d("Busstop", "Error Up Value ==> " + e.toString());
        }




    }


    private void deleteAllData() {

        SQLiteDatabase objSQLite = openOrCreateDatabase("Busstop.db", MODE_PRIVATE, null);
        objSQLite.delete("busstopTABLE", null, null);
        objSQLite.delete("busTABLE", null, null);
        objSQLite.delete("busrouteTABLE",null,null);


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
