package tendency.hz.zhihuijiayuan.units;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.wearlink.blecomm.BleService;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Author：Li Bin on 2019/10/10 15:50
 * Description：
 */
public class BluetoothUtils {
    private static BluetoothUtils mInstance;
    private BleService bleService;

    private BluetoothUtils() {

    }

    public static BluetoothUtils getInstance() {
        if (mInstance == null) {
            synchronized (BluetoothUtils.class) {
                if (mInstance == null) {
                    mInstance = new BluetoothUtils();
                }
            }
        }
        return mInstance;
    }

    public void startSerVice(Context context) {
        if (connection !=null){
            Intent intent = new Intent(context, BleService.class);
            context.bindService(intent, connection, BIND_AUTO_CREATE);
        }
    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.log("onBindService");
            bleService = ((BleService.MyBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.log("service disconnected.");
        }

    };

    public BleService getBleService() {
        return bleService;
    }

    /**
     * @param context 断开服务
     */
    public void unbindService(Context context) {
        if (bleService != null && connection != null) {
            LogUtils.log("解绑服务。。。。");
            context.unbindService(connection);
        }
    }

}
