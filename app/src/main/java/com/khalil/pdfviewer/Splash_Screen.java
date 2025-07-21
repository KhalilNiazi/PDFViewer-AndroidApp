package com.khalil.pdfviewer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash_Screen extends AppCompatActivity {

        private static final int SPLASH_DELAY = 2500; // 2.5 seconds

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash_screen);

            ImageView logo = findViewById(R.id.logoImage);
            TextView title = findViewById(R.id.appTitle);

            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            logo.startAnimation(fadeIn);
            title.startAnimation(fadeIn);

            new Handler().postDelayed(() -> {
                startActivity(new Intent(Splash_Screen.this, MainActivity.class));
                finish();
            }, SPLASH_DELAY);
        }
    }
