package com.example.karug.myapplication;

import android.content.Intent;
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
import static android.provider.AlarmClock.*;


public class MainActivity extends AppCompatActivity {
    private boolean set_flag = false;
    int i = 10;
    int j = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         TimePicker tp = findViewById(R.id.alarm);
         tp.setHour(7);
         tp.setMinute(0);
         final CompoundButton switch1 = findViewById(R.id.switch1);
         switch1.setOnClickListener(new CompoundButton.OnClickListener(){
             @Override
             public void onClick(View v){
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
                }
                else {
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
                    switch (radio_id){
                        case R.id.ForceBtn:
                            for(;i > 0; i--)
                                NormalAlarm(hour,minute - i, R.id.ForceBtn, text);
                            break;
                        case R.id.NormalBtn:
                            NormalAlarm(hour, minute, R.id.NormalBtn, text);
                            break;
                        case R.id.NapBtn:
                            NormalAlarm(hour, minute, R.id.NapBtn, text);
                            break;
                        default:
                            Toast.makeText(MainActivity.this,"Missing Alarm Set", Toast.LENGTH_SHORT).show();
                    }
                    button1.setText(R.string.cancel);
                    switch1.setChecked(true);
                    String min = String.format("%02d", minute);
                    switch1.setText("Set time is " + hour + ":" + min + "\nMode is " + text);
                    set_flag = true;
                    j = 10 - i + 1;
                }

                else {
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

    private void NormalAlarm(int hour, int minute, int mode,String text){
        Intent it = new Intent(ACTION_SET_ALARM);
        it.putExtra(EXTRA_HOUR, hour);
        it.putExtra(EXTRA_MINUTES, minute);
        it.putExtra(EXTRA_SKIP_UI,true);

        switch (mode){
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
        Intent dis = new Intent(ACTION_DISMISS_ALARM);
        for(;num>0;num--)
        startActivity(dis);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int num = 10;
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "設定メニューへ", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings2:
                Toast.makeText(this, "データへ", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings3:
                Toast.makeText(this, "使い方へ", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings4 :
                CancelAlarm(num);
                Toast.makeText(this, "消しました",Toast.LENGTH_SHORT).show();
                return true;

            default:
                Toast.makeText(this, "おぷしょんへ", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }
}

