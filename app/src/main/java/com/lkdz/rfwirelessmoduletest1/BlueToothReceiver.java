

package com.lkdz.rfwirelessmoduletest1;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BlueToothReceiver extends BroadcastReceiver {


    private String btMessage = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
            btMessage = device.getName() + "正在断开蓝牙连接。。。";
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            btMessage = device.getName() + "蓝牙已断开!!";

        }else {
            return;
        }


        intent.putExtra("Bluetooth", btMessage);
        intent.setClass(context, RelayControl.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

}
