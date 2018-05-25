package com.example.karug.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SubActivity extends AppCompatActivity {

    int[] result = new int[3];

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.option_set);
            Intent intent2 = getIntent();
            result = intent2.getIntArrayExtra("result2");
            final Button set_button = findViewById(R.id.set_button);
            final EditText set_hour2 = findViewById(R.id.set_hour);
            final EditText set_minute2 = findViewById(R.id.set_minute);
            final EditText set_alarm = findViewById(R.id.mode_set);
            set_alarm.setText(String.valueOf(result[0]));
            set_hour2.setText(String.valueOf(result[1]));
            set_minute2.setText(String.valueOf(result[2]));

            set_button.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    if (Integer.parseInt(set_hour2.getText().toString()) <= 23
                            && Integer.parseInt(set_hour2.getText().toString()) >= 0
                            && Integer.parseInt(set_minute2.getText().toString()) <= 59
                            && Integer.parseInt(set_minute2.getText().toString()) >= 0) {
                        result[1] = Integer.parseInt(set_hour2.getText().toString());
                        result[2] = Integer.parseInt(set_minute2.getText().toString());
                    }
                    else
                        Toast.makeText(SubActivity.this, "適切な時間が設定されていません", Toast.LENGTH_SHORT).show();
                    if(Integer.parseInt(set_alarm.getText().toString()) >= 1){
                        result[0] = Integer.parseInt(set_alarm.getText().toString());
                    }
                    else
                        Toast.makeText(SubActivity.this, "適切な回数が設定されていません", Toast.LENGTH_SHORT).show();
                    intent.putExtra("result",result);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

        }
    }

