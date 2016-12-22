/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.dawars.christmas2016;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

/**
 * Skeleton of the main Android Things activity. Implement your device's logic
 * in this class.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 */
public class MainActivity extends Activity {
    private static final String TAG = me.dawars.christmas2016.MainActivity.class.getSimpleName();
    private Gpio mLedGpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");


        PeripheralManagerService manager = new PeripheralManagerService();
        List<String> portList = manager.getGpioList();
        if (portList.isEmpty()) {
            Log.i(TAG, "No GPIO port available on this device.");
        } else {
            Log.i(TAG, "List of available ports: " + portList);
        }
        List<String> pwmList = manager.getPwmList();
        if (pwmList.isEmpty()) {
            Log.i(TAG, "No PWM port available on this device.");
        } else {
            Log.i(TAG, "List of available ports: " + pwmList);
        }

        try {
            mLedGpio = manager.openGpio("BCM6");
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
//            mLedGpio.setValue(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

            String data = new String(dp.getData(), 0, dp.getLength());
            switch (data) {
                case "OFF":
                    mLedGpio.setValue(false);
                    Log.v("command", "turning off");

                    break;
                case "ON":
                    mLedGpio.setValue(true);
                    Log.v("command", "turning on");
                    break;
            }
            Log.v("LED STATE", mLedGpio.getValue() + "");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        try {
            mLedGpio.setValue(false);
            mLedGpio.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}