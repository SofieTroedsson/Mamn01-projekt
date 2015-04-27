package com.example.sofietroedsson.tax;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton button = (ImageButton) findViewById(R.id.button);
        ImageButton button2 = (ImageButton) findViewById(R.id.button2);
        ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);


    }

    public void onClick(View v){
       Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        switch (v.getId()) {
            case R.id.button: {
                startActivity(new Intent(this, Tax.class));
                vib.vibrate(500);
                break;
            }

            case R.id.button2: {
                startActivity(new Intent(this, Options.class));
                vib.vibrate(500);
                break;
            }
            case R.id.button3:{
                startActivity(new Intent(this, highScore.class));
                vib.vibrate(500);
                break;
            }

        }
    }
}

