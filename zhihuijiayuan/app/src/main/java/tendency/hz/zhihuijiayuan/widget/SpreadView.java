package tendency.hz.zhihuijiayuan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.cjt2325.cameralibrary.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import cn.bertsir.zbar.Qr.SymbolSet;
import tendency.hz.zhihuijiayuan.R;
import tendency.hz.zhihuijiayuan.units.LogUtils;

/**
 * Author：Libin on 2020/3/26 0026 11:25
 * Email：1993911441@qq.com
 * Describe：
 */
public class SpreadView extends View {

    private final Paint spreadPaint;
    private  int radius;
    private  int maxRadius;
    private  int distance;
    private  Paint centerPaint;
    private  List<Integer> alphas = new ArrayList<>();

    private List<Integer> spreadRadius= new ArrayList<>();
    private int centerX;
    private int centerY;
    private int delayMilliseconds;

    public SpreadView(Context context) {
        this(context, null, 0);
    }
    public SpreadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public SpreadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpreadView, defStyleAttr, 0);
        radius = a.getInt(R.styleable.SpreadView_spread_radius, radius);
        maxRadius = a.getInt(R.styleable.SpreadView_spread_max_radius, maxRadius);
        int centerColor = a.getColor(R.styleable.SpreadView_spread_center_color, ContextCompat.getColor(context, R.color.colorAccent));
        int spreadColor = a.getColor(R.styleable.SpreadView_spread_spread_color, ContextCompat.getColor(context, R.color.colorAccent));
        distance = a.getInt(R.styleable.SpreadView_spread_distance, distance);
        delayMilliseconds = a.getInt(R.styleable.SpreadView_spread_delay_milliseconds,delayMilliseconds);

        a.recycle();
        centerPaint = new Paint();
        centerPaint.setColor(centerColor);
        centerPaint.setAntiAlias(true);
        //最开始不透明且扩散距离为0
        alphas.add(255);
        spreadRadius.add(0);
        spreadPaint = new Paint();
        spreadPaint.setAntiAlias(true);
        spreadPaint.setAlpha(255);
        spreadPaint.setColor(spreadColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //圆心位置
        centerX = w / 2;
        centerY = h / 2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < spreadRadius.size(); i++) {
            int alpha = alphas.get(i);
            spreadPaint.setAlpha(alpha);
            int width = spreadRadius.get(i);
            //绘制扩散的圆
            canvas.drawCircle(centerX, centerY, radius + width, spreadPaint);
            //每次扩散圆半径递增，圆透明度递减
            if (alpha > 0 && width < 300) {
                alpha = alpha - distance > 0 ? alpha - distance*3 : 1;
                alphas.set(i, alpha);
                spreadRadius.set(i, width + distance);
            }
        }
        //当最外层扩散圆半径达到最大半径时添加新扩散圆
        if (spreadRadius.get(spreadRadius.size() - 1) > maxRadius) {
            spreadRadius.add(0);
            alphas.add(255);
        }
        //超过8个扩散圆，删除最先绘制的圆，即最外层的圆
        if (spreadRadius.size() >= 2) {
            alphas.remove(0);
            spreadRadius.remove(0);
        }
        //中间的圆
        canvas.drawCircle(centerX, centerY, radius, centerPaint);
        //TODO 可以在中间圆绘制文字或者图片
        //延迟更新，达到扩散视觉差效果
        postInvalidateDelayed(delayMilliseconds);
    }
}
