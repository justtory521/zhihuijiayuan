package tendency.hz.zhihuijiayuan.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    public RelativeLayout rlDeleteCard;

    public MainCardItemHolder(View itemView, HomeCardItemOnClickInter homeCardItemOnClickInter) {
        super(itemView);
        mTextViewName = itemView.findViewById(R.id.text_card_name_main);
        mSimpleDraweeView = itemView.findViewById(R.id.img_card_main);
        mSimpleDraweeViewPortrait = itemView.findViewById(R.id.ic_portrait);
        rlDeleteCard = itemView.findViewById(R.id.rl_delete_card);



        mSimpleDraweeView.setOnClickListener(view -> homeCardItemOnClickInter.onItemOnClick(itemView, getAdapterPosition()));
        rlDeleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeCardItemOnClickInter.deleteCrad(getAdapterPosition());
            }
        });
    }
}
