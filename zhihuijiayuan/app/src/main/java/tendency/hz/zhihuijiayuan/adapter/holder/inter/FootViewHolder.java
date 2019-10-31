package tendency.hz.zhihuijiayuan.adapter.holder.inter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import tendency.hz.zhihuijiayuan.R;

/**
 * Created by JasonYao on 2018/1/24.
 */

public class FootViewHolder extends RecyclerView.ViewHolder {
    public LinearLayout mLayoutLoading;
    public LinearLayout mLayoutNoMore;

    public FootViewHolder(View itemView) {
        super(itemView);
        mLayoutLoading = itemView.findViewById(R.id.layout_loading);
        mLayoutNoMore = itemView.findViewById(R.id.layout_no_more);
    }
}
