package tendency.hz.zhihuijiayuan.presenter;

import tendency.hz.zhihuijiayuan.model.SetModelImpl;
import tendency.hz.zhihuijiayuan.model.modelInter.SetModelInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.AllPrenInter;
import tendency.hz.zhihuijiayuan.presenter.prenInter.SetPrenInter;
import tendency.hz.zhihuijiayuan.view.viewInter.AllViewInter;

/**
 * Created by JasonYao on 2018/4/2.
 */

public class SetPrenImpl implements SetPrenInter, AllPrenInter {

    private AllViewInter mAllViewInter;
    private SetModelInter mSetModelInter;

    public SetPrenImpl(AllViewInter allViewInter) {
        mAllViewInter = allViewInter;
        mSetModelInter = new SetModelImpl(this);
    }


    @Override
    public void feedBack(int netCode, String title, String adviceContent, String imgList, String contact) {
        mSetModelInter.feedBack(netCode, title, adviceContent, imgList, contact);
    }

    @Override
    public void validate(int netCode, String cardId, String realName, String EditIDCardTime, String imgList) {
        mSetModelInter.validate(netCode, cardId, realName, EditIDCardTime, imgList);
    }

    @Override
    public void cardQrCode(int netCode, String code) {
        mSetModelInter.cardQrCode(netCode, code);
    }

    @Override
    public void startPage(int netCode) {
        mSetModelInter.startPage(netCode);
    }

    @Override
    public void sendAppTyoe(int netCode) {
        mSetModelInter.sendAppTyoe(netCode);
    }

    @Override
    public void getMessagePrevent(int netCode) {
        mSetModelInter.getMessagePrevent(netCode);
    }

    @Override
    public void editMessagePrevent(int netCode, String isEnablePrevent) {
        mSetModelInter.editMessagePrevent(netCode, isEnablePrevent);
    }

    @Override
    public void getVersion(int netCode) {
        mSetModelInter.getVersion(netCode);
    }

    @Override
    public void WXSubscribeMessage(int netCode, String touser, String template_id, String scene) {
        mSetModelInter.WXSubscribeMessage(netCode, touser, template_id, scene);
    }

    @Override
    public void onSuccess(int what, Object object) {
        mAllViewInter.onSuccessed(what, object);
    }

    @Override
    public void onFail(int what, Object object) {
        mAllViewInter.onFailed(what, object);
    }
}
