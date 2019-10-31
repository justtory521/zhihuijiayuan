package tendency.hz.zhihuijiayuan.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.HomeCardItemOnClickInter;

/**
 * Created by JasonYao on 2018/4/12.
 */

public class MainCardItemHolder extends RecyclerView.ViewHolder {
    public TextView mTextViewName;
    public SimpleDraweeView mSimpleDraweeView, mSimpleDraweeViewPortrait;
    public LinearLayout mLayoutCard;
    public ViewGroup.LayoutParams mLayoutParams;
    public ViewGroup.MarginLayoutParams marginParams = null;

    public MainCardItemHolder(View itemView, HomeCardItemOnClickInter homeCardItemOnClickInter) {
        super(itemView);
        mTextViewName = itemView.findViewById(R.id.text_card_name_main);
        mSimpleDraweeView = itemView.findViewById(R.id.img_card_main);
        mSimpleDraweeViewPortrait = itemView.findViewById(R.id.ic_portrait);
        mLayoutCard = itemView.findViewById(R.id.layout_card);
        mLayoutParams = mLayoutCard.getLayoutParams();

        if (mLayoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) mLayoutParams;
        } else {
            //不存在时创建一个新的参数
            //基于View本身原有的布局参数对象
            marginParams = new ViewGroup.MarginLayoutParams(mLayoutParams);
        }

        mSimpleDraweeView.setOnClickListener(view -> homeCardItemOnClickInter.onItemOnClick(itemView, getAdapterPosition()));
    }
}
