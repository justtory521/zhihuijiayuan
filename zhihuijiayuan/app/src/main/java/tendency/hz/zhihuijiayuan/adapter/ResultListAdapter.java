package tendency.hz.zhihuijiayuan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.City;
import tendency.hz.zhihuijiayuan.inter.RecyclerOnClickInter;

/**
 * Created by JasonYao on 2017/6/19.
 */

public class ResultListAdapter extends BaseAdapter {
    private static final String TAG = "ResultListAdapter---";
    private Context mContext;
    private List<City> mCities = new ArrayList<>();
    private RecyclerOnClickInter mListener;

    public ResultListAdapter(Context mContext, List<City> mCities) {
        this.mContext = mContext;
    }

    public void changeData(List<City> list) {
        mCities.clear();
        mCities.addAll(list);

        notifyDataSetChanged();
    }

    public void setListener(RecyclerOnClickInter listener) {
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public City getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ResultViewHolder holder;
        final int myPosition = 0;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.cp_item_search_result_listview, parent, false);
            holder = new ResultViewHolder();
            holder.name = view.findViewById(R.id.tv_item_result_listview_name);
            holder.mLayoutSelectedCity = view.findViewById(R.id.layout_city_sreach);
            holder.mLayoutSelectedCity.setOnClickListener(view1 -> mListener.onItemOnClick(view1, myPosition));
            view.setTag(holder);
        } else {
            holder = (ResultViewHolder) view.getTag();
        }
        holder.name.setText(mCities.get(position).getName());
        return view;
    }

    public static class ResultViewHolder {
        TextView name;
        LinearLayout mLayoutSelectedCity;
    }
}
