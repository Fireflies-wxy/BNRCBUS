package com.bnrc.util;

import android.util.SparseArray;
import android.view.View;

/**
 * 漏 2012 amsoft.cn
 * 鍚嶇О锛欰bViewHolder.java 
 * 鎻忚堪锛氳秴绠�娲佺殑ViewHolder.
 * 浠ｇ爜鏇寸畝鍗曪紝鎬ц兘鐣ヤ綆锛屽彲浠ュ拷鐣�
 * @author 杩樺涓�姊︿腑
 * @version v1.0
 * @date锛�2014-06-17 涓嬪崍20:32:13
 */
public class AbViewHolder {
    
    /**
     * ImageView view = AbViewHolder.get(convertView, R.id.imageView);
     * @param view
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
