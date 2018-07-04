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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import static android.provider.AlarmClock.*;
import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sm;
    Sensor ac;
    int NUM = 10;
    private TimePicker tp;
    float[] values = new float[3];
    private boolean set_flag = false;
    private boolean sensor_flag = true;
    private boolean time_flag = false;
    private boolean hour_checker = true;
    int i = NUM;
    int j = 0;
    public int set_hour = 8;
    public int set_minute = 0;

    @Override
    protected void onPause() {
        super.onPause();
        if (!sensor_flag) {
            sm.unregisterListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ac = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);  //加速度センサの取得
        sm.registerListener(this, ac, SensorManager.SENSOR_DELAY_NORMAL);  //加速度センサのリスナー登録
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        TimePicker tp = findViewById(R.id.alarm);
        int gethour = tp.getHour();
        int getminute = tp.getMinute();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if(getminute >=  20 && getminute <= 60){
            hour_checker = false;
        }

        if(hour_checker){//センサーが利用可能で分が19以下たっだとき
            if(gethour - 1 == hour && 60 - getminute <= minute)
                time_flag = true;
        }
        else if(!hour_checker){//センサーが利用可能で分が20以上だった時
            if(gethour == hour && getminute - 20 <= minute)
                time_flag = true;
        }
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            values[0] = event.values[0];
            values[1] = event.values[1];
            values[2] = event.values[2];
            /*
            ここから加速度センサの大きさ比較でアラームを鳴らす(修正箇所)
            ifの条件として
            加速度を適切なものに設定する
            設定時間の20分前にflagをたて設定時間が過ぎたときにtrueにし、アラームを鳴らせる状態にする
            */

            if (
                    set_flag && sensor_flag && time_flag
                            && (abs(values[0]) >= 12 || abs(values[1]) >= 12 || abs(values[2]) >= 12)
                    ) {
                Toast.makeText(MainActivity.this, "時間は" + gethour + "分は" + getminute +"チェッカ" + hour_checker,Toast.LENGTH_SHORT).show();
                time_check();
                sensor_flag = false;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ac = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        tp = findViewById(R.id.alarm);
        tp.setHour(set_hour);
        tp.setMinute(set_minute);
        final CompoundButton switch1 = findViewById(R.id.switch1);
        switch1.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                if (!set_flag) {
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
                    j = NUM - i + 1;
                } else {
                    Toast.makeText(MainActivity.this, "キャンセルしました", Toast.LENGTH_SHORT).show();
                    CancelAlarm(j);
                    button1.setText(R.string.set);
                    switch1.setChecked(false);
                    switch1.setText(R.string.switch_none);
                    set_flag = false;
                    i = NUM;
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

    private void time_check() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String text = "センサーでセット";
        NormalAlarm(hour, minute + 1, R.id.NormalBtn, text);
        //現時刻でセットすると明日になるため1分遅らせる
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "消しました", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings4:
                CancelAlarm(NUM);
                Toast.makeText(this, "消しました", Toast.LENGTH_SHORT).show();
                return true;

            default:
                Toast.makeText(this, "おぷしょんへ", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}