package tendency.hz.zhihuijiayuan.widget;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author：Libin on 2019/5/16 08:54
 * Email：1993911441@qq.com
 * Describe：
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public GridItemDecoration(int mSpace) {
        this.mSpace = mSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        int childPosition = parent.getChildAdapterPosition(view);
        if (manager != null) {
            if (manager instanceof GridLayoutManager) {
                // manager为GridLayoutManager时
                setGridOffset(((GridLayoutManager) manager).getOrientation(), ((GridLayoutManager) manager).getSpanCount(), outRect, childPosition);
            }
        }

    }


    private void setGridOffset(int orientation, int spanCount, Rect outRect, int childPosition) {
        float totalSpace = mSpace * (spanCount + 1);
        float eachSpace = totalSpace / spanCount;
        int column = childPosition % spanCount;

        float left;
        float right;
        float top;
        float bottom;
        if (orientation == GridLayoutManager.VERTICAL) {
            top = 0;
            bottom = mSpace;

            if (childPosition < spanCount) {
                top = mSpace;
            }

            if (spanCount == 1) {
                left = mSpace;
                right = left;
            } else {
                left = column * (eachSpace - mSpace * 2) / (spanCount - 1) + mSpace;
                right = eachSpace - left;
            }
        } else {
            left = 0;
            right = mSpace;

            if (childPosition < spanCount) {
                left = mSpace;
            }

            if (spanCount == 1) {
                top = mSpace;
                bottom = top;
            } else {
                top = column * (eachSpace - mSpace * 2) / (spanCount - 1) + mSpace;
                bottom = eachSpace - top;
            }
        }

        outRect.set((int) left, (int) top, (int) right, (int) bottom);
    }
}
