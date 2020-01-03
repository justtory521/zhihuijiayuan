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
import tendency.hz.zhihuijiayuan.bean.base.Uri;
import tendency.hz.zhihuijiayuan.units.BaseUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.LogUtils;
import tendency.hz.zhihuijiayuan.units.MediaUtils;
import tendency.hz.zhihuijiayuan.units.SPUtils;
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

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

            if (BaseUnits.getInstance().isActivityRunning("tendency.hz.zhihuijiayuan.view.card.CardContentActivity")) {
                String msg = null;
                if (bundle != null) {
                    msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                }
                LogUtils.log("透传："+msg);
                String functionName = null;
                try {
                    JSONObject jsonObject = new JSONObject(msg);
                    functionName = jsonObject.getString("FinalFuncName");
                    int cardId = jsonObject.getInt("CardId");
                    //当前页面卡片id
                    String currentCardID = (String) SPUtils.getInstance().get(SPUtils.FILE_CARD, SPUtils.cardID, "");
                    if (cardId == 0 || String.valueOf(cardId).equals(currentCardID)) {
                        Intent i = new Intent();
                        i.setAction(Request.Broadcast.JG_PUSH);
                        i.putExtra("functionName", functionName);
                        i.putExtra("msg", msg);
                        context.sendBroadcast(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (functionName != null) {
                        Intent i = new Intent();
                        i.setAction(Request.Broadcast.JG_PUSH);
                        i.putExtra("functionName", functionName);
                        i.putExtra("msg", msg);
                        context.sendBroadcast(i);
                    }

                }
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            try {
                if (bundle != null) {
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
                }

            } catch (JSONException e) {
                MediaUtils.getInstance().playAudio(R.raw.sound1);
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {


            MediaUtils.getInstance().stopAudio();
            if (bundle != null) {
                jump(context, bundle.getString(JPushInterface.EXTRA_EXTRA));
            }
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
                    if (BaseUnits.getInstance().isActivityRunning("tendency.hz.zhihuijiayuan.view.card.CardContentActivity")) {
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

