package tendency.hz.zhihuijiayuan.widget;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import tendency.hz.zhihuijiayuan.R;

/**
 * Created by JasonYao on 2018/11/22.
 */
public class ClassicsHeader extends LinearLayout implements RefreshHeader {
    private static final String TAG = "ClassicsHeader---";
    private ImageView mImageView;//标题文本

    public ClassicsHeader(Context context) {
        super(context);
        initView(context);
    }

    public ClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public ClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        mImageView = new ImageView(context);
        addView(mImageView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int maxDragHeight) {
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        return 500;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mImageView.setImageResource(R.mipmap.layout_refresh_begin);
                break;
            case Refreshing:
                mImageView.setImageResource(R.mipmap.layout_refresh_ing);
                break;
            case ReleaseToRefresh:
                mImageView.setImageResource(R.mipmap.layout_refresh_begin);
                break;
            case RefreshFinish:
                mImageView.setImageResource(R.mipmap.layout_refresh_finish);
                break;
        }
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int maxDragHeight) {
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {
    }
}
