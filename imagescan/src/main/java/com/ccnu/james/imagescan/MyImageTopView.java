package com.ccnu.james.imagescan;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by James on 2016/4/26.
 */
public class MyImageTopView extends ViewGroup {
    private GestureDetector gesDetector;//手势检测器
    private Scroller scroller;//滚动对象
    private int currentImageIndex = 0;
    private boolean fling = false;

    private Handler mHandler;
    private Context context;

    public MyImageTopView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        init();
        this.setOnTouchListener(new MyOnTouchListener());
    }

    public void init() {
        scroller = new Scroller(context);
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0x11) {
                    //收到消息后，切换到指定的图片
                    scrollToImage((currentImageIndex + 1) % getChildCount());
                }
            }
        };
        gesDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                //手指按在触摸屏上，它的时间范围在按下起效，在长按之前
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                //手指离开触摸屏的一瞬间调用该方法
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //手指在触摸屏上滑动，如果滑动范围在第一页和最后一页之间，distanceX>0表示向右滑动，distanceY《0表示向左滑动
                //如果超出了这个范围，则不做任何操作。
                if ((distanceX > 0 && getScrollX() < getWidth() * (getChildCount() - 1)) || (distanceX < 0 && getScrollX() > 0)) {
                    scrollBy((int) distanceX, 0);
                    //滚动的距离，在此只需水平滚动，垂直方向滚动为0；
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                //手指在屏幕上持续一段时间，并且没有松开
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //手指在屏幕上快速移动并松开，判断是否达到最小滑动速度，取绝对值
                if (Math.abs(velocityX) > ViewConfiguration.get(context).getScaledMinimumFlingVelocity()) {
                    //如果速度超过了最小速度
                    if (velocityX > 0 && currentImageIndex >= 0) {
                        fling = true;
                        scrollToImage((currentImageIndex - 1 + getChildCount()) % getChildCount());
                    } else if (velocityX < 0 && currentImageIndex <= getChildCount() - 1) {
                        fling = true;
                        scrollToImage((currentImageIndex + 1) % getChildCount());
                    }
                }
                return true;
            }
        });

        Timer timer = new Timer();//创建定时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0x11);
            }
        }, 0, 8000);
    }

    public void scrollToImage(int targetIndex) {
        //跳转到目标图片
        if (targetIndex != currentImageIndex && getFocusedChild() != null && getFocusedChild() == getChildAt(currentImageIndex)) {
            getFocusedChild().clearFocus();
        }
        final int delta = targetIndex * getWidth() - getScrollX();//需要滑动的距离
        int time = Math.abs(delta) * 5;//滑动时间是滑动距离的五倍；
        scroller.startScroll(getScrollX(), 0, delta, 0, time);
        invalidate();
        currentImageIndex = targetIndex;
        ((MainActivity) context).resetImg();//改变下方●状态
        ((MainActivity) context).imgViews[currentImageIndex].
                setImageResource(R.drawable.choosed);


    }

    public void computeScroll() {
        //重写父类方法，记录滚动条的新位置
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    private class MyOnTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gesDetector.onTouchEvent(event);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (!fling) {
                    snapToDestination();
                }
                fling = false;
            }
            return true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setVisibility(View.VISIBLE);
            child.measure(r - l, b - t);
            child.layout(i * getWidth(), 0, (i + 1) * getWidth(), getHeight());
        }
    }

    private void snapToDestination() {
        scrollToImage((getScrollX() + (getWidth() / 2)) / getWidth());
    }

    public void initImage(int[] imgIds) {
        int num = imgIds.length;
        this.removeAllViews();
        for (int i = 0; i < num; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(imgIds[i]);
            this.addView(imageView);
        }

    }
}
