package yunstudio2015.android.yunmeet.activityz;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.MainVpAdapter;
import yunstudio2015.android.yunmeet.app.MyApplication;
import yunstudio2015.android.yunmeet.customviewz.SlidingTabLayout;

public class ShowActivity extends AppCompatActivity {


    @Nullable @Bind(R.id.toolbar_show)
    Toolbar toolbar;

    // 包括 、广场  、关注的那个菜单
    @Bind(R.id.stl_menu)
    SlidingTabLayout slidingMenu;

    @Bind(R.id.viewpager_container)
    ViewPager vp_container;


    @Bind(R.id.hsc_lny)
    LinearLayout hsc_lny;


    // varz
    OnTopCategoryMenuClickListener tab_category_menu_listener;
      DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);

        /**
         * 1、 把xml文件里的toolbar设计成当前activity的默认actionbar
         * 2、 把菜单添加上去
         */
        tab_category_menu_listener = new OnTopCategoryMenuClickListener();
        setUpFragments (vp_container);
        setUpCategoriesHv(hsc_lny);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setUpSlidingTabs(slidingMenu);
        }
        // 创建一个activity基于两个不同的借口去实现数据及信息展示
    }

    private void setUpCategoriesHv(LinearLayout hsc_lny) {

        // standard categories, and other categories could be got the db
        // we doing this so that in case of no connection, there is
        // still some categories to show if we want to subdivise
        // or get a way to actualize the activity local database everytime it got
        // some changes.

        // get the default categories list inside the database, if it doesnt exist,
        // then change it inside this file

        // 从本低那获取默认的类型
        String[] default_categories = getResources().getStringArray(R.array.default_categories);


        // we just create items and add them with a predefined width

//        TtDebug("sw " + getScreenWidth(this) + " hscw " + hsc_categories.getWidth());
        int usewidth = getScreenWidth(this);

        hsc_lny.removeAllViews();

        for (int i = 0; i <default_categories.length; i++) {

            TextView tv_tmp = new TextView(this);
            tv_tmp.setText(default_categories[i]);
            tv_tmp.setTextColor(getResources().getColor(R.color.actionbar_color));
            tv_tmp.setTag(R.id.category_id, i);
            tv_tmp.setTag(R.id.category_position_id, i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.width = usewidth/6;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tv_tmp.setLayoutParams(params);
            tv_tmp.setOnClickListener(tab_category_menu_listener);
            if (i == 0)
                params.leftMargin =  getResources().getDimensionPixelSize(R.dimen.hsc_left_margin);
            else if (i == default_categories.length -1)
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.hsc_right_margin);
            hsc_lny.addView(tv_tmp);
            // give to all of them the same listener and make
            // do his job.
            if (i == 0)
                tv_tmp.setTextColor(getResources().getColor(R.color.little_red));
            // each child will keep an id for himself
        }
    }



    private void setUpFragments(ViewPager vp_container) {

        MainVpAdapter adap = new MainVpAdapter(getSupportFragmentManager());
        vp_container.setAdapter(adap);
    }

    private void setUpSlidingTabs(SlidingTabLayout tabs) {

        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true,
        // This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.little_red);
            }
        });
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(vp_container);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void TtDebug(String s) {
        if (MyApplication.debug)
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public DisplayMetrics getMetrics (Context context) {
        if (metrics == null) {
         metrics = new DisplayMetrics();
            ((ShowActivity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
            return metrics;
    }

    // utils functions
    public static int getScreenWidth (Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((ShowActivity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }


    // inner classes
    class OnTopCategoryMenuClickListener implements View.OnClickListener {


        private int previous_selected = 0;
        @Override
        public void onClick(View view) {

            // change the fragment in the current viewpager page.
            // deselect the previous clicked
            // and select the clicked one.
            if (view.getTag(R.id.category_position_id) != previous_selected) {
                ((TextView)hsc_lny.getChildAt(previous_selected)).setTextColor(getResources().getColor(R.color.actionbar_color));
                ((TextView) view).setTextColor(getResources().getColor(R.color.little_red));
                previous_selected = (int) view.getTag(R.id.category_position_id);
                TtDebug(getResources().getStringArray(R.array.default_categories)[previous_selected]);
            }
        }
    }

}
