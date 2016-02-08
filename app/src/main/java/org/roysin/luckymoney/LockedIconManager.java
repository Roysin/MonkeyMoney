package org.roysin.luckymoney;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import org.roysin.luckymoney.view.ItemInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/2/4.
 */
public class LockedIconManager extends Activity implements View.OnClickListener{
    private static final String TAG = "LockedIconManager";
    private ImageButton imgBtn = null;
    private static final int MESSAGE_SHOW_MSGLISTS =0x0000 ;
    private static final int MESSAGE_ICON_CLICKED =0x0001 ;

    private List<ItemInfo> unReadMsgs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.locked_icon_layout);
        imgBtn = (ImageButton)findViewById(R.id.money_btn);
        imgBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        //enter wechat
        Intent intent = getIntent();
        PendingIntent pendingIntent = (PendingIntent)intent.getParcelableExtra("pendingIntent");
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            Log.i(TAG, "pendingInetent Failed");
            e.printStackTrace();
        }
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
