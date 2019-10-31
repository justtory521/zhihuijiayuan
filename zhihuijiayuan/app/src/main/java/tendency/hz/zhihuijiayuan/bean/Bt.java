package tendency.hz.zhihuijiayuan.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by JasonYao on 2018/7/12.
 */
public class Bt implements Serializable {
    private String name;
    private String address;
    private int rssi;
    private byte[] beacon;
    private String deviceId;

    private String bleStatus;
    private UUID writeSUUID;
    private UUID writeCUUID;
    private UUID notifySUUID;
    private UUID notifyCUUID;
    private UUID readSUUID;
    private UUID readCUUID;

    private String date;
    private String operation;
    private String sianal;
    private String reset;

    @Override
    public String toString() {
        return "Bt{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", rssi=" + rssi +
                ", beacon=" + Arrays.toString(beacon) +
                ", deviceId='" + deviceId + '\'' +
                ", bleStatus='" + bleStatus + '\'' +
                ", writeSUUID=" + writeSUUID +
                ", writeCUUID=" + writeCUUID +
                ", notifySUUID=" + notifySUUID +
                ", notifyCUUID=" + notifyCUUID +
                ", readSUUID=" + readSUUID +
                ", readCUUID=" + readCUUID +
                ", date='" + date + '\'' +
                ", operation='" + operation + '\'' +
                ", sianal='" + sianal + '\'' +
                ", reset='" + reset + '\'' +
                '}';
    }

    public UUID getReadSUUID() {
        return readSUUID;
    }

    public void setReadSUUID(UUID readSUUID) {
        this.readSUUID = readSUUID;
    }

    public UUID getReadCUUID() {
        return readCUUID;
    }

    public void setReadCUUID(UUID readCUUID) {
        this.readCUUID = readCUUID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSianal() {
        return sianal;
    }

    public void setSianal(String sianal) {
        this.sianal = sianal;
    }

    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public byte[] getBeacon() {
        return beacon;
    }

    public void setBeacon(byte[] beacon) {
        this.beacon = beacon;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getBleStatus() {
        return bleStatus;
    }

    public void setBleStatus(String bleStatus) {
        this.bleStatus = bleStatus;
    }

    public UUID getWriteSUUID() {
        return writeSUUID;
    }

    public void setWriteSUUID(UUID writeSUUID) {
        this.writeSUUID = writeSUUID;
    }

    public UUID getWriteCUUID() {
        return writeCUUID;
    }

    public void setWriteCUUID(UUID writeCUUID) {
        this.writeCUUID = writeCUUID;
    }

    public UUID getNotifySUUID() {
        return notifySUUID;
    }

    public void setNotifySUUID(UUID notifySUUID) {
        this.notifySUUID = notifySUUID;
    }

    public UUID getNotifyCUUID() {
        return notifyCUUID;
    }

    public void setNotifyCUUID(UUID notifyCUUID) {
        this.notifyCUUID = notifyCUUID;
    }
}
