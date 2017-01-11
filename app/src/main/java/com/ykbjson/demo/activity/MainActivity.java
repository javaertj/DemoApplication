package com.ykbjson.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ykbjson.demo.R;
import com.ykbjson.demo.customview.ptrheader.CustomerPtrHandler;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    CustomerPtrHandler header;
    PtrClassicFrameLayout ptrRefresh;
    private Runnable testTunner = new Runnable() {
        @Override
        public void run() {
            ptrRefresh.refreshComplete();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "chrift.otf");
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                View view = getDelegate().createView(parent, name, context, attrs);
                if (null != view && view instanceof TextView) {
                    ((TextView) view).setTypeface(typeface);
                }
                return view;
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TestJsActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ptrRefresh = (PtrClassicFrameLayout) findViewById(R.id.ptr_frame);
        initRefreshHandler();
    }


    /**
     * 初始化下拉刷新
     */
    private void initRefreshHandler() {
        ptrRefresh.setEnabled(false);
        header = new CustomerPtrHandler(this);
        ptrRefresh.setHeaderView(header);
        ptrRefresh.addPtrUIHandler(header);
        ptrRefresh.setPullToRefresh(false);
        ptrRefresh.disableWhenHorizontalMove(true);//避免跟水平滑动的组件冲突
        ptrRefresh.setLastUpdateTimeRelateObject(this);
        ptrRefresh.setLoadingMinTime(2000);
        ptrRefresh.setDurationToCloseHeader(3000);
        ptrRefresh.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                ptrRefresh.postDelayed(testTunner, 3000);
            }
        });
    }

//    /**
//     * 初始化下拉刷新
//     */
//    private void initRefreshHandler() {
//        final MaterialHeader header = new MaterialHeader(this);
//        int[] colors = getResources().getIntArray(R.array.google_colors);
//        header.setColorSchemeColors(colors);
//        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
//        header.setPadding(0, ToolUnit.dipTopx(15), 0, ToolUnit.dipTopx(10));
////        header.setPtrFrameLayout(layout_refresh);
//        layout.setLoadingMinTime(1000);
//        layout.setDurationToCloseHeader(1500);
//        layout.setHeaderView(header);
//        layout.addPtrUIHandler(header);
//
//        layout.setPtrHandler(new PtrHandler() {
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                frame.postDelayed(testTunner, 1000);
//            }
//        });

//        layout.setLastUpdateTimeKey(lastUpdateTime);
//        layout.setLoadingMinTime(1000);
//        layout.setDurationToCloseHeader(1500);
//
//        layout.setPtrHandler(new PtrHandler() {
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
//                getData();
//            }
//        });
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(this, TestPullToZoomActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this, ExamineAssistantActivity.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(this, TestTravelListActivity.class));
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
