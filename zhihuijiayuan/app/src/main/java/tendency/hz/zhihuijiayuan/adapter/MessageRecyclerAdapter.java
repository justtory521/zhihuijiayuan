package tendency.hz.zhihuijiayuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.MessageItemOnCheckInter;
import tendency.hz.zhihuijiayuan.bean.Message;
import tendency.hz.zhihuijiayuan.inter.RecyclerOnClickInter;
import tendency.hz.zhihuijiayuan.units.CacheUnits;

/**
 * Created by JasonYao on 2018/11/19.
 */
public class MessageRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = "MessageRecycler---";
    private Context mContext;
    private List<Message> mMessageLists;
    private LayoutInflater mInflater;
    private boolean showCheckBox = false; //标记是否显示checkbox
    private MessageItemOnCheckInter mListener;

    private RecyclerOnClickInter mOnClickListener;

    public MessageRecyclerAdapter(Context mContext, List<Message> mMessageLists) {
        this.mContext = mContext;
        this.mMessageLists = mMessageLists;

        this.mMessageLists = mMessageLists;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.item_message_recycler, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, mMessageLists.get(position).toString());
        ((MyViewHolder) holder).mTextViewTitle.setText(mMessageLists.get(position).getCardName());
        ((MyViewHolder) holder).mTextViewDate.setText(mMessageLists.get(position).getDateTime());
        ((MyViewHolder) holder).mTextViewMessage.setText(mMessageLists.get(position).getContent());
        ((MyViewHolder) holder).mIcon.setImageURI(mMessageLists.get(position).getCardLogoUrl());
        if (showCheckBox) {
            ((MyViewHolder) holder).mCheckBox.setVisibility(View.VISIBLE);
        } else {
            ((MyViewHolder) holder).mCheckBox.setVisibility(View.GONE);
        }

        ((MyViewHolder) holder).mCheckBox.setChecked(mMessageLists.get(position).isChecked());

    }

    @Override
    public int getItemCount() {
        return mMessageLists.size();
    }

    public void setIsShow(boolean isShow) {
        showCheckBox = isShow;
        notifyDataSetChanged();
    }

    public void setAllSelected(boolean allSelected) {
        for (Message message : mMessageLists) {
            if (allSelected) {
                message.setChecked(true);
            } else {
                message.setChecked(false);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 删除所选条目
     */
    public void remove() {
        for (int i = 0; i < mMessageLists.size(); i++) {
            if (mMessageLists.get(i).isChecked()) {
                int index = mMessageLists.indexOf(mMessageLists.get(i));
                mMessageLists.remove(mMessageLists.get(i));
                i--;  //index需要自剪1，避免ConcurrentModificationException 异常
                notifyItemRemoved(index);
            }
        }
        CacheUnits.getInstance().clearMessage();
        CacheUnits.getInstance().insertMessage(mMessageLists);
    }

    public void setLitener(MessageItemOnCheckInter litener) {
        mListener = litener;
    }

    public void setOnClickListener(RecyclerOnClickInter listener) {
        mOnClickListener = listener;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewTitle, mTextViewDate, mTextViewMessage;
        public SimpleDraweeView mIcon;
        public CheckBox mCheckBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextViewTitle = itemView.findViewById(R.id.text_title);
            mTextViewDate = itemView.findViewById(R.id.text_date);
            mTextViewMessage = itemView.findViewById(R.id.text_msg);
            mIcon = itemView.findViewById(R.id.img_icon);
            mCheckBox = itemView.findViewById(R.id.checkbox_msg);

            mCheckBox.setOnClickListener(view -> {
                mMessageLists.get(getAdapterPosition()).setChecked(!mMessageLists.get(getAdapterPosition()).isChecked());
                mListener.onCheckedListener(mMessageLists.get(getAdapterPosition()));
                notifyDataSetChanged();
            });

            mTextViewMessage.setOnClickListener(view -> mOnClickListener.onItemOnClick(view, getAdapterPosition()));
        }
    }
}
