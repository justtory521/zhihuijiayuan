package tendency.hz.zhihuijiayuan.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import tendency.hz.zhihuijiayuan.bean.UserRankBean;
import tendency.hz.zhihuijiayuan.view.BaseActivity;

/**
 * Author：Libin on 2019/5/14 16:24
 * Email：1993911441@qq.com
 * Describe：
 */
public class CreditRankAdapter extends BaseQuickAdapter<UserRankBean.DataBean.ListBean,BaseViewHolder>{


    public CreditRankAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserRankBean.DataBean.ListBean item) {
    }
}
