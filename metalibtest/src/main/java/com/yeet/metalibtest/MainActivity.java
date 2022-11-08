package com.yeet.metalibtest;

import static com.yeet.metalib.Utils.readAppData;
import static com.yeet.metalib.Utils.readMetadata;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread() {
            public void run() {
                try {
                    Log.d("Metadata", readMetadata().rom.latest.version);
                    Log.d("AppData", Objects.requireNonNull(readAppData(MainActivity.this, new Struct())).wow);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

class Struct {
    String wow;
}