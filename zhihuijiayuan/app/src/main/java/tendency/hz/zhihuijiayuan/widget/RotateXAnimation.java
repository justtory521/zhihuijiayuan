package tendency.hz.zhihuijiayuan.widget;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Author：Li Bin on 2019/11/19 12:44
 * Description：X轴旋转
 */
public class RotateXAnimation extends Animation {
    int centerX, centerY;
    Camera camera = new Camera();

    /**
     * 获取坐标，定义动画时间
     * @param width
     * @param height
     * @param parentWidth
     * @param parentHeight
     */
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

    }

    public RotateXAnimation(int centerX, int centerY){
        this.centerX = centerX;
        this.centerY = centerY;
    }

    /**
     * 旋转的角度设置
     * @param interpolatedTime
     * @param t
     */

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float degrees = 90+ ((-90) * interpolatedTime);
        Matrix matrix = t.getMatrix();
        camera.save();
        //设置camera的位置
        camera.translate(0.0f, 0.0f, 0);
        camera.rotateX(degrees);
        //把我们的摄像头加在变换矩阵上
        camera.getMatrix(matrix);
        //设置翻转中心点
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX,centerY);
        camera.restore();
    }
}
