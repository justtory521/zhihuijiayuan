package tendency.hz.zhihuijiayuan.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import tendency.hz.zhihuijiayuan.R;

/**
 * Created by Administrator on 2016/11/17.
 * 带删除按钮的exitText
 */

public class EditTextWithDel extends  android.support.v7.widget.AppCompatEditText{
    private Drawable mBtnDel;

    private Context mContext;

    int eventX, eventY;

    private Rect mRect = new Rect();

    public EditTextWithDel(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mBtnDel = mContext.getResources().getDrawable(R.mipmap.ic_btn_delete_edt);
        setDrawable();
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setDrawable();
            }
        });
    }

    private void setDrawable() {
        if (length() > 0) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, mBtnDel, null);
            setCompoundDrawablePadding(10);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mBtnDel != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            eventX = (int) event.getRawX();
            eventY = (int) event.getRawY();

            getGlobalVisibleRect(mRect);
            mRect.left = mRect.right - 50;
            if (mRect.contains(eventX, eventY)) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
