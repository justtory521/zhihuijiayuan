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
import tendency.hz.zhihuijiayuan.bean.City;
import tendency.hz.zhihuijiayuan.inter.RecyclerOnClickInter;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;

/**
 * Created by JasonYao on 2018/12/26.
 */
public class HotCityListAdapter extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private List<City> cities;
    private RecyclerOnClickInter mListener;

    public HotCityListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        cities = ConfigUnits.getInstance().getHotCities();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(mInflater.inflate(R.layout.item_hot_city, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((MyViewHolder) viewHolder).mTextViewCity.setText(cities.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public void setListener(RecyclerOnClickInter listener) {
        mListener = listener;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewCity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewCity = itemView.findViewById(R.id.text_city);
            mTextViewCity.setOnClickListener(view -> mListener.onItemOnClick(view,getAdapterPosition()));
        }
    }
}
