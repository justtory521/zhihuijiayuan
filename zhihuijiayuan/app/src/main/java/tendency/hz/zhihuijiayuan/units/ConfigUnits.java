package tendency.hz.zhihuijiayuan.units;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.bean.City;
import tendency.hz.zhihuijiayuan.bean.base.Field;

/**
 * 配置工具类
 * Created by JasonYao on 2018/2/27.
 */

public class ConfigUnits {
    private static ConfigUnits mInstances = null;  //单例对象

    private ConfigUnits() {
    }  //私有化构造方法

    public static ConfigUnits getInstance() {
        if (mInstances == null) {
            synchronized (ConfigUnits.class) {
                if (mInstances == null) {
                    mInstances = new ConfigUnits();
                }
            }
        }

        return mInstances;
    }

    /**
     * 设置模拟手机IMEI号
     */
    public void setPhoneAnalogIMEI(String imei) {
        SPUtils.getInstance().put(SPUtils.FILE_CONFIG, SPUtils.analogImei, imei);
    }

    /**
     * 清空模拟手机的IMEI号
     */
    public void clearPhoneAnalogIMEI() {
        SPUtils.getInstance().put(SPUtils.FILE_CONFIG, SPUtils.analogImei, "");
    }

    /**
     * 获取模拟手机IMEI号
     */
    public String getPhoneAnalogIMEI() {
        return (String) SPUtils.getInstance().get(SPUtils.FILE_CONFIG, SPUtils.analogImei, "");
    }

    /**
     * 设置本地数据库是否拷贝完成（0即为拷贝完成）
     */
    public void setDBStatus() {
        SPUtils.getInstance().put(SPUtils.DBCOPY, "DBStatus", "2");
    }

    /**
     * 获取本地数据库是否拷贝完成（0即为拷贝完成）
     *
     * @return
     */
    public String getDBStatus() {
        return SPUtils.getInstance().get(SPUtils.DBCOPY, "DBStatus", "").toString();
    }

    /**
     * 获取缓存的所有城市列表
     *
     * @return
     */
    public List<City> getAllCities() {
        List<City> cities = new ArrayList<>();
        Cursor cursor = AddressDbHelper.getInstance().get("select * from " + Field.Area.dbName + " order by " + Field.Area.area_name + " COLLATE LOCALIZED ASC", new String[]{});

        if (cursor.getCount() <= 0) {
            return cities;
        } else {
            while (cursor.moveToNext()) {
                City city = new City();
                city.setID(cursor.getString(1));
                city.setParentId(cursor.getString(2));
                city.setName(cursor.getString(3));
                city.setShortName(cursor.getString(4));

                cities.add(city);
            }


//            for (int i = 0; i < cities.size(); i++) {
//                if ("W".equals(PinyinUtils.getFirstLetter(FormatUtils.getInstance().getPinYin(cities.get(i).getName())))) {
//                    City city1 = new City();
//                    city1.setID("888889");
//                    city1.setName("天地人");
//                    city1.setShortName("天地人");
//                    cities.add(i, city1);
//                    break;
//                }
//            }


            City city = new City();
            city.setID("888888");
            city.setName("中国移动");
            city.setShortName("中国移动");
            cities.add(city);


            return cities;
        }
    }

    public List<City> getHotCities() {
        List<City> cities = new ArrayList<>();

        cities.add(new City("110100", "北京市"));
        cities.add(new City("330100", "杭州市"));
        cities.add(new City("330300", "温州市"));
        cities.add(new City("410100", "郑州市"));
        cities.add(new City("320100", "南京市"));
        cities.add(new City("310100", "上海市"));
        cities.add(new City("530100", "昆明市"));
        cities.add(new City("450100", "南宁市"));

        return cities;
    }

    /**
     * 根据城市名称获取城市ID
     *
     * @param cityName
     * @return
     */
    public String getCityIdByName(String cityName) {
        if (cityName.equals("中国移动")) {
            return "888888";
        }

        if (cityName.equals("天地人")) {
            return "888889";
        }
        if (FormatUtils.getInstance().isEmpty(cityName)) {
            return "0";
        }
        String cityId = null;
        Cursor cursor = AddressDbHelper.getInstance().get("select " + Field.Address.area_code + " from " + Field.Address.dbName + " where " + Field.Address.area_name + "='" + cityName + "'", new String[]{});

        if (cursor.getCount() <= 0) {
            return "0";
        } else {
            while (cursor.moveToNext()) {
                cityId = cursor.getString(0);
            }

            return cityId;
        }
    }

