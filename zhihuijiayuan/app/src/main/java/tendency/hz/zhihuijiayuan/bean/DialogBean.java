package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

/**
 * Author：Libin on 2020/1/3 0003 15:19
 * Email：1993911441@qq.com
 * Describe：
 */
public class DialogBean {

    /**
     * type : normal
     * title : 模拟标题
     * content : 这是一串很长的字符串呐
     * button : [{"text":"取消","textColor":"#323233"},{"text":"确定","textColor":"#323233"},{"text":"待确定","textColor":"#323233"}]
     * callback : dialog_callBack
     */

    private String type;
    private String title;
    private String content;
    private String callback;
    private List<ButtonBean> button;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public List<ButtonBean> getButton() {
        return button;
    }

    public void setButton(List<ButtonBean> button) {
        this.button = button;
    }

    public static class ButtonBean {
        /**
         * text : 取消
         * textColor : #323233
         */

        private String text;
        private String textColor;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }
    }
}
