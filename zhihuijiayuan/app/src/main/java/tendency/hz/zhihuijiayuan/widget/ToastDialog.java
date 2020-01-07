package tendency.hz.zhihuijiayuan.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import tendency.hz.zhihuijiayuan.R;


/**
 * Author：Libin on 2020/1/3 0003 11:19
 * Email：1993911441@qq.com
 * Describe：
 */
public class ToastDialog extends Dialog {
    public ToastDialog(Context context) {
        super(context);
    }

    public ToastDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public static class Builder {
        private Context context;
        private String message;
        private boolean isCancelOutside = true;
        private boolean cancelable = true;
        private int position = 0;
        private int resource;
        private String textColor = "#FFFFFF";
        private String bgColor = "#666666";
        private int type;


        public Builder(Context context) {
            this.context = context;
        }

        public ToastDialog.Builder setType(int type) {
            this.type = type;
            return this;
        }

        public ToastDialog.Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public ToastDialog.Builder setBgColor(String bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public ToastDialog.Builder  setTextColor(String textColor) {
            this.textColor = textColor;
            return this;
        }

        public ToastDialog.Builder  setResource(int resource) {
            this.resource = resource;
            return this;
        }


        public ToastDialog.Builder setPosition(int position) {
            this.position = position;
            return this;
        }

        public ToastDialog.Builder setMessage(String message) {
            this.message = message;
            return this;
        }


        public ToastDialog.Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        public ToastDialog create() {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            View view = inflater.inflate(R.layout.layout_toast, (ViewGroup) null);
            ToastDialog loadingDialog = new ToastDialog(this.context, R.style.ToastDialogStyle);
            TextView msgText = (TextView) view.findViewById(R.id.tv_toast);
            ImageView img = view.findViewById(R.id.iv_toast);
            ProgressBar progressBar = view.findViewById(R.id.pb_loading);
            CardView cardView = view.findViewById(R.id.cv_toast);


            if (!TextUtils.isEmpty(this.message)) {
                msgText.setVisibility(View.VISIBLE);
                msgText.setText(this.message);
            } else {
                msgText.setVisibility(View.GONE);
            }

            msgText.setTextColor(Color.parseColor(textColor));
            cardView.setCardBackgroundColor(Color.parseColor(bgColor));

            if (this.type ==1){
                if (this.resource != 0){
                    img.setVisibility(View.VISIBLE);
                    Glide.with(context).load(resource).into(img);
                }else {
                    img.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }else if (this.type == 2){
                img.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            Window window = loadingDialog.getWindow();
            WindowManager.LayoutParams lp;
            if (window != null) {
                lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;//宽高可设置具体大小
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                switch (position){
                    case 0:
                        window.setGravity(Gravity.CENTER);
                        break;
                    case 1:
                        window.setGravity(Gravity.TOP);
                        lp.y = 200;
                        break;
                    case 2:
                        window.setGravity(Gravity.BOTTOM);
                        lp.y = 200;
                        break;
                }

                window.setAttributes(lp);

                if (this.isCancelOutside){
                    window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                }
            }

            loadingDialog.setCanceledOnTouchOutside(this.isCancelOutside);
            loadingDialog.setContentView(view);
            loadingDialog.setCancelable(this.cancelable);
            return loadingDialog;
        }
    }
}