    /**
     * 根据二级行政区划获取城市ID
     *
     * @param cityName
     * @return
     */
    public String getCityIdBy3Name(String cityName) {
        if (FormatUtils.getInstance().isEmpty(cityName)) {
            return "0";
        }
        String cityId = null;
        Cursor cursor = AddressDbHelper.getInstance().get("select " + Field.Address.area_code + " from " + Field.Address.dbName + " where " + Field.Address.area_name + "='" + cityName + "'", new String[]{});

        if (cursor.getCount() <= 0) {
            return "0";
        } else {
            while (cursor.moveToNext()) {
                cityId = cursor.getString(0);
            }

            return cityId;
        }
    }

    /**
     * 根据关键字，返回城市列表
     *
     * @param keyword
     * @return
     */
    public static List<City> getSreachCity(String keyword) {
        List<City> cities = new ArrayList<>();
        Cursor cursor = AddressDbHelper.getInstance().get("select * from " + Field.Area.dbName + " where Name like \"%" + keyword
                + "%\" or ShortName like \"%" + keyword + "%\"", new String[]{});
        if (cursor.getCount() <= 0) {
            return null;
        } else {
            while (cursor.moveToNext()) {
                City city = new City();
                city.setID(cursor.getString(1));
                city.setParentId(cursor.getString(2));
                city.setName(cursor.getString(3));
                city.setShortName(cursor.getString(4));

                cities.add(city);
            }
        }

        return cities;
    }

    /**
     * 根据城市名称获取城市
     *
     * @param cityName
     * @return
     */
    public City getCityByCityName(String cityName) {
        City city = new City();
        Cursor cursor = AddressDbHelper.getInstance().get("select * from " + Field.Area.dbName + " where " + Field.Area.area_name + "='" + cityName + "'", new String[]{});
        if (cursor.getCount() <= 0) {
            return null;
        } else {
            while (cursor.moveToNext()) {
                city.setID(cursor.getString(1));
                city.setParentId(cursor.getString(2));
                city.setName(cursor.getString(3));
                city.setShortName(cursor.getString(4));
            }
        }

        return city;
    }

    /**
     * 获取是否首次安装的状态
     *
     * @return
     */
    public boolean getFirstInstallStatus() {
        return (boolean) SPUtils.getInstance().get(SPUtils.FILE_CONFIG, SPUtils.isFirstInstall, true);
    }

    /**
     * 设置是否首次安装的状态
     *
     * @param status
     */
    public void setFirstInstallStatus(boolean status) {
        SPUtils.getInstance().put(SPUtils.FILE_CONFIG, SPUtils.isFirstInstall, status);
    }

    /**
     * 通过ID获取性别
     *
     * @param id
     * @return
     */
    public String getSexById(String id) {
        if (FormatUtils.getInstance().isEmpty(id)) return "待完善";
        switch (id) {
            case "1":
                return "男";
            case "2":
                return "女";
            case "3":
                return "待完善";
            default:
                return "待完善";
        }
    }

    /**
     * 获取广告Img
     *
     * @return
     */
    public String getAdImg() {
        return (String) SPUtils.getInstance().get(SPUtils.FILE_CONFIG, SPUtils.adImg, "");
    }

    /**
     * 设置广告Img地址
     *
     * @param value
     * @return
     */
    public void setAdImg(String value) {
        SPUtils.getInstance().put(SPUtils.FILE_CONFIG, SPUtils.adImg, value);
    }

    /**
     * 校验广告Img
     *
     * @return
     */
    public boolean checkAdImg(String adImage, String adUrl) {
        if (FormatUtils.getInstance().isEmpty(adImage)) {
            return false;
        }

        if (FormatUtils.getInstance().isEmpty(adUrl)) {
            return false;
        }

        if (FormatUtils.getInstance().isEmpty(getAdImg())) {
            return false;
        }

        if (!adImage.equals(getAdImg())) {
            return false;
        }

        if (!adImage.equals(getAdUrl())) {
            return false;
        }

        return true;
    }

    /**
     * 获取广告Img
     *
     * @return
     */
    public String getAdUrl() {
        return (String) SPUtils.getInstance().get(SPUtils.FILE_CONFIG, SPUtils.adUrl, "");
    }

    /**
     * 设置广告Img地址
     *
     * @param value
     * @return
     */
    public void setAdUrl(String value) {
        SPUtils.getInstance().put(SPUtils.FILE_CONFIG, SPUtils.adUrl, value);
    }

    /**
     * 获取当前缓存的手机版本号
     *
     * @return
     */
    public String getSVersionCode() {
        return (String) SPUtils.getInstance().get(SPUtils.FILE_CONFIG, SPUtils.sVersionCode, "");
    }

    /**
     * 获取当前缓存的手机版本号
     *
     * @param value
     */
    public void setSVersionCode(String value) {
        SPUtils.getInstance().put(SPUtils.FILE_CONFIG, SPUtils.sVersionCode, value);
    }
}
