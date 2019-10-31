package tendency.hz.zhihuijiayuan.units;

/**
 * Author：Li Bin on 2019/8/21 14:53
 * Description：
 */
public  class BleCommStatus {
    /**
     * operation mode: advertisement.
     */
    public static final byte OPER_ADV = 0x00;
    /**
     * operation mode: connection requirement.
     */
    public static final byte OPER_CON_REQ = 0x01;
    /**
     * operation mode: connection response.
     */
    public static final byte OPER_CON_RSP = 0x02;
    /**
     * operation mode: disconnection requirement.
     */
    public static final byte OPER_DISCON_REQ = 0x03;
    /**
     * operation mode: send message requirement.
     */
    public static final byte OPER_TRAN = 0x04;
    /**
     * operation mode: ble communication session open
     */
    public static final byte OPER_OPEN = 0x05;
    /**
     * operation mode: ble communication session clos
     */
    public  static final byte OPER_CLOSE = 0x06;
    /**
     * Advertisement Flag : LE General Discoverable Mode.
     */
    public static final byte ADV_LE_FLAG = 0x02;
    /**
     * Advertisement Flag : BR/EDR Not Supported.
     */
    public static final byte ADV_BR_EDR_FLAG = 0x04;

    /**
     * No Error occurred
     */
    public static final byte BLE_ERROR_OK = 0x00;

    /**
     * Error: At least one of the input parameters is invalid
     */
    public static final byte BLE_ERROR_INVALID_PARAMETER = 0x01;

    /**
     * Error: Operation is not permitted
     */
    public static final byte BLE_ERROR_INVALID_OPERATION = 0x02;
    /**
     * Error: connection requirement failed
     * time out
     */
    public static final byte BLE_ERROR_CONNECTION_TIMEOUT = 0x03;
    /**
     * Error: connection has disconnected
     * time out
     */
    public static final byte BLE_ERROR_CONNECTION_DISCONNECTED = 0x04;
}
