package org.roysin.luckymoney;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.roysin.luckymoney.utils.LogUtils;
import org.roysin.luckymoney.utils.LuckyMoneyHelper;
import org.roysin.luckymoney.view.ItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 */
public class IconManager  {

    private static final String TAG = "IconManager";
    private static final int MESSAGE_SHOW_MSGLISTS =0x0000 ;
    private static final int MESSAGE_ICON_CLICKED =0x0001 ;

    private WindowManager mWm;
    private WindowManager.LayoutParams mLayoutParams;
    private IconAdapter mAdapter;
    private ListView mIconListView;
    private RelativeLayout mFloatMessagesLayout;
    private ImageView mMoveButton;

    private Context mContext;
    private List<ItemInfo> unReadMsgs;
    private RobMoneyService mService;
    private boolean mIsViewShowing;
    private static Handler mHandler;

    public IconManager(Context context){
        LogUtils.log(TAG,"IconManager constructor");
        mContext = context;

        if(mHandler == null){
            mHandler = new Handler(mContext.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case MESSAGE_SHOW_MSGLISTS:
                            showFloatMsgs();
                            break;
                        case MESSAGE_ICON_CLICKED:
                            int position = msg.arg1;
                            unReadMsgs.remove(position);
                            LogUtils.log(TAG,"unreadMsgs.size = "+unReadMsgs.size());
                            if(unReadMsgs.size() == 0){
                                removeFloatMsgsView();
                            }
                            mAdapter.setData(unReadMsgs);
                            mAdapter.notifyDataSetChanged();
                            break;
                        default:
                            break;
                    }

                    super.handleMessage(msg);
                }
            };
        }
        mFloatMessagesLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.float_message_list_layout, null);

        mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams =getWindowManagerParams();
        mMoveButton = (ImageView) mFloatMessagesLayout.findViewById(R.id.moveButton);
        mIconListView = (ListView) mFloatMessagesLayout.findViewById(R.id.msg_list);
        mAdapter = new IconAdapter();
        mIconListView.setAdapter(mAdapter);
        mMoveButton.setOnTouchListener(new View.OnTouchListener() {
            private boolean isMovingEnabled;
            private float downX;
            private float downY;
            private final float MIN_MOVE_X = 20;
            private final float MIN_MOVE_Y = 20;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getRawX();
                        downY = event.getRawY();
                        LogUtils.log(TAG,"onTouch ACTION_DOWN :downX = "+downX + " downY = " +downY);
                        break;
                    case MotionEvent.ACTION_MOVE:

                        float x = event.getRawX();
                        float y = event.getRawY();
                        if(!isMovingEnabled
                                &&(Math.abs(downX -x) >= MIN_MOVE_X)
                                && (Math.abs(downY - y) >= MIN_MOVE_Y)){
                                isMovingEnabled = true;
                        }else{
                            mLayoutParams.x = (int)x-mLayoutParams.width/2;
                            mLayoutParams.y = (int)y-80;
                            mWm.updateViewLayout(mFloatMessagesLayout,mLayoutParams);
                        }
                        LogUtils.log(TAG,"onTouch ACTION_MOVE x = "+x + " y = " +y+" isMovingEnabled = "+isMovingEnabled);
                        break;
                    case MotionEvent.ACTION_UP:
                        isMovingEnabled = false;
                        break;
                }
                return true;
            }
        });


        mService = RobMoneyService.getInstance();
        mService.registerLuckyMoneyListener(new LuckyMoneyListener() {
            @Override
            public void onLuckyMoneyArrived(ItemInfo info) {
                LogUtils.log(TAG,"onLuckyMoneyArrived");
                if (unReadMsgs == null) {
                    unReadMsgs = new ArrayList<ItemInfo>();
                }
                String sender = info.messageSender;
                int instanceId = info.instanceId;
                ItemInfo item = LuckyMoneyHelper.getInstace().getItemFromList(unReadMsgs, sender, instanceId);
                if (item != null) {
                    unReadMsgs.remove(item);
                }
                unReadMsgs.add(0, info);
                if (unReadMsgs.size() >= 1) {
                   mHandler.sendEmptyMessage(MESSAGE_SHOW_MSGLISTS);
                }
            }

            @Override
            public void onLuckyMoneyRemoved(ItemInfo info) {
                LogUtils.log(TAG,"onLuckyMoneyRemoved");

            }
        });
    }

    private void showFloatMsgs() {
        LogUtils.log(TAG,"showFloatMsgs");
        if(!mIsViewShowing){
            mWm.addView(mFloatMessagesLayout,mLayoutParams);
        }
        mIsViewShowing = true;
        mAdapter.setData(unReadMsgs);
        mAdapter.notifyDataSetChanged();
    }

    private void removeFloatMsgsView() {
        if(mIsViewShowing){
            mWm.removeViewImmediate(mFloatMessagesLayout);
        }
        mIsViewShowing = false;
    }

    public  WindowManager.LayoutParams getWindowManagerParams()
    {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        ;

        params.width = mContext.getResources().getDimensionPixelSize(R.dimen.float_icon_width)
                        + mContext.getResources().getDimensionPixelSize(R.dimen.paddingLeft)
                        + mContext.getResources().getDimensionPixelSize(R.dimen.paddingRight);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSPARENT;
        params.gravity = Gravity.TOP | Gravity.LEFT;

        LogUtils.log(TAG,"params width = "+params.width + " height = "+params.height);

        params.x = 200;
        params.y = 200;
        return params;
    }




    public  interface LuckyMoneyListener {
        public void onLuckyMoneyArrived( ItemInfo info );
        public void onLuckyMoneyRemoved( ItemInfo info );
    }

    private class IconAdapter extends BaseAdapter {
        private List<ItemInfo> mFloatIcons;

        public void setData(List<ItemInfo> items){
            LogUtils.log(TAG,"setData mFloatIcons.size = "+ items.size());
            mFloatIcons = items;
            notifyDataSetChanged();
        }

        private List<ItemInfo> getData(){
            if(mFloatIcons == null){
                mFloatIcons = new ArrayList<ItemInfo>();
            }
            return mFloatIcons;
        }
        @Override
        public int getCount() {
            return getData().size();
        }

        @Override
        public Object getItem(int position) {
            return getData().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LogUtils.log(TAG,"getView");

            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.float_icon_layout,parent,false);
                ViewHolder holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            ItemInfo info = getData().get(position);
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.notificationNumber.setText(String.valueOf(info.messageNumber));
            holder.sender.setText(info.messageSender);
            if(info.instanceId > 0){
                Drawable cloneIcon = mContext.getResources().getDrawable(R.drawable.lucky_money_icon);
                cloneIcon = LuckyMoneyHelper.getInstace().getCloneIcon(cloneIcon);
                holder.icon.setImageDrawable(cloneIcon);
            }else{
                holder.icon.setImageResource(R.drawable.lucky_money_icon);
            }
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.log(TAG,"onClick ");
                    try {
                        getData().get(position).pendingIntent.send();
                        onItemClicked(position);
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            });
            convertView = holder.convertView;
            return convertView;
        }

        private void onItemClicked(int position) {
            Message msg = mHandler.obtainMessage();
            msg.what = MESSAGE_ICON_CLICKED;
            msg.arg1 = position;
            mHandler.sendMessage(msg);
        }


        private  class ViewHolder{
            public ImageView icon;
            public TextView sender;
            public TextView notificationNumber;
            public View convertView;
            public ViewHolder(View v){
                convertView = v;
                icon  = (ImageView) v.findViewById(R.id.icon);
                sender = (TextView) v.findViewById(R.id.msg_sender);
                notificationNumber = (TextView) v.findViewById(R.id.msg_count);
            }

        }
    }
}
