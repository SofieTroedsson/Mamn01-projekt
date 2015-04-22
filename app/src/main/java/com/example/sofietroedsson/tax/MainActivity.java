package com.example.sofietroedsson.tax;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

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
            case R.id.button3: {
                startActivity(new Intent(this, Test.class));
                vib.vibrate(500);
                break;

            }
            case R.id.button4:{
                startActivity(new Intent(this, highScore.class));
                vib.vibrate(500);
                break;
            }

        }
    }
}

