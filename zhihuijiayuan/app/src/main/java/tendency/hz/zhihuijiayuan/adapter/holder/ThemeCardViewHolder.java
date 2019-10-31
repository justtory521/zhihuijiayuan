package tendency.hz.zhihuijiayuan.adapter.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.ThemeDetailRecyclerAdapter;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.GoSreachOnClickInter;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.ThemeRecyclerOnClickInter;
import tendency.hz.zhihuijiayuan.bean.Theme;

/**
 * Created by JasonYao on 2018/11/5.
 */
public class ThemeCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ThemeDetailRecyclerAdapter mAdapter;
    private ThemeRecyclerOnClickInter mListener;
    private GoSreachOnClickInter mGoSreachListener;
    private TextView mTextViewThemeName, mTextViewRemarks;
    private RecyclerView mRecyclerView;
    private LinearLayout mGoLayout;
    private Theme mTheme;
    private Context mContext;

    public ThemeCardViewHolder(Context context, View itemView, GoSreachOnClickInter goSreachOnClickInter, ThemeRecyclerOnClickInter themeRecyclerOnClickInter) {
        super(itemView);
        mContext = context;
        mTextViewThemeName = itemView.findViewById(R.id.text_theme_name);
        mTextViewRemarks = itemView.findViewById(R.id.text_theme_remarks);
        mRecyclerView = itemView.findViewById(R.id.recycler_cards);
        mGoLayout = itemView.findViewById(R.id.layout_go);
        mGoSreachListener = goSreachOnClickInter;
        mListener = themeRecyclerOnClickInter;
        mGoLayout.setOnClickListener(view -> mGoSreachListener.onItemOnClick(view, getAdapterPosition()));
    }

    public void setData(Theme theme) {
        mTheme = theme;
        mTextViewThemeName.setText(mTheme.getThemeName());
        mTextViewRemarks.setText(mTheme.getThemeRemarks());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mAdapter=new ThemeDetailRecyclerAdapter(theme.getCards(),mContext);
        mAdapter.setLisntener(mListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {

    }
}
