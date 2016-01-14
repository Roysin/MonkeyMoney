package org.roysin.luckymoney;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import org.roysin.luckymoney.utils.LogUtils;
import org.roysin.luckymoney.utils.LuckyMoneyHelper;
import org.roysin.luckymoney.view.ItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 */
@SuppressLint("OverrideAbstract")
public class RobMoneyService extends NotificationListenerService {

    private static final String TAG ="RobMoneyService" ;
    private static RobMoneyService instance;
    private LuckyMoneyHelper lmhelper;
    private List<IconManager.LuckyMoneyListener> mLuckyMoneyListeners;
    private IconManager mIconManager;

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        LogUtils.log(TAG,"onNotificationRemoved ");
        PendingIntent pi = sbn.getNotification().contentIntent;
        if(lmhelper.isLuckyMoney(pi)) {
            ItemInfo info = new ItemInfo();
            info.pendingIntent = pi;
            info.isLuckyMoney = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                info.messageSender = (String)sbn.getNotification().extras.get(Notification.EXTRA_TITLE);
            }
            notifyLuckyMoneyRemoved(info);
        }
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        LogUtils.log(TAG,"onNotificationPosted ");

        PendingIntent pi = sbn.getNotification().contentIntent;
        if(lmhelper.isLuckyMoney(pi)) {
            ItemInfo info = new ItemInfo();
            info.pendingIntent = pi;
            info.isLuckyMoney = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                info.messageSender = (String)sbn.getNotification().extras.get(Notification.EXTRA_TITLE);
            }
            notifyLuckyMoneyArrived(info);
        }
        super.onNotificationPosted(sbn);
    }

    private void notifyLuckyMoneyArrived(ItemInfo info) {
        LogUtils.log(TAG,"notifyLuckyMoneyArrived");
        if(mLuckyMoneyListeners != null){
            for(IconManager.LuckyMoneyListener l : mLuckyMoneyListeners){
                ItemInfo itemInfo = new ItemInfo(info);
                l.onLuckyMoneyArrived(itemInfo);
            }
        }
    }

    private void notifyLuckyMoneyRemoved(ItemInfo info) {
        LogUtils.log(TAG,"notifyLuckyMoneyArrived");
        if(mLuckyMoneyListeners != null){
            for(IconManager.LuckyMoneyListener l : mLuckyMoneyListeners){
                ItemInfo itemInfo = new ItemInfo(info);
                l.onLuckyMoneyRemoved(itemInfo);
            }
        }
    }

    public RobMoneyService(){
        super();
        mLuckyMoneyListeners = new ArrayList<IconManager.LuckyMoneyListener>();
        instance = this;
    }

    public static RobMoneyService getInstance(){
        if(instance == null){
            instance = new RobMoneyService();
        }
        return instance;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.log(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        LogUtils.log(TAG,"onCreate ");
        mIconManager = new IconManager(this);
        lmhelper = LuckyMoneyHelper.getInstace();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        LogUtils.log(TAG,"onDestroy ");
        mLuckyMoneyListeners.clear();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    public void registerLuckyMoneyListener(IconManager.LuckyMoneyListener l){
        LogUtils.log(TAG,"registerLuckyMoneyListener");
        if(mLuckyMoneyListeners != null ){
            if(mLuckyMoneyListeners.contains(l)){
                LogUtils.log(TAG,"registerLuckyMoneyListener: already have this listener, abandon!");
                return;
            }
            mLuckyMoneyListeners.add(l);
        }
    }

    public void unRegisterLuckyMoneyListener(IconManager.LuckyMoneyListener l){
        if(mLuckyMoneyListeners != null && mLuckyMoneyListeners.contains(l)){
            mLuckyMoneyListeners.remove(l);
        }
    }

}
