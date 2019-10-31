package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

/**
 * Created by JasonYao on 2018/10/30.
 */
public class Theme {
    String ThemeVal;
    String ThemeName;
    String ThemeRemarks;
    List<CardItem> Cards;

    @Override
    public String toString() {
        return "Theme{" +
                "ThemeVal='" + ThemeVal + '\'' +
                ", ThemeName='" + ThemeName + '\'' +
                ", ThemeRemarks='" + ThemeRemarks + '\'' +
                ", Cards=" + Cards +
                '}';
    }

    public String getThemeVal() {
        return ThemeVal;
    }

    public void setThemeVal(String themeVal) {
        ThemeVal = themeVal;
    }

    public String getThemeName() {
        return ThemeName;
    }

    public void setThemeName(String themeName) {
        ThemeName = themeName;
    }

    public String getThemeRemarks() {
        return ThemeRemarks;
    }

    public void setThemeRemarks(String themeRemarks) {
        ThemeRemarks = themeRemarks;
    }

    public List<CardItem> getCards() {
        return Cards;
    }

    public void setCards(List<CardItem> cards) {
        Cards = cards;
    }
}
