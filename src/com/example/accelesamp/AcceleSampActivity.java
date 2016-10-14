package com.example.accelesamp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

//*�ǉ�*/
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.IOException;

import android.os.Environment;

import java.io.File;

import android.util.Log;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AcceleSampActivity extends Activity
        implements SensorEventListener {

    private TextView txt01;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
//    private boolean startFlag = false;


    //*�ǉ��]�[��*/
    public StringBuffer sensorData = new StringBuffer();
    //*�����܂�*/

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accele_samp);

        //*�ǉ��]�[��*/
        Button startbutton = (Button) findViewById(R.id.startbutton);
        startbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startbuttononClick();
            }

        });

        Button stopbutton = (Button) findViewById(R.id.stopbutton);
        stopbutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                stopbuttononClick();
            }

        });

        //*�����܂�*/


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        txt01 = (TextView) findViewById(R.id.txt01);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, 400000);
    }

    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        Date now = new Date();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(now);

        StringBuffer bufStr = new StringBuffer();

        bufStr.append("Xline:" + event.values[0] + "\n");
        bufStr.append("Yline:" + event.values[1] + "\n");
        bufStr.append("Zline:" + event.values[2] + "\n");

        txt01.setText(bufStr.toString());

        sensorData.append(date);
        sensorData.append(',');
        sensorData.append(event.values[0]);
        sensorData.append(',');
        sensorData.append(event.values[1]);
        sensorData.append(',');
        sensorData.append(event.values[2]);
        sensorData.append(System.getProperty("line.separator"));
    }

    //*�ǉ��]�[��*/

    private void startbuttononClick() {
        sensorData = new StringBuffer();
    }

    private void stopbuttononClick() {
        Date now = new Date();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(now);
        exportFile(date + ".csv" , sensorData.toString());
    }

    private void exportFile(String filename, String content) {
        final String directory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AcceleSamp";
        final File logfile = new File(directory, filename);
        final File logPath = logfile.getParentFile();


        if (!logPath.isDirectory() && !logPath.mkdirs()) {
            Log.e("export_data", "Could not create directory for log files");
        }
        try {
            FileWriter filewriter = new FileWriter(logfile);
            BufferedWriter bw = new BufferedWriter(filewriter);
            bw.write(content);
            bw.flush();
            bw.close();
            Log.i("export_data", "export finished!");
        } catch (IOException ioe) {
            Log.e("export_data", "IOException while writing Logfile");
        }
    }
    //*�����܂�*/
}
