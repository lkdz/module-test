package com.lkdz.rfwirelessmoduletest1;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.lkdz.rfwirelessmoduletest1.adapter.FragmentViwPagerAdapter;
import com.lkdz.rfwirelessmoduletest1.fragment.ParticularsPagerFragment;
import com.lkdz.rfwirelessmoduletest1.fragment.SimplenessPagerFragment;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static boolean isRecording = false;// 线程控制标记
    public static int state = 0;
    public static boolean isSended = false;

    private ToggleButton tgbt1, tgbt2, tgbt3, tgbt4;

    private Button releaseCtrl, btBack, btSend;

    private OutputStream outStream = null;

    private EditText _txtRead, _txtSend;

//    private ConnectedThread manageThread;
    private Handler mHandler;

    private RadioGroup radioType;
    private RadioButton rbPC;
    private String encodeType = "GBK";

    long receiveTimeout = 1000;
    public static RelayControl self;
    TextView tv_expression, tv_send_msg;
    EditText et_id;
    LinearLayout ll_expression;
    public LinearLayout ll_vp_selected_index;
    private BlueToothReceiver myBroadcastReceiver;

    CheckBox cc;

    ListView lv_id;
    TextView tv_chat_title;
    private String blueString;
    TextView ON;
    private ViewPager mPageVp;
    private TextView textView;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private FragmentViwPagerAdapter mFragmentAdapter;
    private SimplenessPagerFragment slpagerfg;
    private ParticularsPagerFragment plpagerfg;
    // ViewPager的当前选中页
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_title);
        toolbar.setSubtitle(R.string.toolbarsub_title);
        setSupportActionBar(toolbar);


        findById();
        init();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.popuwpwindow_layout, null);
        // 创建PopupWindow对象
        final PopupWindow pop = new PopupWindow(view, Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, false);

        textView= (TextView) findViewById(R.id.text_xh);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(pop.isShowing()) {
                    // 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
                    pop.dismiss();
                } else {
                    // 显示窗口
                    pop.showAsDropDown(v);

                }

            }
        });
    }
public void findById(){
    et_id = (EditText) findViewById(R.id.et_id);
    et_id.addTextChangedListener(new EditTextUtil(et_id));

    mPageVp= (ViewPager) findViewById(R.id.viewpager);
}
    private void init() {
        slpagerfg = new SimplenessPagerFragment();
        plpagerfg = new ParticularsPagerFragment();
        mFragmentList.add(slpagerfg);
        mFragmentList.add(plpagerfg);

        mFragmentAdapter = new FragmentViwPagerAdapter(
                this.getSupportFragmentManager(), mFragmentList);
        mPageVp.setAdapter(mFragmentAdapter);
        mPageVp.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /**
     * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
     *
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据

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
        }else if (id==R.id.action_lanya_cearch){

            startActivityForResult(new Intent(MainActivity.this, RelayControl.class), 1);
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
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
