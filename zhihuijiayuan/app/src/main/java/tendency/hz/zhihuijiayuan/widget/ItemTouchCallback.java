package tendency.hz.zhihuijiayuan.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.cjt2325.cameralibrary.util.LogUtil;

import java.util.Collections;

import tendency.hz.zhihuijiayuan.adapter.MainCardRecyclerAdapter;
import tendency.hz.zhihuijiayuan.inter.OnItemDragListener;
import tendency.hz.zhihuijiayuan.units.LogUtils;

/**
 * Author：Li Bin on 2019/11/22 14:37
 * Description：
 */
public class ItemTouchCallback extends ItemTouchHelper.Callback {


    private MainCardRecyclerAdapter mAdapter;
    private OnItemDragListener mOnItemDragListener;

    public ItemTouchCallback(MainCardRecyclerAdapter adapter,OnItemDragListener onItemDragListener) {
        mAdapter = adapter;
        mOnItemDragListener = onItemDragListener;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(((MainCardRecyclerAdapter) mAdapter).getDataList(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(((MainCardRecyclerAdapter) mAdapter).getDataList(), i, i - 1);
            }
        }
        mAdapter.notifyItemMoved(fromPosition, toPosition);
        LogUtils.log("onMove"+fromPosition+","+toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        LogUtils.log("onMoved"+fromPos+","+toPos);
        mOnItemDragListener.onDrag();
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }
}

