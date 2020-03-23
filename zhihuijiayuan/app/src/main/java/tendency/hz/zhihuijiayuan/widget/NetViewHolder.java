package tendency.hz.zhihuijiayuan.widget;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhpan.bannerview.holder.ViewHolder;
import com.zhpan.bannerview.utils.BannerUtils;

import tendency.hz.zhihuijiayuan.R;

/**
 * Author：Libin on 2020-03-23 15:06
 * Email：1993911441@qq.com
 * Describe：
 */
public class NetViewHolder implements ViewHolder<String> {

    @Override
    public int getLayoutId() {
        return R.layout.layout_banner_item;
    }

    @Override
    public void onBind(View itemView, String data, int position, int size) {
        SimpleDraweeView imageView = itemView.findViewById(R.id.iv_banner_item);

        imageView.setImageURI(data);
    }
}
