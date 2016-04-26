package com.ccnu.james.imagescan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private MyImageTopView mTopView;
    private LinearLayout mBottomView;
    private int[] imgIds = new int[]{R.drawable.a, R.drawable.b, R.drawable.c,
            R.drawable.d, R.drawable.e, R.drawable.f};
    public ImageView[] imgViews = new ImageView[imgIds.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomView = (LinearLayout) findViewById(R.id.mBottomView);
        mTopView = (MyImageTopView) findViewById(R.id.mTopView);
        initBottom();
        mTopView.initImage(imgIds);
    }

    public void initBottom() {
        //初始化底部的圆
        for (int i = 0; i < imgViews.length; i++) {
            imgViews[i] = new ImageView(this);
            if (i == 0) {
                imgViews[i].setImageResource(R.drawable.choosed);
            } else {
                imgViews[i].setImageResource(R.drawable.unchoosed);
            }
            imgViews[i].setPadding(15, 0, 0, 0);
            imgViews[i].setId(i);
            imgViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetImg();//使所有圆圈都不处于选中的状态
                    ((ImageView) v).setImageResource(R.drawable.choosed);//使当前圆圈处于选中状态
                    mTopView.scrollToImage(v.getId());
                }
            });
            mBottomView.addView(imgViews[i]);
        }
    }

    public void resetImg() {
        //重置所有圆圈为未选中状态
        for (int i = 0; i < imgViews.length; i++) {
            imgViews[i].setImageResource(R.drawable.unchoosed);
        }
    }
}
