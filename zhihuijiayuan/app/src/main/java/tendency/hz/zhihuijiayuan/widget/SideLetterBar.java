package tendency.hz.zhihuijiayuan.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.City;
import tendency.hz.zhihuijiayuan.units.FormatUtils;

/**
 * Created by JasonYao on 2017/6/16.
 */

public class SideLetterBar extends View {
    private static final String TAG = "SideLetterBar---";
    private static final String[] b = {"定位", "热门", "A", "B", "C", "D", "E", "F", "G", "H","J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "W", "X", "Y", "Z"};
    private int choose = -1;
    private Paint paint = new Paint();
    private boolean showBg = false;
    private OnLetterChangedListener onLetterChangedListener;
    private TextView overlay;

    public SideLetterBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideLetterBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideLetterBar(Context context) {
        super(context);
    }

    /**
     * 设置悬浮的textview
     *
     * @param overlay
     */
    public void setOverlay(TextView overlay) {
        this.overlay = overlay;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBg) {
            canvas.drawColor(Color.TRANSPARENT);
        }

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;
        for (int i = 0; i < b.length; i++) {
            paint.setTextSize(getResources().getDimension(R.dimen.side_letter_bar_letter_size));
            paint.setColor(getResources().getColor(R.color.colorTextGray));
            paint.setAntiAlias(true);
            if (i == choose) {
                paint.setColor(getResources().getColor(R.color.colorTextYellow));
                paint.setFakeBoldText(true);  //加粗
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnLetterChangedListener listener = onLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "ACTION_DOWN");
                showBg = true;
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.length) {
                        listener.onLetterChanged(b[c]);
                        choose = c;
                        invalidate();
                        if (overlay != null) {
                            overlay.setVisibility(VISIBLE);
                            overlay.setText(b[c]);
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.length) {
                        listener.onLetterChanged(b[c]);
                        choose = c;
                        invalidate();
                        if (overlay != null) {
                            overlay.setVisibility(VISIBLE);
                            overlay.setText(b[c]);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                showBg = false;
                choose = c;
                invalidate();
                if (overlay != null) {
                    overlay.setVisibility(GONE);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "ACTION_UP");
                showBg = false;
                choose = c;
                invalidate();
                if (overlay != null) {
                    overlay.setVisibility(GONE);
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setSelectCityIndex(City cityIndex) {
        if (cityIndex.getName().equals("当前")) {
            choose = -1;
            invalidate();
            return;
        }

        if (cityIndex.getName().equals("定位")) {
            choose = 0;
            invalidate();
            return;
        }

        if (cityIndex.getName().equals("热门")) {
            choose = 1;
            invalidate();
            return;
        }

        String index = FormatUtils.getInstance().getPinYin(cityIndex.getName()).substring(0, 1).toUpperCase();
        for (int i = 0; i < b.length; i++) {
            if (b[i].equals(index)) {
                choose = i;
                invalidate();
            }
        }
    }

    public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
        this.onLetterChangedListener = onLetterChangedListener;
    }

    public interface OnLetterChangedListener {
        void onLetterChanged(String letter);
    }
}
