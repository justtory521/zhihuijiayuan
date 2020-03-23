package uk.co.imallan.jellyrefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class JellyRefreshLayout extends PullToRefreshLayout implements PullToRefreshLayout.PullToRefreshPullingListener {

    private JellyLayout mJellyLayout;

    public JellyRefreshLayout(Context context) {
        this(context, null);
    }

    public JellyRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JellyRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        resolveAttributes(attrs);
    }

    private void resolveAttributes(@Nullable AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.JellyRefreshLayout);
        try {
            int color = a.getColor(R.styleable.JellyRefreshLayout_jellyColor, Color.WHITE);
            float height = a.getDimension(R.styleable.JellyRefreshLayout_headerHeight, mHeaderHeight);
            float pullHeight = a.getDimension(R.styleable.JellyRefreshLayout_pullHeight, mPullHeight);
            float triggerHeight = a.getDimension(R.styleable.JellyRefreshLayout_pullHeight, mTriggerHeight);
            mJellyLayout.setColor(color);
            mHeaderHeight = height;
            mPullHeight = pullHeight;
            mTriggerHeight = triggerHeight;
        } finally {
            a.recycle();
        }
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            post(() -> mJellyLayout.setPointX(mJellyLayout.getWidth() / 2));
        }
        super.setRefreshing(refreshing);
    }

    private void init() {
        mJellyLayout = new JellyLayout(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mJellyLayout.setElevation(getElevation());
        }
        setHeaderView(mJellyLayout);
        setPullingListener(this);
    }

    @Override
    protected void onStateChanged(@State int newState) {
        switch (newState) {
            case STATE_REFRESHING:

                break;
            case STATE_REFRESHING_SETTLING:

                break;
            case STATE_DRAGGING:
            case STATE_IDLE:
            case STATE_SETTLING:
            case STATE_RELEASING:

                break;
        }
    }

    @Override
    public void onPulling(float fraction, float pointXPosition) {
        mJellyLayout.setPointX(pointXPosition);
    }

    @Override
    public void onTranslationYChanged(float translationY) {
        switch (getState()) {
            case STATE_DRAGGING:
            case STATE_RELEASING:
                mJellyLayout.mHeaderHeight = Math.min(translationY / 2, mHeaderHeight);
                mJellyLayout.mPullHeight = translationY;
                break;
            case STATE_REFRESHING:
                mJellyLayout.mHeaderHeight = mHeaderHeight;
                mJellyLayout.mPullHeight = mHeaderHeight;
                break;
            case STATE_SETTLING:
                mJellyLayout.mHeaderHeight = translationY;
                mJellyLayout.mPullHeight = translationY;
                break;
            case STATE_REFRESHING_SETTLING:
                mJellyLayout.mHeaderHeight = mHeaderHeight;
                if (translationY > mHeaderHeight) {
                    float dy = translationY - mHeaderHeight;
                    float acceleratedHeight = translationY - 2 * dy;
                    mJellyLayout.mPullHeight = Math.max(mHeaderHeight, acceleratedHeight);
                } else {
                    mJellyLayout.mPullHeight = mHeaderHeight;
                }
                break;
            case STATE_IDLE:
                mJellyLayout.mHeaderHeight = 0;
                mJellyLayout.mPullHeight = 0;
                break;
        }
        mJellyLayout.postInvalidate();
    }
}
