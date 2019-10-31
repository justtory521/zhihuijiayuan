package tendency.hz.zhihuijiayuan.units;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Author：Libin on 2019/5/15 18:01
 * Email：1993911441@qq.com
 * Describe：rv间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    //间距
    private int mSpace;

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int childCount = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        if (position > 0 && position < childCount) {
            outRect.set(0, 0, 0, mSpace);
        } else {
            outRect.set(0, mSpace, 0, mSpace);
        }
    }
}