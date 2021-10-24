package com.example.giroscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
private Sensor sensor;
private SensorManager sysmanager; //sysmanager
private ImageView imgV;
private TextView txt;
private SensorEventListener sv; //sv
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.TV);
        imgV= findViewById(R.id.img);
        sysmanager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sysmanager!=null)
            sensor =sysmanager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        sv = new SensorEventListener ()
        {
            @Override
            public void onSensorChanged(SensorEvent event)
            {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                float[] rem = new float[16];
               
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        rem);
                float orient[] = new float[3];
                SensorManager.getOrientation(rem,orient);
                for (int i =0; i<3; i++) orient[i]=(float) (Math.toDegrees(orient[i]));
                txt.setText(String.valueOf((int)orient[2]));
                imgV.setRotationX(orient[2]);
                imgV.setRotationY(orient[2]);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {}
        };
    }
//    @Override
//    public void onAccuracyChanged (Sensor sensor, int accuracy){}

    @Override
    protected void onResume() {
        super.onResume();
        sysmanager.registerListener(sv,sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sysmanager.unregisterListener(sv);
    }

}