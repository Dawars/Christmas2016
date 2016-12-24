package me.dawars.christmas2016;

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by dawars on 12/23/16.
 */

public class NetworkThread extends Thread {
    private static final String TAG = NetworkThread.class.getSimpleName();

    private final Gpio[] mLedGpio;
    private final PeripheralManagerService manager;

    public NetworkThread(PeripheralManagerService manager, Gpio[] mLedGpio) {
        this.manager = manager;
        this.mLedGpio = mLedGpio;
    }

    @Override
    public void run() {
        try {
            receivePacket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receivePacket() throws IOException {
        DatagramSocket socket = null;
        socket = new DatagramSocket(1234);


        byte[] buf = new byte[1000];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);
        while (true) {
            Log.v("socket", "Receiving packet");
            socket.receive(dp);
            String rcvd = new String(dp.getData(), 0, dp.getLength()) + ", from address: "
                    + dp.getAddress() + ", port: " + dp.getPort();
            Log.i("Packet received", rcvd);


            String[] data = new String(dp.getData(), 0, dp.getLength()).split(" ");
            if (data.length > 1) {
                int num = Integer.valueOf(data[1]);
                if (num >= 0 && num < mLedGpio.length) {
                    switch (data[0].toLowerCase()) {
                        case "off":
                            mLedGpio[num].setValue(false);
                            Log.i(TAG, "Turning LED" + num + " Off");
                            Log.i(TAG, mLedGpio[num].getValue() ? "ON" : "OFF");
                            break;
                        case "on":
                            mLedGpio[num].setValue(true);
                            Log.i(TAG, "Turning LED" + num + " On");
                            Log.i(TAG, mLedGpio[num].getValue() ? "ON" : "OFF");
                            break;
                    }
                }
            }
        }
    }
}