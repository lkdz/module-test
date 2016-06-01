package com.lkdz.rfwirelessmoduletest1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lkdz.rfwirelessmoduletest1.adapter.MyExpandableListViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static com.lkdz.rfwirelessmoduletest1.R.id.progress;

public class RelayControl extends AppCompatActivity {

    static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";


    //    ExpandableListView list;
    private Handler _handler = new Handler();


    /* 取得默认的蓝牙适配器 */
    private BluetoothAdapter btadapter;

    private Runnable _discoveryWorkder;


    private ListView lv, llv;

    ExpandableListAdapter mAdapter;

    boolean ON = false;
    //搜索蓝牙设备类型
    private final int UNCATEGORIZED=7936;

    /* 请求打开蓝牙 */
    private static final int REQUEST_ENABLE = 0x1;

    /* 请求能够被搜索 */
    private static final int REQUEST_DISCOVERABLE = 0x2;

    /* 用来存储搜索到的蓝牙设备 */
    private ArrayList<String> _devices = new ArrayList<>();

    public static BluetoothSocket btSocket;

    private RelativeLayout seach;

    ArrayAdapter<String> adtDevices;

    private String blueString;

    /* 是否完成搜索 */
    private volatile boolean _discoveryFinished;

    private String[] g = {"已配对的设备", "可用设备"};

    ArrayList<String> yipeiuidevice = new ArrayList<>();

    ArrayList<String> groupList;

    ArrayList<ArrayList<String>> childList;

    ExpandableListView list;

    private ImageView searchimg;


private ProgressBar mprogressBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_relay_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.re_toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        mprogressBar= (ProgressBar) findViewById(progress);
        btadapter = BluetoothAdapter.getDefaultAdapter();

        isbluetooth();
        Set<BluetoothDevice> pairedDevices = btadapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice devices : pairedDevices) {
                if (devices.getBluetoothClass().getMajorDeviceClass() == UNCATEGORIZED) {
                    yipeiuidevice.add(devices.getName() + "|" + devices.getAddress());

                }
            }
        }


        groupList = new ArrayList<String>();
        for (int i = 0; i < g.length; i++) {
            groupList.add(g[i]);
        }


        childList = new ArrayList<ArrayList<String>>();
        childList.add(yipeiuidevice);
        childList.add(_devices);


        list = (ExpandableListView) findViewById(R.id.lanya_shebei_expandlistview);
        mAdapter = new MyExpandableListViewAdapter(groupList, childList, this);

        list.setAdapter(mAdapter);

      list.setOnChildClickListener(new ItemClickEvent());

        list.setCacheColorHint(0x00000000);
        list.setSelector(new ColorDrawable(Color.TRANSPARENT));
        list.setGroupIndicator(null);
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            list.expandGroup(i);
        }


//         注册Receiver来获取蓝牙设备相关的结果
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(searchDevices, intent);

    }



    private void isbluetooth() {


        if (!btadapter.isEnabled()) {
            btadapter.enable();
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(enabler, REQUEST_DISCOVERABLE);
        }

        if (!btadapter.isDiscovering()) {
            btadapter.startDiscovery();
            setProgressBarVisibility(true);
        }
        seach = (RelativeLayout) findViewById(R.id.lanya_shebei_search);
        searchimg = (ImageView) findViewById(R.id.lanya_shuaxing);
        searchimg.setVisibility(View.INVISIBLE);
        seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btadapter.isDiscovering()){
                    btadapter.cancelDiscovery();
                    mprogressBar.setVisibility(View.INVISIBLE);
                    searchimg.setVisibility(View.VISIBLE);
                }else if (!btadapter.isDiscovering()){
                    _devices.clear();
                    btadapter.startDiscovery();
                    mprogressBar.setVisibility(View.VISIBLE);
                    setProgressBarVisibility(true);
                    searchimg.setVisibility(View.INVISIBLE);
                }else {
                    return;
                }

            }

        });


    }


    private BroadcastReceiver searchDevices = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();

            // 显示所有收到的消息及其细节
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
                Log.e(keyName, String.valueOf(b.get(keyName)));
            }


            // 搜索设备时，取得设备的MAC地址
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String str = device.getName() + "|" + device.getAddress();
                if (_devices.indexOf(str) == -1 && device.getBluetoothClass().getMajorDeviceClass()==UNCATEGORIZED)// 防止重复添加
                    _devices.add(str); // 获取设备名称和mac地址
                childList.add(_devices);
                list.expandGroup(1);
                list.collapseGroup(1);
                list.expandGroup(1);

            }
        }

    };

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(searchDevices);
        btadapter.cancelDiscovery();
        super.onDestroy();

    }

    class ItemClickEvent implements ExpandableListView.OnChildClickListener {
        @Override
        public boolean onChildClick(ExpandableListView lv, View v, int groupPosition, int childPosition, long id) {
            btadapter.cancelDiscovery();
            String str =childList.get(groupPosition).get(childPosition);
            String[] values = str.split("\\|");
            String address = values[1];

            UUID uuid = UUID.fromString(SPP_UUID);
            BluetoothDevice btDev = btadapter.getRemoteDevice(address);
            try {
                btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
               int i= btDev.getBluetoothClass().getDeviceClass();
                Log.i("wwwww",""+i);
                btSocket.connect();
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("result", address);
                //设置返回数据
                RelayControl.this.setResult(RESULT_OK, intent);
                //关闭Activity
                RelayControl.this.finish();
                BluetoothService.getInstance().setBluetoothSocket(btSocket);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(RelayControl.this, "连接失败...", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rela, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_lanya_cearch) {


        } else if (id == R.id.action_lanya_cearch) {


        }

        return super.onOptionsItemSelected(item);
    }
}
