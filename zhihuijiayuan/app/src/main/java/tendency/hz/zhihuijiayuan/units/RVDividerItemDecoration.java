package tendency.hz.zhihuijiayuan.units;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import tendency.hz.zhihuijiayuan.R;

/**
 * Author：Libin on 2019/5/15 15:12
 * Email：1993911441@qq.com
 * Describe：rv分割线
 */
public class RVDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int padding = 0;


    public RVDividerItemDecoration(Context context, int padding) {
        this.mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
        this.padding = padding;
    }



    public RVDividerItemDecoration(Context context, int padding, int drawableId) {
        this.mDivider = ContextCompat.getDrawable(context, drawableId);
        this.padding = padding;
    }


    public RVDividerItemDecoration(Context context) {
        this.mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
    }


    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft() + padding;
        int right = parent.getWidth() - parent.getPaddingRight() - padding;
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount - 1; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + this.mDivider.getIntrinsicHeight();
            this.mDivider.setBounds(left, top, right, bottom);
            this.mDivider.draw(c);
        }
    }

}


