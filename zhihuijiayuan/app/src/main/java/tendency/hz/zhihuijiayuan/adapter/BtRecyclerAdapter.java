package tendency.hz.zhihuijiayuan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.Bt;
import tendency.hz.zhihuijiayuan.inter.RecyclerOnClickInter;

/**
 * Created by JasonYao on 2019/3/6.
 */
public class BtRecyclerAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Bt> mBts;
    private LayoutInflater mInflater;
    private RecyclerOnClickInter mListener;

    public BtRecyclerAdapter(Context context, List<Bt> bts) {
        mContext = context;
        mBts = bts;

        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BtRecyclerViewHolder(mInflater.inflate(R.layout.item_bt_recycler, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((BtRecyclerViewHolder) viewHolder).mTextViewName.setText(mBts.get(i).getName());
        ((BtRecyclerViewHolder) viewHolder).mTextViewAddress.setText(mBts.get(i).getAddress());
    }

    @Override
    public int getItemCount() {
        return mBts.size();
    }

    public void setListener(RecyclerOnClickInter listener) {
        mListener = listener;
    }

    private class BtRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewName;
        public TextView mTextViewAddress;

        public BtRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewName = itemView.findViewById(R.id.text_name);
            mTextViewAddress = itemView.findViewById(R.id.text_address);

            mTextViewName.setOnClickListener(view -> mListener.onItemOnClick(view, getAdapterPosition()));
        }
    }
}
