package com.example.sensores_acelerometro;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // -- Define um gestor de sensores
    private SensorManager sensorManager;
    private  boolean cor = false;
    // -- Define um view genérico
    private View view;
    private long updateTempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //-- Desenho da janela fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-- indicar com cor blackground azul
        view = findViewById(R.id.textView);
        view.setBackgroundColor(Color.BLUE);

        //-- Aceder a um dos sensores (o Acelermetro)
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                sensorManager.SENSOR_DELAY_NORMAL);
        //-- Obtém a hora actual do sistema (em milisegundos)
        updateTempo = System.currentTimeMillis();

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType()== Sensor.TYPE_ACCELEROMETER){
            calculosAcelerometro(event);
        }

    }

    public void calculosAcelerometro(SensorEvent event){
        // -- Valores de movimento nos 3 eixos
        float[] values = event.values;
        float x = values[0]; float y = values[1]; float z = values[2];

        // -- Obtém a hora atual do system
        long tempoActual = System.currentTimeMillis();

        //-- calcula a aceleração
        float aceleracao =(x*x + y*y + z*z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        //-- se houver um moviemnto suficiente grande
        if(aceleracao >=2){
            if(tempoActual - updateTempo < 200){return;}
            updateTempo = tempoActual;
            Toast.makeText(this,"Dectado movimento!", Toast.LENGTH_SHORT).show();
            if (cor){view.setBackgroundColor(Color.BLUE);
            }else{ view.setBackgroundColor(Color.GREEN);}
            cor = !cor;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //-- Para evitar a utilização desnecessária de bateria,
    //-- o listener é registado novamente neste metedo e "libertado"
    //-- no metodo "onPause()"


    @Override
    protected void onResume() {
        super.onResume();

        //-- Resgistar esta classe como um listener para o sensor ACCELEROMETER
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //-- libertar o listener
        sensorManager.unregisterListener(this);
    }
}