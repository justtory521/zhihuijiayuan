package tendency.hz.zhihuijiayuan.model.modelInter;

/**
 * 基本配置网络接口定义
 * Created by JasonYao on 2018/3/19.
 */

public interface BaseModelInter {
    /**
     * 获取标准三级区划
     *
     * @param netCode
     */
    void getAllDistricts(int netCode);

    /**
     * 获取所有的配置数据字典
     *
     * @param netCode
     */
    void getDataDictionary(int netCode);

    /**
     * 上传图片（暂时只是上传头像用到）
     *
     * @param netCode
     */
    void uploadImg(int netCode,String img);

    /**
     * 提交设备信息
     *
     * @param netCode
     */
    void addAppDeviceInfo(int netCode);
}
