package org.newtonproject.newtoncore.android.views.widget.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.newtonproject.newtoncore.android.R;
/**
 * @author weixuefeng@lubangame.com
 * @version $Rev$
 * @time: 2018/9/26--PM 3:25
 * @description
 * @copyright (c) 2018 Newton Foundation. All rights reserved.
 */
public class IntroPagerAdapter extends PagerAdapter {

    private int[] titles = new int[] {
            R.string.guide_title1,
            R.string.guide_title2,
            R.string.guide_title3,
    };
    private int[] messages = new int[] {
            R.string.guide_message1,
            R.string.guide_message2,
            R.string.guide_message3,
            R.string.guide_message4,
    };
    private int[] images = new int[] {
            R.mipmap.img_intro_04,
            R.mipmap.img_intro_01,
            R.mipmap.img_intro_02,
            R.mipmap.img_intro_03
    };

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.layout_page_intro, container, false);
        //((TextView) view.findViewById(R.id.title)).setText(titles[position]);
        ((TextView) view.findViewById(R.id.message)).setText(messages[position]);
        ((ImageView) view.findViewById(R.id.img)).setImageResource(images[position]);
        container.addView(view);
        Log.e("TAG", view.getHeight() + "");
        return view;
    }

    @Override
    public int getCount() {
        return messages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public static class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

}
