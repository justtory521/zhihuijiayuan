package tendency.hz.zhihuijiayuan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.UserServeBean;
import tendency.hz.zhihuijiayuan.bean.base.Field;
import tendency.hz.zhihuijiayuan.units.DateUtils;
import tendency.hz.zhihuijiayuan.view.user.CreditRecordActivity;

/**
 * Author：Libin on 2019/5/14 11:22
 * Email：1993911441@qq.com
 * Describe：信用积分进行服务
 */
public class CreditPagerAdapter extends PagerAdapter {

    private List<UserServeBean.DataBean> mServerList;
    private Context mContext;
    private LayoutInflater mInflater;

    public CreditPagerAdapter(Context context, List<UserServeBean.DataBean> serverList) {
        this.mServerList = serverList;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mServerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mInflater.inflate(R.layout.vp_serve_item, null);
        LinearLayout linearLayout = view.findViewById(R.id.ll_user_serve);
        TextView tvSerVeTime = view.findViewById(R.id.tv_serve_time);
        TextView tvServeStatus = view.findViewById(R.id.tv_serve_status);
        SimpleDraweeView sdvServe = view.findViewById(R.id.sdv_serve);
        TextView tvServeName = view.findViewById(R.id.tv_serve_name);


        UserServeBean.DataBean dataBean = mServerList.get(position);
        tvSerVeTime.setText(DateUtils.formatTime(Field.DateType.standard_time_format, Field.DateType.year_month_day, dataBean.getCreateTime()));
        tvServeStatus.setText(dataBean.getStatusText());
        sdvServe.setImageURI(dataBean.getImageUrl());
        tvServeName.setText(dataBean.getServiceName());

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreditRecordActivity.class);
                intent.putExtra("type", 1);
                mContext.startActivity(intent);

            }
        });


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
