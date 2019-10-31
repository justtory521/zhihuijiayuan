package tendency.hz.zhihuijiayuan.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.bertsir.zbar.Qr.Image;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.UserRankBean;

/**
 * Author：Libin on 2019/5/14 12:49
 * Email：1993911441@qq.com
 * Describe：
 */
public class UserRankAdapter extends RecyclerView.Adapter<UserRankAdapter.MyViewHolder> {
    private List<UserRankBean.DataBean.ListBean> dataList;
    private Context mContext;
    private LayoutInflater mInflater;

    public UserRankAdapter(Context context, List<UserRankBean.DataBean.ListBean> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(mInflater.inflate(R.layout.rv_rank_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        UserRankBean.DataBean.ListBean dataBean = dataList.get(i);

        Object avatar = dataBean.getHeadImg();
        if (avatar instanceof Integer) {
            myViewHolder.sdvAvatar.setVisibility(View.GONE);
            myViewHolder.ivAvatar.setVisibility(View.VISIBLE);
            myViewHolder.ivAvatar.setImageResource((Integer) avatar);
        } else if (avatar instanceof String) {
            myViewHolder.sdvAvatar.setVisibility(View.VISIBLE);
            myViewHolder.ivAvatar.setVisibility(View.GONE);
            myViewHolder.sdvAvatar.setImageURI(String.valueOf(avatar));
        }


        myViewHolder.tvUserName.setText(dataBean.getNickName());
        myViewHolder.tvUserRank.setText(String.valueOf(dataBean.getPreviousCreditScoreRanking()));
    }

    @Override
    public int getItemCount() {
        return dataList.size() > 0 ? dataList.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdvAvatar;
        ImageView ivAvatar;
        TextView tvUserName;
        TextView tvUserRank;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            sdvAvatar = itemView.findViewById(R.id.sdv_user_avatar);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserRank = itemView.findViewById(R.id.tv_user_rank);
            ivAvatar = itemView.findViewById(R.id.iv_user_credit_avatar);
        }
    }
}
