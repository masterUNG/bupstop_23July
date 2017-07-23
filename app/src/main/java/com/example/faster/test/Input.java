package com.example.faster.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.speech.RecognizerIntent;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class Input extends AppCompatActivity {
    private static final int REQUEST_VOIC_RECOGINITION = 100 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Toast.makeText(this,"ใส่ป้ายรถประจำทางที่ต้องการ",Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void OnclickOK(View view) {
        Button bt_OnclickOK = (Button) findViewById(R.id.button3);
        EditText inputOnline = (EditText)findViewById(R.id.editText);
        String textinput = inputOnline.getText().toString();
        if (textinput.matches("")) {
            Toast.makeText(this, "กรุณาใส่ป้ายรถประจำทางเป้าหมาย", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Input.this, Bus_line.class);
            intent.putExtra("inputOnline", inputOnline.getText().toString());
            startActivity(intent);
            finish();
        }

    }

    public void OnclickCancel (View view) {
        Button bt_OnclickCancel = (Button) findViewById(R.id.button4);
        Intent intentt = new Intent(Input.this, System.class);
        startActivity(intentt);
        finish();
    }

    public void Onclickmicrophone (View view) {
        ImageButton Onclickmicrophone = (ImageButton) findViewById(R.id.imageButton);
        callVoiceRecognition();
    }

    private void callVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "th-TH");
        startActivityForResult(intent, REQUEST_VOIC_RECOGINITION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VOIC_RECOGINITION &&
                resultCode == RESULT_OK &&
                data != null) {
            ArrayList<String> resultList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            EditText text = (EditText) findViewById(R.id.editText);
            text.setText("");
            String name = resultList.get(0);
            text.setText(name);
            Toast.makeText(this,name,Toast.LENGTH_SHORT).show();
        }
    }

}
