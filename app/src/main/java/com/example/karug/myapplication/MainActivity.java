package com.example.karug.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import static android.provider.AlarmClock.*;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager mSensorManager;
    Sensor mAccSensor;
    float[] values = new float[6];
    private boolean set_flag = false;
    int i = 10;
    int j = 0;
    int set_hour = 6;
    int set_minute = 0;
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            /*x_axis = String.valueOf(event.values[0])
             *y_axis = String.valueOf(event.values[1])
             *z_axis = String.valueOf(event.values[2])
             */
            values[0] = event.values[0];
            values[1] = event.values[1];
            values[2] = event.values[2];
        }

        /*String text = "x =" + values[0] +"\n"
                     +"y =" + values[1] +"\n"
                     +"z =" + values[2];
        Toast.makeText(this,text,Toast.LENGTH_SHORT);
    */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        TimePicker tp = findViewById(R.id.alarm);
        tp.setHour(set_hour);
        tp.setMinute(set_minute);
        final CompoundButton switch1 = findViewById(R.id.switch1);
        switch1.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void  onClick(View v) {
                switch1.setChecked(set_flag);
            }
        });

        final RadioGroup rg = findViewById(R.id.set_mode);
        final Button button1 = findViewById(R.id.setalarm);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker tp = findViewById(R.id.alarm);
                int hour = tp.getHour();
                int minute = tp.getMinute();
                int radio_check = rg.getCheckedRadioButtonId();
                String text;
                int radio_id;

                if (radio_check != -1) {
                    // 選択されているラジオボタンの取得
                    RadioButton radioButton = findViewById(radio_check);
                    // ラジオボタンのテキストを取得
                    text = radioButton.getText().toString();
                    radio_id = radioButton.getId();
                } else {
                    RadioButton NormalButton = findViewById(R.id.NormalBtn);
                    text = NormalButton.getText().toString();
                    radio_id = R.id.NormalBtn;
                }

                if (set_flag == false) {
                    /*Toast.makeText(
                            MainActivity.this,
                            hour + ":" + minute + "にセットしました\n" + text,
                            Toast.LENGTH_SHORT).show();
                    */
                    switch (radio_id) {
                        case R.id.ForceBtn:
                            for (; i > 0; i--)
                                NormalAlarm(hour, minute - i, R.id.ForceBtn, text);
                            break;
                        case R.id.NormalBtn:
                            NormalAlarm(hour, minute, R.id.NormalBtn, text);
                            break;
                        case R.id.NapBtn:
                            NormalAlarm(hour, minute, R.id.NapBtn, text);
                            break;
                        default:
                            Toast.makeText(MainActivity.this, "Missing Alarm Set", Toast.LENGTH_SHORT).show();
                    }
                    button1.setText(R.string.cancel);
                    switch1.setChecked(true);
                    String min = String.format("%02d", minute);
                    switch1.setText("Set time is " + hour + ":" + min + "\nMode is " + text);
                    set_flag = true;
                    j = 10 - i + 1;
                } else {
                    Toast.makeText(MainActivity.this, "キャンセルしました", Toast.LENGTH_SHORT).show();
                    CancelAlarm(j);
                    button1.setText(R.string.set);
                    switch1.setChecked(false);
                    switch1.setText(R.string.switch_none);
                    set_flag = false;
                    i = 10;
                }
            }
        });
    }

    private void NormalAlarm(int hour, int minute, int mode, String text) {
        Intent it = new Intent(ACTION_SET_ALARM);
        it.putExtra(EXTRA_HOUR, hour);
        it.putExtra(EXTRA_MINUTES, minute);
        it.putExtra(EXTRA_SKIP_UI, true);

        switch (mode) {
            case R.id.ForceBtn:
                it.putExtra(EXTRA_MESSAGE, text);
                //誤爆防止のためのサイレント
                it.putExtra(EXTRA_RINGTONE, VALUE_RINGTONE_SILENT);
                break;
            case R.id.NormalBtn:
                it.putExtra(EXTRA_MESSAGE, text);
                break;
            case R.id.NapBtn:
                it.putExtra(EXTRA_MESSAGE, text);
                //フェードインの処理
                break;
            default:
                break;
        }
        startActivity(it);
    }

    private void CancelAlarm(int num) {
        Intent dis = new Intent();
        dis.setAction(ACTION_DISMISS_ALARM);
        for (; num > 0; num--)
            startActivity(dis);
    }

    /*private void second_check(){
        Calendar calendar = Calendar.getInstance();
        int time1 = calendar.get(Calendar.SECOND);//カレンダーから現時刻の秒数を取得
        TextView text1 = findViewById(R.id.t_clock);
        text1.setText(String.valueOf(time1));
    }*/

    private void time_check() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        String text = "センサーでセット";
        NormalAlarm(hour, minute, R.id.NormalBtn, text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                setContentView(R.layout.option_set);
                Button set_button = findViewById(R.id.set_button);
                set_button.setOnClickListener(new View.OnClickListener(){
                EditText set_hour2 = findViewById(R.id.set_hour);
                EditText set_minute2 = findViewById(R.id.set_minute);

                    @Override
                    public void onClick(View v) {
                        if (Integer.parseInt(set_hour2.getText().toString()) <= 23
                                && Integer.parseInt(set_hour2.getText().toString()) >= 0
                                && Integer.parseInt(set_minute2.getText().toString()) <= 59
                                && Integer.parseInt(set_minute2.getText().toString()) >= 0) {
                            set_hour = Integer.parseInt(set_hour2.getText().toString());
                            set_minute = Integer.parseInt(set_minute2.getText().toString());
                            setContentView(R.layout.activity_main);
                            TimePicker tp = findViewById(R.id.alarm);
                            tp.setHour(set_hour);
                            tp.setMinute(set_minute);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "適切な値が設定されていません",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return true;

            case R.id.action_settings2:
                Toast.makeText(this, "データへ", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings3:
                Toast.makeText(this, "使い方へ", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings4:
                CancelAlarm(i);
                Toast.makeText(this, "消しました",Toast.LENGTH_SHORT).show();
                return true;

            default:
                Toast.makeText(this, "おぷしょんへ", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

