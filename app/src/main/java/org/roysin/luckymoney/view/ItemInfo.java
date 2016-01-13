package org.roysin.luckymoney.view;

import android.app.PendingIntent;

/**
 * Created by Administrator on 2016/1/12.
 */
public class ItemInfo {
    public String PackageName;
    public String messageSender;
    public boolean isLuckyMoney;
    public int messageNumber;
    public int instanceId;
    public PendingIntent pendingIntent;

    public ItemInfo(){
        PackageName = "com.tencent.mm";
        messageSender = "user";
        isLuckyMoney = true;
        messageNumber = 1;
        instanceId = 0;
        pendingIntent = null;
    }
    public ItemInfo ( ItemInfo info){
        PackageName = info.PackageName;
        messageSender = info.messageSender;
        isLuckyMoney = info.isLuckyMoney;
        messageNumber = info.messageNumber;
        instanceId = info.instanceId;
        pendingIntent = info.pendingIntent;
    }
}
