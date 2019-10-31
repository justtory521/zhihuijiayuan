package tendency.hz.zhihuijiayuan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.UserRightsBean;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseFragment;
import tendency.hz.zhihuijiayuan.widget.GridItemDecoration;

/**
 * Author：Libin on 2019/5/15 20:43
 * Email：1993911441@qq.com
 * Describe：
 */
public class UserRightsFragment extends BaseFragment {
    @BindView(R.id.rv_user_rights)
    RecyclerView rvUserRights;
    Unbinder unbinder;
    private List<UserRightsBean.DataBean.ListBean> dataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_rights, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();
        initView();
        return view;
    }

    private void getData() {
        dataList = (List<UserRightsBean.DataBean.ListBean>) getArguments().getSerializable("type");
    }

    private void initView() {
        rvUserRights.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvUserRights.addItemDecoration(new GridItemDecoration(ViewUnits.getInstance().dp2px(getActivity(), 12)));
        BaseQuickAdapter<UserRightsBean.DataBean.ListBean, BaseViewHolder> adapter = new
                BaseQuickAdapter<UserRightsBean.DataBean.ListBean, BaseViewHolder>(R.layout.rv_user_rights_item, dataList) {
                    @Override
                    protected void convert(BaseViewHolder helper, UserRightsBean.DataBean.ListBean item) {
                        SimpleDraweeView simpleDraweeView = helper.getView(R.id.sdv_rights);
                        simpleDraweeView.setImageURI(item.getHeadImage());
                        helper.setText(R.id.tv_rights_name, item.getServiceName());
                        helper.setText(R.id.tv_rights_content, item.getDescribe());
                    }
                };
        rvUserRights.setAdapter(adapter);
    }

    public static UserRightsFragment newInstance(List<UserRightsBean.DataBean.ListBean> dataList) {

        Bundle args = new Bundle();
        args.putSerializable("type", (Serializable) dataList);
        UserRightsFragment fragment = new UserRightsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
