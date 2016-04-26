package com.ccnu.james.actionbardemo;

import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ActionBar mActionBar;
    private String[] titles = new String[] {"学校","collage","class"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionBar = this.getActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


    }
    private class MyTabListener implements android.app.ActionBar.TabListener {
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            //选项页被选中事件处理
            String tabText = tab.getText().toString();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            if(tabText.equalsIgnoreCase("学校")) {
                fragmentTransaction.replace();
            }
            fragmentTransaction.commit();
        }
        public void onTabUnselected (android.app.ActionBar.Tab tab, FragmentTransaction ft) {}

        public void onTabReselected(Tab tab,FragmentTransaction ft) {}

    }
}
