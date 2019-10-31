package tendency.hz.zhihuijiayuan.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.HomeCardItemOnClickInter;

/**
 * Created by JasonYao on 2018/4/12.
 */

public class MainMoreItemHolder extends RecyclerView.ViewHolder {
    private ImageView mTextViewAdd;

    public MainMoreItemHolder(View itemView, HomeCardItemOnClickInter homeCardItemOnClickInter) {
        super(itemView);
        mTextViewAdd = itemView.findViewById(R.id.text_add_more);

        mTextViewAdd.setOnClickListener(view -> homeCardItemOnClickInter.addMoreCardOnClick(view, getAdapterPosition()));
    }
}
