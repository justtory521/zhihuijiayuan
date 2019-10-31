package tendency.hz.zhihuijiayuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.holder.ThemeCardViewHolder;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.GoSreachOnClickInter;
import tendency.hz.zhihuijiayuan.adapter.holder.inter.ThemeRecyclerOnClickInter;
import tendency.hz.zhihuijiayuan.bean.Theme;

/**
 * Created by JasonYao on 2018/10/30.
 */
public class ThemeRecyclerAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ThemeRecyclerAdapter---";
    private List<Theme> mThemes;
    private LayoutInflater mInflater;
    private Context mContext;
    private ThemeDetailRecyclerAdapter mAdapter;
    private ThemeRecyclerOnClickInter mListener;
    private GoSreachOnClickInter mGoSreachListener;

    public ThemeRecyclerAdapter(Context context, List<Theme> themes) {
        mThemes = themes;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThemeCardViewHolder(mContext, mInflater.inflate(R.layout.item_theme_recycler, parent, false), mGoSreachListener, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ThemeCardViewHolder) holder).setData(mThemes.get(position));
    }

    @Override
    public int getItemCount() {
        return mThemes.size();
    }

    public void setListener(ThemeRecyclerOnClickInter listener) {
        mListener = listener;
    }

    public void setGoSreachListener(GoSreachOnClickInter listener) {
        mGoSreachListener = listener;
    }
}
