package org.roysin.luckymoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Intent serviceIntent ;
    boolean start;
    Button startButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.btn_start);
        serviceIntent = new Intent();
        serviceIntent.setClass(getApplicationContext(), RobMoneyService.class);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start){
                    stopService(serviceIntent);
                    startButton.setText("ON");
                }else {
                    startService(serviceIntent);
                    startButton.setText("OFF");
                }
                start = !start;
            }
        });
    }


}
