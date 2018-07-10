/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.example.android.mobileperf.battery;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class WaitForPowerActivity extends ActionBarActivity {
    public static final String LOG_TAG = "WaitForPowerActivity";

    TextView mPowerMsg;
    ImageView mCheyennePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power);

        mPowerMsg = (TextView) findViewById(R.id.cheyenne_txt);
        mCheyennePic = (ImageView) findViewById(R.id.cheyenne_img);

        Button theButtonThatTakesPhotos = (Button) findViewById(R.id.power_take_photo);
        theButtonThatTakesPhotos.setText(R.string.take_photo_button);

        final Button theButtonThatFiltersThePhoto = (Button) findViewById(R.id.power_apply_filter);
        theButtonThatFiltersThePhoto.setText(R.string.filter_photo_button);

        theButtonThatTakesPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                // After we take the photo, we should display the filter option.
                theButtonThatFiltersThePhoto.setVisibility(View.VISIBLE);
            }
        });

        theButtonThatFiltersThePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilter();
            }
        });
    }

    /**
     * These are placeholder methods for where your app might do something interesting! Try not to
     * confuse them with functional code.
     *
     * In this case, we are showing how your app might want to manipulate a photo a user has
     * uploaded--perhaps by performing facial detection, applying filters, generating thumbnails,
     * or backing up the image. In many instances, these actions might not be immediately necessary,
     * and may even be better done in batch. In this sample, we allow the user to "take" a photo,
     * and then "apply" a simple magenta filter to the photo. For brevity, the photos are already
     * included in the sample.
     */
    private void takePhoto() {
        // Make photo of Cheyenne appear.
        mPowerMsg.setText(R.string.photo_taken);
        mCheyennePic.setImageResource(R.drawable.cheyenne);
    }

    private void applyFilter() {
        if (checkForPower()){
            mCheyennePic.setImageResource(R.drawable.pink_cheyenne);
            mPowerMsg.setText(R.string.photo_filter);
        } else{
            Toast.makeText(this, "Plug in the device to apply filter", Toast.LENGTH_SHORT).show();
        }

    }

    // checks if the device is charging
    private boolean checkForPower(){
        // It is very easy to subscribe to changes to the battery state, but you can get the current
        // state by simply passing null in as your receiver.  Nifty, isn't that?
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, filter);
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean acCharge = (chargePlug == BatteryManager.BATTERY_PLUGGED_AC);
        boolean usbCharge = (chargePlug == BatteryManager.BATTERY_PLUGGED_USB);
        boolean wCharge = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            wCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;

        }
        return (acCharge || usbCharge || wCharge);
    }
}
