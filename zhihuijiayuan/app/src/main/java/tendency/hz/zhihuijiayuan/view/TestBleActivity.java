package tendency.hz.zhihuijiayuan.view;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.adapter.BtRecyclerAdapter;
import tendency.hz.zhihuijiayuan.bean.Bt;
import tendency.hz.zhihuijiayuan.bean.base.Request;
import tendency.hz.zhihuijiayuan.databinding.ActivityTestBleBinding;
import tendency.hz.zhihuijiayuan.units.ClientManager;
import tendency.hz.zhihuijiayuan.units.FormatUtils;
import tendency.hz.zhihuijiayuan.units.ViewUnits;

/**
 * Created by JasonYao on 2019/3/6.
 */
public class TestBleActivity extends BaseActivity {
    private static final String TAG = "TestBleActivity---";
    private ActivityTestBleBinding mBinding;
    private SearchRequest mRequest;
    private BluetoothAdapter mBluetoothAdapter;//蓝牙适配器
    private BtRecyclerAdapter mAdapter;
    private List<Bt> mList = new ArrayList<>();
    private BleConnectOptions mOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test_ble);

        initBle();

        initData();

        setListener();
    }

    private void initBle() {
        mRequest = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 2)
                .build();
        mOptions = new BleConnectOptions.Builder()
                .setConnectRetry(3)   // 连接如果失败重试3次
                .setConnectTimeout(30000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
                .build();
    }

    private void initData() {
        mAdapter = new BtRecyclerAdapter(this, mList);
        mBinding.recyclerBleList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerBleList.setAdapter(mAdapter);
    }

    private void setListener() {
        mBinding.buttonScan.setOnClickListener(view -> {
            if (checkBle()) {
                scanBle();
            }
        });

        /*mAdapter.setListener((view, postion) -> {
            ViewUnits.getInstance().showLoading(this, "连接中...");
            String mac=mList.get(postion).getAddress();
            Log.e(TAG,"mac地址为："+mac);
            ClientManager.getClient(TestBleActivity.this).connect(mac, mOptions, new BleConnectResponse() {
                @Override
                public void onResponse(int code, BleGattProfile data) {
                    ViewUnits.getInstance().missLoading();
                    if (code != REQUEST_SUCCESS) {
                        ViewUnits.getInstance().showMiddleToast("连接失败");
                        return;
                    }

                    ViewUnits.getInstance().showMiddleToast("连接成功");

                    for (BleGattService service : data.getServices()) {
                        Log.e(TAG,data.getServices().size()+"");
                        Log.e(TAG, "Service=" + service.toString());
                    }
                }
            });
        });*/
    }

    /**
     * 检查蓝牙
     */
    private boolean checkBle() {
        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ViewUnits.getInstance().showToast("没有蓝牙设备");
            return false;
        }

        //打开蓝牙
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Request.Bluetooth.bluetoothEnable);
        }

        return true;
    }

    /**
     * 扫描蓝牙设备
     */
    private void scanBle() {
        mList.clear();
        mAdapter.notifyDataSetChanged();

        ClientManager.getClient(this).search(mRequest, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                ViewUnits.getInstance().showLoading(TestBleActivity.this, "扫描中...");
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Log.e(TAG, "设备名称为：" + device.getName());
                Log.e(TAG, "设备地址为：" + device.getAddress());
                ViewUnits.getInstance().missLoading();
                if (mList != null) {
                    //判断是否已存在
                    boolean canadd = true;
                    for (Bt temp : mList) {
                        if (temp.getAddress().equals(device.getAddress())) {
                            //已存在则更新rssi
                            temp.setRssi(device.rssi);
                            canadd = false;
                        }
                    }
                    if (canadd) {
                        if (!FormatUtils.getInstance().isEmpty(device.getName())) {
                            //不存在则添加
                            Bt newdevice = new Bt();
                            newdevice.setName(device.getName());
                            newdevice.setRssi(device.rssi);
                            newdevice.setAddress(device.getAddress());
                            mList.add(newdevice);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onSearchStopped() {

            }

            @Override
            public void onSearchCanceled() {
            }
        });
    }
}
