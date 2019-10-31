package tendency.hz.zhihuijiayuan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.inter.RecyclerOnClickInter;

/**
 * Created by JasonYao on 2019/1/7.
 */
public class HotSreachRecyclerAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<CardItem> mList;
    private RecyclerOnClickInter mListener;

    public HotSreachRecyclerAdapter(Context mContext, List<CardItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(mInflater.inflate(R.layout.item_hot_sreach, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((MyViewHolder) viewHolder).mTextView.setText(mList.get(i).getTitle());
        if (mList.get(i).getIndex().equals("0") || mList.get(i).getIndex().equals("1") || mList.get(i).getIndex().equals("2")) {
            ((MyViewHolder) viewHolder).mImageView.setVisibility(View.VISIBLE);
        } else {
            ((MyViewHolder) viewHolder).mImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setListener(RecyclerOnClickInter recyclerOnClickInter) {
        mListener = recyclerOnClickInter;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ImageView mImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_hot_sreach);
            mImageView = itemView.findViewById(R.id.ic_hot_sreach);

            mTextView.setOnClickListener(view -> mListener.onItemOnClick(view, getAdapterPosition()));
        }
    }
}
