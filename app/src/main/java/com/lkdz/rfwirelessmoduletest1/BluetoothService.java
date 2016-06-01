package com.lkdz.rfwirelessmoduletest1;

import android.bluetooth.BluetoothSocket;
import android.support.annotation.Nullable;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by DELL on 2016/5/22.
 */
public class BluetoothService {
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean isSending;    // 是否正在发送数据
    private boolean isRecving;    // 是否正在接收数据

    private BluetoothService() {

    }

    private static final BluetoothService btSrv = new BluetoothService();

    public static BluetoothService getInstance() {
        return btSrv;
    }


    public boolean setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        try {
            if (isSending || isRecving) {
                return false;
            }

            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean Send(byte[] out) {
        try {
            isSending = true;
            outputStream.write(out);
            outputStream.flush();

            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        finally {
            isSending = false;
        }
    }

    @Nullable
    public byte[] Receive(int timeout) {
        try {
            isRecving = true;
            byte[] ret;
            long sendTime = System.currentTimeMillis(); // 记录发送数据时间
            while (true) {
                Thread.sleep(50);  // 50毫秒去查看一次接收缓冲区是否有数据
                if (inputStream.available() > 0) {
                    // 发现有数据进来一次性接收完毕
                    byte[] buffer = new byte[128];  // 最大长度由协议决定，目前128足够用了
                    int bytes = 0; // 已经成功读取的字节的个数
                    while (inputStream.available() > 0) {
                        bytes += inputStream.read(buffer, bytes, buffer.length - bytes);
                        Thread.sleep(500);  // 蓝牙数据可能分段接收，延迟下看后续是否有数据
                    }
                    ret = new byte[bytes];
                    System.arraycopy(buffer, 0, ret, 0, ret.length);
                    return ret;
                }
                // 看是否接收超时
                if ((System.currentTimeMillis() - sendTime) > timeout) {
                    throw new Exception("接收数据超时");
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        finally {
            isRecving = false;
        }
    }

    public void ClearInputStreamBuffer() {
        try {
            if (inputStream.available() > 0) {
                inputStream.read(new byte[inputStream.available()]);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
