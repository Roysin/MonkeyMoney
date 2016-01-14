package org.roysin.luckymoney;

import android.content.ComponentName;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private Button startButton;
    private ContentObserver ntfSettingObserver;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mHandler = new Handler();
//        ntfSettingObserver = new ContentObserver(mHandler) {
//            @Override
//            public void onChange(boolean selfChange) {
//
//                super.onChange(selfChange);
//                if(!isEnabled()){
//                    startButton.setText("start now !");
//                }else{
//                    startButton.setText("already started");
//                }
//            }
//        };

        startButton = (Button) findViewById(R.id.btn_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotificationAccess();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isEnabled()){
            startButton.setText("start now !");
        }else{
            startButton.setText("already started");
        }
    }

    private final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    private void openNotificationAccess() {
        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
