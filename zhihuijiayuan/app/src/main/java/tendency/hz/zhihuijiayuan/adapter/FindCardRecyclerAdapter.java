package tendency.hz.zhihuijiayuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.FootViewHolder;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.SearchCardItemOnClickInter;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.base.What;
import tendency.hz.zhihuijiayuan.units.FormatUtils;


/**
 * Created by JasonYao on 2018/3/20.
 */

public class FindCardRecyclerAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<CardItem> mList;
    private LayoutInflater mInflater;

    private SearchCardItemOnClickInter mListener;

    private boolean mHasMore = false;

    public FindCardRecyclerAdapter(Context context, List<CardItem> list) {
        this.mContext = context;
        this.mList = list;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == What.RecyclerItemType.typeItem) {
            return new FindRecyclerViewHolder(mInflater.inflate(R.layout.item_find_card_recycler, parent, false), mListener);
        } else if (viewType == What.RecyclerItemType.typeFooter) {
            return new FootViewHolder(mInflater.inflate(R.layout.layout_loading, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FindRecyclerViewHolder) {
            ((FindRecyclerViewHolder) holder).mTextViewName.setText(mList.get(position).getTitle());
            ((FindRecyclerViewHolder) holder).mImageCard.setImageURI(FormatUtils.getInstance().isEmpty(mList.get(position).getPoster()) ? mList.get(position).getPosterUrl() : mList.get(position).getPoster());
            ((FindRecyclerViewHolder) holder).mTextViewSubTitle.setText(FormatUtils.getInstance().isEmpty(mList.get(position).getSubTitle()) ? "副标题" : mList.get(position).getSubTitle());
            ((FindRecyclerViewHolder) holder).mTextViewCardTitle.setText(mList.get(position).getTitle());
            ((FindRecyclerViewHolder) holder).mHeadImg.setImageURI(FormatUtils.getInstance().isEmpty(mList.get(position).getLogo()) ? mList.get(position).getLogoUrl() : mList.get(position).getLogo());
        }

        if (holder instanceof FootViewHolder) {
            if (mHasMore) {
                ((FootViewHolder) holder).mLayoutNoMore.setVisibility(View.GONE);
                ((FootViewHolder) holder).mLayoutLoading.setVisibility(View.VISIBLE);
            } else {
                ((FootViewHolder) holder).mLayoutNoMore.setVisibility(View.VISIBLE);
                ((FootViewHolder) holder).mLayoutLoading.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() == 0 ? 0 : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return What.RecyclerItemType.typeFooter;
        } else {
            return What.RecyclerItemType.typeItem;
        }
    }

    public void setHasMore(boolean hasMore) {
        mHasMore = hasMore;
    }

    public void setListener(SearchCardItemOnClickInter listener) {
        mListener = listener;
    }


    public class FindRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public SimpleDraweeView mImageCard,mHeadImg;
        public TextView mTextViewName, mTextViewSubTitle,mTextViewCardTitle;
        public LinearLayout mLayoutQrCode, mLayoutShare;
        public LinearLayout mLayoutCardItem;

        public SearchCardItemOnClickInter listener;

        public FindRecyclerViewHolder(View itemView, SearchCardItemOnClickInter findCardItemOnClickInter) {
            super(itemView);

            mImageCard = itemView.findViewById(R.id.img_card);
            mTextViewName = itemView.findViewById(R.id.text_card_name);
            mLayoutCardItem = itemView.findViewById(R.id.layout_card_item);
            mTextViewSubTitle = itemView.findViewById(R.id.text_subtitle);
            mLayoutQrCode = itemView.findViewById(R.id.layout_qrcode);
            mLayoutShare = itemView.findViewById(R.id.layout_share);
            mHeadImg = itemView.findViewById(R.id.ic_portrait);
            mTextViewCardTitle = itemView.findViewById(R.id.text_card_title);

            listener = findCardItemOnClickInter;

            mLayoutCardItem.setOnClickListener(this);
            mLayoutShare.setOnClickListener(this);
            mLayoutQrCode.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_card_item:
                    listener.onItemOnClick(view, getAdapterPosition());
                    break;
                case R.id.layout_share:
                    listener.onShareOnClick(view, getAdapterPosition());
                    break;
                case R.id.layout_qrcode:
                    listener.onQrCodeOnClick(view, getAdapterPosition());
                    break;
            }
        }
    }
}
