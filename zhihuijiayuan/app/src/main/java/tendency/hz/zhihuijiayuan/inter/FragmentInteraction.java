package tendency.hz.zhihuijiayuan.inter;

/**
 * Created by JasonYao on 2018/11/30.
 * Fragment之间消息传递
 */
public interface FragmentInteraction {
    void process();

    void clearMessage();
}
