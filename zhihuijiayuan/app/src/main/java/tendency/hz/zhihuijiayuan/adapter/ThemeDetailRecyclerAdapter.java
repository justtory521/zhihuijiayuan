package tendency.hz.zhihuijiayuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.ThemeRecyclerOnClickInter;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.units.FormatUtils;

/**
 * Created by JasonYao on 2018/10/30.
 */
public class ThemeDetailRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ThemeDetail---";
    private List<CardItem> mList;
    private Context mContext;

    private LayoutInflater mInflater;

    private ThemeRecyclerOnClickInter mListener;

    public ThemeDetailRecyclerAdapter(List<CardItem> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.layout_choice_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).simpleDraweeView.setImageURI(mList.get(position).getPosterUrl());
        ((MyViewHolder) holder).mTextViewTitle.setText(mList.get(position).getTitle());
        ((MyViewHolder) holder).mTextViewSubTitle.setText(!FormatUtils.getInstance().isEmpty(mList.get(position).getSubTitle()) ?
                mList.get(position).getSubTitle() : "副标题");
        ((MyViewHolder) holder).mTextViewTitleInside.setText(mList.get(position).getTitle());
        ((MyViewHolder) holder).mSimpleDraweeViewHeadImg.setImageURI(FormatUtils.getInstance().isEmpty(mList.get(position).getLogo()) ?
                mList.get(position).getLogoUrl() : mList.get(position).getLogo());
        if (position == 0) {
            ((MyViewHolder) holder).marginParams.setMargins(12, 0, 0, 0);
            ((MyViewHolder) holder).mLayoutCard.setLayoutParams(((MyViewHolder) holder).marginParams);
        }
        if (position == mList.size() - 1 && mList.size() > 2) {
            ((MyViewHolder) holder).marginParams.setMargins(0, 0, 12, 0);
            ((MyViewHolder) holder).mLayoutCard.setLayoutParams(((MyViewHolder) holder).marginParams);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setLisntener(ThemeRecyclerOnClickInter recyclerOnClickInter) {
        mListener = recyclerOnClickInter;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView simpleDraweeView, mSimpleDraweeViewHeadImg;
        private TextView mTextViewTitle, mTextViewSubTitle, mTextViewTitleInside;
        public ViewGroup.LayoutParams mLayoutParams;
        public ViewGroup.MarginLayoutParams marginParams = null;
        private LinearLayout mLayoutCard;

        public MyViewHolder(View itemView) {
            super(itemView);
            simpleDraweeView = itemView.findViewById(R.id.card_bg);
            mTextViewTitle = itemView.findViewById(R.id.text_title);
            mTextViewSubTitle = itemView.findViewById(R.id.text_subtitle);
            mSimpleDraweeViewHeadImg = itemView.findViewById(R.id.ic_portrait);
            mTextViewTitleInside = itemView.findViewById(R.id.text_card_title);
            mLayoutCard = itemView.findViewById(R.id.layout_card);
            mLayoutParams = mLayoutCard.getLayoutParams();
            if (mLayoutParams instanceof ViewGroup.MarginLayoutParams) {
                marginParams = (ViewGroup.MarginLayoutParams) mLayoutParams;
            } else {
                //不存在时创建一个新的参数
                //基于View本身原有的布局参数对象
                marginParams = new ViewGroup.MarginLayoutParams(mLayoutParams);
            }

            simpleDraweeView.setOnClickListener(view -> {
                Log.e(TAG, getAdapterPosition() + "");
                Log.e(TAG, "list大小为：" + mList.size());
                mListener.onItemOnClick(mList.get(getAdapterPosition()));
            });
        }
    }
}
