package com.javiertorres.pendulum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                start();
            }
        });

        Button btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stop();
            }
        });

        CanvasView view = findViewById(R.id.view);
        view.setMainActivity(this);
        view.initialize();
    }

    private void start() {
        CanvasView view = findViewById(R.id.view);
        view.start();
    }

    private void stop() {
        CanvasView view = findViewById(R.id.view);
        view.stop();
    }


    public void getData() {

        double angle = Double.parseDouble(((EditText) findViewById(R.id.txtAngle)).getText().toString());
        double length = Double.parseDouble(((EditText) findViewById(R.id.txtLength)).getText().toString());
        double mass = Double.parseDouble(((EditText) findViewById(R.id.txtMass)).getText().toString());
        double gravity = Double.parseDouble(((EditText) findViewById(R.id.txtGravity)).getText().toString());
        double friction = Double.parseDouble(((EditText) findViewById(R.id.txtFriction)).getText().toString());
        double velocity = Double.parseDouble(((EditText) findViewById(R.id.txtVelocity)).getText().toString());

        CanvasView view = findViewById(R.id.view);
        view.initPendulum(angle, length, mass, gravity, friction, velocity);


    }

    public void defaultParameters() {
        ((EditText) findViewById(R.id.txtMass)).setText("1");
        ((EditText) findViewById(R.id.txtGravity)).setText("9.81");
        ((EditText) findViewById(R.id.txtLength)).setText("1");
        ((EditText) findViewById(R.id.txtAngle)).setText("-85");
        ((EditText) findViewById(R.id.txtFriction)).setText("0.1");
        ((EditText) findViewById(R.id.txtVelocity)).setText("0");
    }
}
