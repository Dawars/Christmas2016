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

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Gpio[] mLedGpios = new Gpio[15];
    private NetworkThread thread;
    private static final String[] gpioNames = new String[]{
            "BCM27",
            "BCM17",
            "BCM5",
            "BCM6",
            "BCM13",
            "BCM19",
            "BCM26",
            "BCM21",
            "BCM20",
            "BCM16",
            "BCM12",
            "BCM25",
            "BCM4",
            "BCM23",
            "BCM18",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        PeripheralManagerService manager = new PeripheralManagerService();
        try {
            for (int i = 0; i < mLedGpios.length; i++) {
                mLedGpios[i] = manager.openGpio(gpioNames[i]);
                mLedGpios[i].setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        thread = new NetworkThread(manager, mLedGpios);
        thread.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        try {
            if (mLedGpios != null) {
                for (Gpio gpio :
                        mLedGpios) {
                    gpio.setValue(false);
                    gpio.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            thread.join();
            thread = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
