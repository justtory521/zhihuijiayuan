package tendency.hz.zhihuijiayuan.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import tendency.hz.zhihuijiayuan.R;

/**
 * Created by JasonYao on 2018/12/17.
 */
@SuppressLint("AppCompatCustomView")
public class PhoneNumEditText extends AppCompatEditText {
    //上次输入框中的内容
    private String lastString;
    //光标的位置
    private int selectPosition;

    //输入框内容改变监听
    private TextChangeListener listener;

    private Drawable mBtnDel;

    private Context mContext;

    int eventX, eventY;

    private Rect mRect = new Rect();

    public PhoneNumEditText(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public PhoneNumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();

    }

    public PhoneNumEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();

    }

    private void initView() {
        mBtnDel = mContext.getResources().getDrawable(R.mipmap.ic_btn_delete_edt);
        setDrawable();

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            /**
             * 当输入框内容改变时的回调
             * @param s  改变后的字符串
             * @param start 改变之后的光标下标
             * @param before 删除了多少个字符
             * @param count 添加了多少个字符
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                //因为重新排序之后setText的存在
                //会导致输入框的内容从0开始输入，这里是为了避免这种情况产生一系列问题
                if (start == 0 && count > 1 && PhoneNumEditText.this.getSelectionStart() == 0) {
                    return;
                }

                String textTrim = getTextTrim(PhoneNumEditText.this);
                if (TextUtils.isEmpty(textTrim)) {
                    return;
                }

                //如果 before >0 && count == 0,代表此次操作是删除操作
                if (before > 0 && count == 0) {
                    selectPosition = start;
                    if (TextUtils.isEmpty(lastString)) {
                        return;
                    }
                    //将上次的字符串去空格 和 改变之后的字符串去空格 进行比较
                    //如果一致，代表本次操作删除的是空格
                    if (textTrim.equals(lastString.replaceAll(" ", ""))) {
                        //帮助用户删除该删除的字符，而不是空格
                        StringBuilder stringBuilder = new StringBuilder(lastString);
                        stringBuilder.deleteCharAt(start - 1);
                        selectPosition = start - 1;
                        PhoneNumEditText.this.setText(stringBuilder.toString());
                    }
                } else {
                    //此处代表是添加操作
                    //当光标位于空格之前，添加字符时，需要让光标跳过空格，再按照之前的逻辑计算光标位置
                    if ((start + count) % 5 == 0) {
                        selectPosition = start + count + 1;
                    } else {
                        selectPosition = start + count;
                    }
                }
            }


            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
                //获取输入框中的内容,不可以去空格
                String etContent = getText(PhoneNumEditText.this);
                if (TextUtils.isEmpty(etContent)) {
                    if (listener != null) {
                        listener.textChange("");
                    }
                    return;
                }
                //重新拼接字符串
                String newContent = addSpeaceByCredit(etContent);
                //保存本次字符串数据
                lastString = newContent;

                //如果有改变，则重新填充
                //防止EditText无限setText()产生死循环
                if (!newContent.equals(etContent)) {
                    PhoneNumEditText.this.setText(newContent);
                    //保证光标的位置
                    PhoneNumEditText.this.setSelection(newContent.length());
//                    PhoneNumEditText.this.setSelection(selectPosition > newContent.length() ? newContent.length() : selectPosition);
                }
                //触发回调内容
                if (listener != null) {
                    listener.textChange(newContent);
                }

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

    /**
     * 输入框内容回调，当输入框内容改变时会触发
     */
    public interface TextChangeListener {
        void textChange(String text);
    }

    public void setTextChangeListener(TextChangeListener listener) {
        this.listener = listener;
    }

    public String addSpeaceByCredit(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        //去空格
        content = content.replaceAll(" ", "");
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        //卡号限制为16位
        if (content.length() > 11) {
            content = content.substring(0, 11);
        }
        StringBuilder newString = new StringBuilder();
        for (int i = 1; i <= content.length(); i++) {
            //当为第4位时，并且不是最后一个第4位时
            //拼接字符的同时，拼接一个空格
            //如果在最后一个第四位也拼接，会产生空格无法删除的问题
            //因为一删除，马上触发输入框改变监听，又重新生成了空格

            if ((i == 3 || i == 7) && i != content.length()) {
                newString.append(content.charAt(i - 1) + " ");
            } else {
                //如果不是4位的倍数，则直接拼接字符即可
                newString.append(content.charAt(i - 1));

            }
        }
        return newString.toString();
    }

    public String getTextTrim(EditText text) {
        return text.getText().toString().replaceAll(" ", "");
    }

    public String getText(EditText text) {
        return text.getText().toString();
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
