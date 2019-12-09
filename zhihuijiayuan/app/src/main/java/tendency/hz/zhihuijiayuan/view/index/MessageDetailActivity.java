package tendency.hz.zhihuijiayuan.view.index;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.Message;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.presenter.PersonalPrenImpl;
import tendency.hz.zhihuijiayuan.units.CacheUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.SpaceItemDecoration;
import tendency.hz.zhihuijiayuan.units.ViewUnits;
import tendency.hz.zhihuijiayuan.view.BaseActivity;
import tendency.hz.zhihuijiayuan.view.card.CardContentActivity;

/**
 * Author：Libin on 2019/12/6 0006 10:24
 * Email：1993911441@qq.com
 * Describe：消息详情
 */
public class MessageDetailActivity extends BaseActivity {
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.tv_title_name)
    TextView tvTitleName;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.rv_msg)
    RecyclerView rvMsg;
    @BindView(R.id.ll_no_msg)
    LinearLayout llNoMsg;
    @BindView(R.id.srf_msg)
    SmartRefreshLayout srfMsg;


    private String title;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_message_new);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    private void initView() {
        ViewUnits.getInstance().setTitleHeight(viewStatusBar);
        llTitle.setBackgroundResource(R.color.blue_0d8);
        tvTitleName.setText(title);
        srfMsg.setEnableRefresh(false);
        srfMsg.setEnableLoadMore(false);

        List<Message> msgList = new ArrayList<>();
        msgList.addAll(CacheUnits.getInstance().getMessageByCardId(type));

        //标记已读
        CacheUnits.getInstance().markAsRead(type);
        if (msgList.size() > 0){
            rvMsg.setVisibility(View.VISIBLE);
            llNoMsg.setVisibility(View.GONE);
        }else {
            rvMsg.setVisibility(View.GONE);
            llNoMsg.setVisibility(View.VISIBLE);
        }
        rvMsg.setLayoutManager(new LinearLayoutManager(this));
        rvMsg.addItemDecoration(new SpaceItemDecoration(ViewUnits.getInstance().dp2px(this, 12)));
        BaseQuickAdapter<Message, BaseViewHolder> adapter = new BaseQuickAdapter<Message,
                BaseViewHolder>(R.layout.rv_message_detail_item, msgList) {
            @Override
            protected void convert(BaseViewHolder helper, Message item) {
                if (!TextUtils.isEmpty( item.getCardID())&& !item.getCardID().equals("0")){
                    helper.setText(R.id.tv_msg_title,item.getCardName());
                }else {
                    helper.setText(R.id.tv_msg_title,"系统消息");
                }
                helper.setText(R.id.tv_msg_time, item.getDateTime());
                helper.setText(R.id.tv_msg_content, item.getContent());
            }
        };

        rvMsg.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!TextUtils.isEmpty(msgList.get(position).getCardID()) && !msgList.get(position).getCardID().equals("0")){
                    Intent intent = new Intent(MessageDetailActivity.this, CardContentActivity.class);
                    CardItem cardItem = new CardItem();
                    cardItem.setCardID(msgList.get(position).getCardID());
                    //如果消息URL为空，则直接打开对应卡片即可
                    cardItem.setCardUrl(TextUtils.isEmpty(msgList.get(position).getUrl()) ?
                            msgList.get(position).getCardUrl() : msgList.get(position).getUrl());
                    intent.putExtra("cardItem", cardItem);
                    intent.putExtra("type", Request.StartActivityRspCode.MESSAGE_CARDCONTENT_JUMP);
                    startActivity(intent);
                }
            }
        });

    }

    private void getData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        type = intent.getStringExtra("type");
    }

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }
}
