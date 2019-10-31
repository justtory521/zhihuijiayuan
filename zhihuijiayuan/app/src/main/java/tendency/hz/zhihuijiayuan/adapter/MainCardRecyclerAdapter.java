package tendency.hz.zhihuijiayuan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.holder.MainCardItemHolder;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.HomeCardItemOnClickInter;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;

/**
 * Created by JasonYao on 2018/4/12.
 */

public class MainCardRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = "HomeCardRecyclerAdapter--";
    private Context mContext;
    private List<CardItem> mList;
    private LayoutInflater mInflater;

    private HomeCardItemOnClickInter mListener;

    public MainCardRecyclerAdapter(Context context, List<CardItem> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setListener(HomeCardItemOnClickInter listener) {
        mListener = listener;
    }

    @SuppressLint("LongLogTag")
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardItemHolder(mInflater.inflate(R.layout.item_main_card, parent, false), mListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < mList.size()) {
            if (!FormatUtils.getInstance().isEmpty(mList.get(position).getPoster())) {
                ((MainCardItemHolder) holder).mSimpleDraweeView.setImageURI(mList.get(position).getPoster());
            }

            if (!FormatUtils.getInstance().isEmpty(mList.get(position).getPosterUrl())) {
                ((MainCardItemHolder) holder).mSimpleDraweeView.setImageURI(mList.get(position).getPosterUrl());
            }

            if (!FormatUtils.getInstance().isEmpty(mList.get(position).getLogo())) {
                ((MainCardItemHolder) holder).mSimpleDraweeViewPortrait.setImageURI(mList.get(position).getLogo());
            }

            if (!FormatUtils.getInstance().isEmpty(mList.get(position).getLogoUrl())) {
                ((MainCardItemHolder) holder).mSimpleDraweeViewPortrait.setImageURI(mList.get(position).getLogoUrl());
            }

            ((MainCardItemHolder) holder).mTextViewName.setText(mList.get(position).getTitle());

            if (position == 0) {
                ((MainCardItemHolder) holder).marginParams.setMargins(0, 20, 0, 0);
            } else if (position == mList.size() - 1) {
                ((MainCardItemHolder) holder).marginParams.setMargins(0, 0, 0, 160);
            } else {
                ((MainCardItemHolder) holder).marginParams.setMargins(0, 0, 0, 0);
            }
            ((MainCardItemHolder) holder).marginParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            ((MainCardItemHolder) holder).mLayoutParams.height = ViewUnits.getInstance().dp2px(mContext, 120);
            ((MainCardItemHolder) holder).mLayoutCard.setLayoutParams(((MainCardItemHolder) holder).marginParams);

        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
