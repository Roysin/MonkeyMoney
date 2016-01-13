package org.roysin.luckymoney.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import org.roysin.luckymoney.view.ItemInfo;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class LuckyMoneyHelper {
	private final String TAG = "LuckyMoneyHelper";
	public  final String KEY_WECHAT_LUCKY_MONEY = "MainUI_User_Last_Msg_Type";
	public  final int MSG_TYPE_WECHAT_LUCKY_MONEY = 0x1A000031; //436207665
	private static LuckyMoneyHelper instance = null;

	public static LuckyMoneyHelper getInstace(){
		if(instance == null){
			instance = new LuckyMoneyHelper();
		}
		return instance;
	}
	
	public  boolean isLuckyMoney(PendingIntent pi){
		printExtrasLog(pi);
		boolean result = false;
		String packageName = null;
		if(pi != null && (packageName=pi.getCreatorPackage()) !=null ){
			Bundle extras = null;
			try {
				Method getIntent = pi.getClass().getMethod("getIntent");
				Intent intent  = (Intent)getIntent.invoke(pi);
				extras = intent.getExtras();
			}catch (Exception e) {
				e.printStackTrace();
			}
			if ("com.tencent.mm".equals(packageName) && extras != null){
				result = (extras.getInt(KEY_WECHAT_LUCKY_MONEY) == MSG_TYPE_WECHAT_LUCKY_MONEY);
			}
		}
		
		LogUtils.log(TAG, "isLuckyMoney package name = " + packageName + " isLuckyMoney = " + result);
		return result;
	}
	
	
	
	private  void printExtrasLog(PendingIntent pi) {
		if (pi == null) {
			return;
		}
		Bundle extras = null;
		if (pi != null) {
			try {
				Method getIntent = pi.getClass().getMethod("getIntent");
				Intent intent = (Intent) getIntent.invoke(pi);
				extras = intent.getExtras();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LogUtils.log(TAG, " printExtrasLog begin =============================");

		if (extras == null) {
			return;
		}
		Iterator<String> it = extras.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			LogUtils.log(TAG, " extras :key = " + key + " value = " + extras.get(key));
		}
		LogUtils.log(TAG, " printExtrasLog end =============================");

	}

	public Drawable getCloneIcon(Drawable drawable){
		return drawable;
	}
	
	private class Rule{
		public static final int RULE_MESSAGE_TYPE = 0;
		public static final int RULE_PREFIX_STRING = 1;
		public static final int RULE_SURFIX_STRING = 2;
		public String packageName;
		public int value;
		public String key;
		public String prefix;
		public String surfix;
	}

	public ItemInfo getItemFromList(List<ItemInfo> list, String sender, int instanceId){
		ItemInfo result = null;
		if(list != null && sender != null){
			for(ItemInfo info :list){
				if(sender.equals(info.messageSender)
						&& instanceId == info.instanceId){
					result = info;
					break;
				}
			}
		}
		LogUtils.log(TAG,"getItemFromList result = " + result);
		return result;
	}

	public int dip2px(Context context, float dipValue)
	{
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public int px2dip(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	/**
	 * 将px值转换为sp值，保证文字大小不变
	 *
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public int px2sp(Context context, float pxValue) {

		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}
	/**
	 * drawble2bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

}
