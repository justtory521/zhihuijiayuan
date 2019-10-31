package tendency.hz.zhihuijiayuan.handlers;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.view.card.DevelopmentCardActivity;
import tendency.hz.zhihuijiayuan.view.picker.CityPickerActivity;

/**
 * Created by JasonYao on 2018/6/28.
 */
public class MainAcitivityHandler {

    /**
     * 跳转至开发者页面
     *
     * @param view
     */
    public void jumpToDevelopment(View view) {
        Intent intent = new Intent(view.getContext(), DevelopmentCardActivity.class);
        view.getContext().startActivity(intent);
    }

    /**
     * 跳转至城市选择页面
     *
     * @param view
     */
    public void jumpToCityPicker(View view) {
        Intent intent = new Intent(view.getContext(), CityPickerActivity.class);
        intent.putExtra("city", ((TextView) view).getText().toString());
        ((Activity) view.getContext()).startActivityForResult(intent, Request.StartActivityRspCode.GOTO_CITYPICKER);
    }
}
