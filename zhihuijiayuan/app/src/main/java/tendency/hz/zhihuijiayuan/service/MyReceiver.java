package tendency.hz.zhihuijiayuan.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.cjt2325.cameralibrary.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import tendency.hz.zhihuijiayuan.MainActivity;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.Card;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.LogUtils;
import tendency.hz.zhihuijiayuan.units.MediaUtils;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "libin";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
//            Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));


        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            if (CardContentActivity.getInstance() != null) {
                String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                String functionName = null;
                try {
                    JSONObject jsonObject = new JSONObject(msg);
                    functionName = jsonObject.getString("FinalFuncName");
                    int cardId = jsonObject.getInt("CardId");
                    if (cardId == 0 || cardId == Integer.valueOf(CardContentActivity.getInstance().getCardId())) {
                        CardContentActivity.getInstance().callBackResult(functionName, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (functionName != null) {
                        CardContentActivity.getInstance().callBackResult(functionName, msg);
                    }

                }

            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notificationId);
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知" + bundle.getString(JPushInterface.EXTRA_EXTRA));


            try {
                JSONObject jsonObject = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                String sound = jsonObject.getString("Sound");
                if (!TextUtils.isEmpty(sound)) {
                    int raw = FormatUtils.getInstance().getResource("raw", sound);
                    if (raw != 0) {
                        MediaUtils.getInstance().playAudio(raw);
                    } else {
                        MediaUtils.getInstance().playAudio(R.raw.sound1);
                    }

                } else {
                    MediaUtils.getInstance().playAudio(R.raw.sound1);
                }
            } catch (JSONException e) {
                MediaUtils.getInstance().playAudio(R.raw.sound1);
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_EXTRA));

            MediaUtils.getInstance().stopAudio();
            jump(context, bundle.getString(JPushInterface.EXTRA_EXTRA));
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }

    }


    private void jump(Context context, String result) {
        //打开自定义的Activity
        Intent i = new Intent();

        if (TextUtils.isEmpty(result)) {
            i.setClass(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(i);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(result);
                i.putExtra("cardId", jsonObject.getString("CardID"));
                i.putExtra("url", jsonObject.getString("Url"));
                Log.e(TAG, jsonObject.getString("CardID"));
                Log.e(TAG, jsonObject.getString("Url"));

                if (FormatUtils.getInstance().isEmpty(jsonObject.getString("Url"))) {
                    i.setClass(context, MainActivity.class);
                    i.putExtra("type", Request.StartActivityRspCode.PUSH_TOMESSAGELIST_JUMP);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } else {
                    if (CardContentActivity.isRunning) {
                        i.setAction(Request.Broadcast.RELOADURL);
                        i.putExtra("id", jsonObject.getString("CardID"));
                        i.putExtra("url", jsonObject.getString("Url"));
                        context.sendBroadcast(i);
                    } else {
                        CardItem cardItem = new CardItem();
                        cardItem.setCardID(jsonObject.getString("CardID"));
                        cardItem.setCardUrl(jsonObject.getString("Url"));
                        i.setClass(context, CardContentActivity.class);
                        i.putExtra("cardItem", cardItem);
                        i.putExtra("type", Request.StartActivityRspCode.PUSH_CARDCONTENT_JUMP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

}

