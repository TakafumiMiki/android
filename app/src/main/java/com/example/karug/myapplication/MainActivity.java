package com.example.karug.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private boolean set_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

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
                String text = null;
                int radio_id = 0;

                if (radio_check != -1) {
                    // 選択されているラジオボタンの取得
                    RadioButton radioButton = findViewById(radio_check);
                    // ラジオボタンのテキストを取得
                    text = radioButton.getText().toString();
                    radio_id = radioButton.getId();
                }

                //AddAlarm(hour,minute);
                //Toast.LENGTH_LONG で表示時間4s SHORTは2s
                if (set_flag == false) {
                    Toast.makeText(
                            MainActivity.this,
                             hour + ":" + minute + "にセットしました\n" + text + radio_id,
                            Toast.LENGTH_SHORT).show();
                    button1.setText(R.string.cancel);
                    switch1.setChecked(true);
                    set_flag = true;
                } else {
                    Toast.makeText(MainActivity.this, "キャンセルしました", Toast.LENGTH_SHORT).show();
                    button1.setText(R.string.set);
                    switch1.setChecked(false);
                    set_flag = false;
                }

            }
        });
    }

    //既存のアラームを使う
    /*private void  AddAlarm(int hour, int minute){
        Intent it = new Intent(AlarmClock.ACTION_SET_ALARM);
        it.putExtra(AlarmClock.EXTRA_HOUR, hour);
        it.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        startActivity(it);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "設定メニューへ", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings2:
                Toast.makeText(this, "read meへ", Toast.LENGTH_SHORT).show();
                return true;
            default:
                Toast.makeText(this, "おぷしょんへ", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }
}

