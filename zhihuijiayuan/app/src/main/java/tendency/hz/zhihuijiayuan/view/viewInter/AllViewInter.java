package tendency.hz.zhihuijiayuan.view.viewInter;

/**
 * 全局V层继承接口
 * Created by JasonYao on 2018/3/19.
 */

public interface AllViewInter {
    /**
     * 网络请求成功V层回调
     * @param what  网络请求标识码
     * @param object 返回对象
     */
    public void onSuccessed(int what, Object object);

    /**
     * 网络请求失败V层回调
     * @param what  网络请求标识码
     * @param object 返回对象
     */
    public void onFailed(int what, Object object);
}
