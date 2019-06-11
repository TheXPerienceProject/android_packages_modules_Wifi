/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.server.wifi;

import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;

import com.android.server.wifi.util.WifiAsyncChannel;

/**
 * Manages the wifi service instance.
 */
public final class WifiService implements WifiServiceBase {

    private static final String TAG = "WifiService";
    final WifiServiceImpl mImpl;

    public WifiService(Context context) {
        mImpl = new WifiServiceImpl(context, new WifiInjector(context), new WifiAsyncChannel(TAG));
    }

    @Override
    public void onStart() {
        Log.i(TAG, "Registering " + Context.WIFI_SERVICE);
        ServiceManager.addService(Context.WIFI_SERVICE, mImpl);

        Log.i(TAG, "Starting " + Context.WIFI_SERVICE);
        mImpl.checkAndStartWifi();

        // Trigger all the necessary boot completed actions, since we are starting late now.
        mImpl.handleBootCompleted();
    }

    @Override
    public void onSwitchUser(int userId) {
        mImpl.handleUserSwitch(userId);
    }

    @Override
    public void onUnlockUser(int userId) {
        mImpl.handleUserUnlock(userId);
    }

    @Override
    public void onStopUser(int userId) {
        mImpl.handleUserStop(userId);
    }
}
