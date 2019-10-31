package tendency.hz.zhihuijiayuan.units;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.search.SearchRequest;

import java.util.UUID;

import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.inter.BleNotifyResult;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

/**
 * Created by dingjikerbo on 2016/8/27.
 */
public class ClientManager {
    private static BluetoothClient mClient;
    private static SearchRequest mRequest;
    private static BleConnectOptions mOptions;


    public static BluetoothClient getClient(Context context) {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(context);
                }
            }
        }
        return mClient;
    }

    public static SearchRequest getRequest() {
        if (mRequest == null) {
            synchronized (ClientManager.class) {
                if (mRequest == null) {
                    mRequest = new SearchRequest.Builder()
                            .searchBluetoothLeDevice(3000, 1)
                            .build();
                }
            }
        }

        return mRequest;
    }

    public static BleConnectOptions getOption() {
        if (mOptions == null) {
            synchronized (ClientManager.class) {
                if (mOptions == null) {
                    mOptions = new BleConnectOptions.Builder()
                            .setConnectRetry(3)
                            .setConnectTimeout(30000)
                            .setServiceDiscoverRetry(3)
                            .setServiceDiscoverTimeout(20000)
                            .build();
                }
            }
        }

        return mOptions;
    }


    public static void notifyBle(String mac, UUID SUUID, UUID CUUID, final BleNotifyResult bleNotifyResult) {
        ClientManager.getClient(MyApplication.getInstance()).notify(mac, SUUID, CUUID, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                bleNotifyResult.sendResult(value);
            }

            @Override
            public void onResponse(int code) {
                if (code == REQUEST_SUCCESS) {

                }
            }
        });


    }

    public static void unNotifyBle(String mac, UUID SUUID, UUID CUUID) {
        ClientManager.getClient(MyApplication.getInstance()).unnotify(mac, SUUID, CUUID, code -> {

        });
    }
}




