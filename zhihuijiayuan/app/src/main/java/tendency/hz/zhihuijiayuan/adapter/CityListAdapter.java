package tendency.hz.zhihuijiayuan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.bean.City;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.units.ConfigUnits;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.PinyinUtils;
import tendency.hz.zhihuijiayuan.units.UserUnits;

/**
 * 城市列表Adpater
 * Created by JasonYao on 2017/6/19.
 */

public class CityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CityListAdapter---";
    private static final int ITEMTYPE_LOCATION = 0x0001;  //定位城市
    private static final int ITEMTYPE_LIST = 0x0002;  //城市列表
    private static final int ITEMTYPE_HOT = 0x0003; //热门城市
    private static final int ITEMTYPE_SELECTED = 0x0004; //当前城市


    private Context mContext;
    private List<City> mCities;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnCityClickListener onCityClickListener;
    private static int locateState = Request.LocateState.LOCATING;
    private String locatedCity;

    private LayoutInflater mInflater;

    private City mNowSelectedCity;

    public CityListAdapter(Context context, List<City> cities, City city) {
        this.mContext = context;
        this.mCities = cities;
        this.mInflater = LayoutInflater.from(mContext);
        this.mNowSelectedCity = city;

        int size = mCities.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++) {
            //当前城市拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(FormatUtils.getInstance().getPinYin(mCities.get(index).getName()));
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(FormatUtils.getInstance().getPinYin(mCities.get(index - 1).getName())) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }

    /**
     * 更新定位状态
     *
     * @param state
     */
    public void updateLocateState(int state, String city) {
        this.locateState = state;
        this.locatedCity = city;
        notifyDataSetChanged();
        notifyItemChanged(0);
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEMTYPE_LOCATION) {
            return new LocationViewHolder(mInflater.inflate(R.layout.cp_view_locate_city, parent, false));
        } else if (viewType == ITEMTYPE_HOT) {
            return new HotLocatinViewHoler(mInflater.inflate(R.layout.cp_view_hot_city, parent, false));
        } else if (viewType == ITEMTYPE_SELECTED) {
            return new SelectedCityViewHolder(mInflater.inflate(R.layout.cp_view_selected_city, parent, false));
        } else {
            return new CityViewHolder(mInflater.inflate(R.layout.cp_item_city_listview, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder-->");
        if (position == 1) {
            switch (locateState) {
                case Request.LocateState.LOCATING:
                    ((LocationViewHolder) holder).mTextViewLocated.setText(mContext.getString(R.string.cp_locating));
                    break;
                case Request.LocateState.FAILED:
                    ((LocationViewHolder) holder).mTextViewLocated.setText(R.string.cp_located_failed);
                    break;
                case Request.LocateState.SUCCESS:
                    ((LocationViewHolder) holder).mTextViewLocated.setText(locatedCity);
                    break;
            }
        } else if (position == 2) {

        } else if (position == 0) {
            if (mNowSelectedCity != null) {
                ((SelectedCityViewHolder) holder).mTextViewCity.setText(mNowSelectedCity.getName());
            } else {
                ((SelectedCityViewHolder) holder).mTextViewCity.setText(UserUnits.getInstance().getSelectCity());
            }
        } else {
            final String city = mCities.get(position).getName();
            ((CityViewHolder) holder).mTextViewName.setText(city);
            ((CityViewHolder) holder).mTextCityCode.setText(mCities.get(position).getID());
            String currentLetter = PinyinUtils.getFirstLetter(FormatUtils.getInstance().getPinYin(mCities.get(position).getName()));
            String previousLetter = position >= 1 ? PinyinUtils.getFirstLetter(FormatUtils.getInstance().getPinYin(mCities.get(position - 1).getName())) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                ((CityViewHolder) holder).mTextViewLetter.setVisibility(View.VISIBLE);
                ((CityViewHolder) holder).mTextViewLetter.setText(currentLetter);
            } else {
                ((CityViewHolder) holder).mTextViewLetter.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return ITEMTYPE_SELECTED;
            case 1:
                return ITEMTYPE_LOCATION;
            case 2:
                return ITEMTYPE_HOT;
            default:
                return ITEMTYPE_LIST;
        }
    }

    private class HotLocatinViewHoler extends RecyclerView.ViewHolder {
        private RecyclerView mRecyclerView;
        private GridLayoutManager mManager;
        private HotCityListAdapter mAdapter;

        public HotLocatinViewHoler(@NonNull View itemView) {
            super(itemView);

            mRecyclerView = itemView.findViewById(R.id.recycler_city_hot);
            mAdapter = new HotCityListAdapter(mContext);
            mManager = new GridLayoutManager(mContext, 3);
            mRecyclerView.setLayoutManager(mManager);
            mRecyclerView.setAdapter(mAdapter);

            mAdapter.setListener((view, postion) -> onCityClickListener.onHotCityClick(ConfigUnits.getInstance().getHotCities().get(postion).getName()));
        }
    }

    private class SelectedCityViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewCity;
        private LinearLayout mLayoutCity;

        public SelectedCityViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewCity = itemView.findViewById(R.id.text_selected_city);
            mLayoutCity = itemView.findViewById(R.id.layout_select_city);
            mLayoutCity.setOnClickListener(view -> onCityClickListener.onLocateResultClick(mTextViewCity.getText().toString()));
        }
    }

    private class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextViewLocated;

        LinearLayout mLinearLayout;

        public LocationViewHolder(View itemView) {
            super(itemView);

            mTextViewLocated = itemView.findViewById(R.id.tv_located_city);

            mLinearLayout = itemView.findViewById(R.id.layout_locate);

            mLinearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_locate:
                    if (locateState == Request.LocateState.FAILED) {
                        //重新定位
                        if (onCityClickListener != null) {
                            onCityClickListener.onLocateClick();
                        }
                    } else if (locateState == Request.LocateState.SUCCESS) {
                        //返回定位城市
                        if (onCityClickListener != null) {
                            onCityClickListener.onLocateResultClick(mTextViewLocated.getText().toString());
                        }
                    }
                    break;
            }
        }
    }


    private class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextViewLetter, mTextViewName, mTextCityCode;

        public CityViewHolder(View itemView) {
            super(itemView);

            mTextViewLetter = itemView.findViewById(R.id.tv_item_city_listview_letter);
            mTextViewName = itemView.findViewById(R.id.tv_item_city_listview_name);
            mTextCityCode = itemView.findViewById(R.id.tv_city_code);

            mTextViewName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tv_item_city_listview_name:
                    if (onCityClickListener != null) {
                        onCityClickListener.onCityClick(getAdapterPosition(), view);
                    }
                    break;

            }
        }
    }


    public void refreshData(List<City> list) {
        if (list != null) {
            mCities = list;
        }

        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        if (letter.equals("定位")) {
            return 0;
        }

        if (letter.equals("热门")) {
            return 1;
        }

        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    public interface OnCityClickListener {
        void onCityClick(int position, View view);

        void onLocateClick();

        void onLocateResultClick(String cityName);

        void onHotCityClick(String cityName);
    }
}
