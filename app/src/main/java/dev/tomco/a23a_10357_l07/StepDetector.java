package dev.tomco.a23a_10357_l07;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepDetector {
    private StepCallback stepCallback;
    private SensorManager sensorManager;
    private Sensor sensor;


    private int stepCountX = 0;
    private int stepCountY = 0;
    private long timestemp = 0;

    private SensorEventListener sensorEventListener;

    public StepDetector(Context context, StepCallback _stepCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.stepCallback = _stepCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];

                calculateStep(x, y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void calculateStep(float x, float y) {
        if (System.currentTimeMillis() - timestemp > 500) {
            timestemp = System.currentTimeMillis();
            if (x > 6.0) {
                stepCountX++;
                if (stepCallback != null)
                    stepCallback.stepX();
            }
            if (y > 6.0) {
                stepCountY++;
                if (stepCallback != null)
                    stepCallback.stepY();
            }
        }
    }

    public int getStepCountX() {
        return stepCountX;
    }

    public int getStepCountY() {
        return stepCountY;
    }

    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
