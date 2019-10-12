package com.example.phogauss.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phogauss.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton cameraFab = findViewById(R.id.cameraFAB);
        FloatingActionButton galleryFab = findViewById(R.id.galleryFAB);

        cameraFab.setOnClickListener( view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        galleryFab.setOnClickListener( view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });






    }

}
