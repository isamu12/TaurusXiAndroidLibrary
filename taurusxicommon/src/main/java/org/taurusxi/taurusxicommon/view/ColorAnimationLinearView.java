package org.taurusxi.taurusxicommon.view;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created on 15/2/12.
 *
 * @author xicheng
 * @email 505591443@qq.com
 * @github https://github.com/TaurusXi
 */
public class ColorAnimationLinearView
        extends LinearLayout
        implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private int[] colors;

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    private static final int DURATION = 3000;
    ValueAnimator colorAnim = null;

    private PageChangeListener mPageChangeListener;

    ViewPager.OnPageChangeListener onPageChangeListener;

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }


    /**
     * 这是你唯一需要关心的方法
     * @param mViewPager  你必须在设置 Viewpager 的 Adapter 这后，才能调用这个方法。
     * @param obj ,这个obj实现了 ColorAnimationView.OnPageChangeListener ，实现回调
     * @param count   ,viewpager孩子的数量
     * @param colors int... colors ，你需要设置的颜色变化值~~ 如何你传人 空，那么触发默认设置的颜色动画
     * */
    /**
     * This is the only method you need care about.
     *
     * @param mViewPager ,you need set the adpater before you call this.
     * @param count      ,this param set the count of the viewpaper's child
     * @param colors     ,this param set the change color use (int... colors),
     *                   so,you could set any length if you want.And by default.
     *                   if you set nothing , don't worry i have already creat
     *                   a default good change color!
     */
    public void setmViewPager(ViewPager mViewPager, int count, int[] colors) {
        if (mViewPager.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        mPageChangeListener.setViewPagerChildCount(count);
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        setColors(colors);
    }

    public ColorAnimationLinearView(Context context) {
        this(context, null, 0);

    }

    public ColorAnimationLinearView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorAnimationLinearView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPageChangeListener = new PageChangeListener();
    }

    private void seek(long seekTime) {
        if (colorAnim == null) {
            createAnimation();
        }
        colorAnim.setCurrentPlayTime(seekTime);
    }

    private void createAnimation() {
        if (colorAnim == null) {
            colorAnim = ObjectAnimator.ofInt(this,
                    "backgroundColor", colors);
            colorAnim.setEvaluator(new ArgbEvaluator());
            colorAnim.setDuration(DURATION);
            colorAnim.addUpdateListener(this);
        }
    }


    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        invalidate();
//		long playtime = colorAnim.getCurrentPlayTime();
    }

    private class PageChangeListener
            implements ViewPager.OnPageChangeListener {

        private int viewPagerChildCount;

        public void setViewPagerChildCount(int viewPagerChildCount) {
            this.viewPagerChildCount = viewPagerChildCount;
        }

        public int getViewPagerChildCount() {
            return viewPagerChildCount;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            int count = getViewPagerChildCount() - 1;
            if (count != 0) {
                float length = (position + positionOffset) / count;
                int progress = (int) (length * DURATION);
                ColorAnimationLinearView.this.seek(progress);
            }
            // call the method by default
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

        }

        @Override
        public void onPageSelected(int position) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }
}


