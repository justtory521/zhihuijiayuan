package tendency.hz.zhihuijiayuan.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author：Li Bin on 2019/11/22 16:44
 * Description：首页卡片间距
 */
public class CradItemDecoration extends RecyclerView.ItemDecoration {
    //间距
    private int mSpace;

    public CradItemDecoration(int space) {
        this.mSpace = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childCount = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        if (childCount == 1) {
            outRect.set(0, mSpace, 0, mSpace);
        } else {
            if (position == 0) {
                outRect.set(0, mSpace, 0, 0);
            } else if (position == childCount - 1) {
                outRect.set(0, 0, 0, mSpace);
            }
        }

    }
}
