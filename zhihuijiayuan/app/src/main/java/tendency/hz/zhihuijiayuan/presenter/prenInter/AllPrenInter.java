package tendency.hz.zhihuijiayuan.presenter.prenInter;

/**
 * 全局P层继承接口
 * Created by JasonYao on 2017/6/12.
 */

public interface AllPrenInter {

    /**
     * 网络请求成功P层回调
     * @param what  网络请求标识码
     * @param object 返回对象
     */
    public void onSuccess(int what, Object object);

    /**
     * 网络请求失败P层回调
     * @param what  网络请求标识码
     * @param object 返回对象
     */
    public void onFail(int what, Object object);
}
