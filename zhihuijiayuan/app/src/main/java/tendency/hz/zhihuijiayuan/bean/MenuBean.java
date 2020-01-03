package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

/**
 * Author：Libin on 2020/1/3 0003 15:19
 * Email：1993911441@qq.com
 * Describe：
 */
public class MenuBean {

    /**
     * cancelBtnText : 根本不是取消文案
     * menuList : ["这绝不是菜单一","这绝不是菜单二","这绝不是菜单三","这绝不是菜单四","这绝不是菜单五"]
     * callback : menu_callBack
     */

    private String cancelBtnText;
    private String callback;
    private List<String> menuList;

    public String getCancelBtnText() {
        return cancelBtnText;
    }

    public void setCancelBtnText(String cancelBtnText) {
        this.cancelBtnText = cancelBtnText;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public List<String> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<String> menuList) {
        this.menuList = menuList;
    }
}
