package com.kircherelectronics.accelerationexplorer.activity;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.kircherelectronics.accelerationexplorer.R;
import com.kircherelectronics.accelerationexplorer.datalogger.DataLoggerManager;
import com.kircherelectronics.accelerationexplorer.prefs.PrefUtils;
import com.kircherelectronics.accelerationexplorer.view.VectorDrawableButton;
import com.kircherelectronics.accelerationexplorer.viewmodel.SensorViewModel;

/*
 * AccelerationExplorer
 * Copyright 2018 Kircher Electronics, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * An Activity that plots the three axes outputs of the acceleration sensor in
 * real-time, as well as displays the tilt of the device and acceleration of the
 * device in two-dimensions. The acceleration sensor can be logged to an
 * external .CSV file.
 *
 * @author Kaleb
 * @version %I%, %G%
 */
public class LoggerActivity extends AppCompatActivity {
    // Plot keys for the acceleration plot
    private final static String tag = LoggerActivity.class.getSimpleName();
    private final static int WRITE_EXTERNAL_STORAGE_REQUEST = 1000;
    private DataLoggerManager dataLogger;
    private boolean logData = false;
    private float[] acceleration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_logger);
        dataLogger = new DataLoggerManager(this);
        initViewModel();
        initStartButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logger, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected Identify single menu
     * item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Start the vector activity
            case R.id.action_help:
                showHelpDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        stopDataLog();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }
        }
    }

    private void initStartButton() {
        final VectorDrawableButton button = findViewById(R.id.button_start);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(LoggerActivity.this, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (!logData) {
                        button.setText("Stop Log");
                        startDataLog();
                    } else {
                        button.setText("Start Log");
                        stopDataLog();
                    }
                } else {
                    requestPermissions();
                }
            }
        });
    }

    private void showHelpDialog() {
        Dialog helpDialog = new Dialog(this);

        helpDialog.setCancelable(true);
        helpDialog.setCanceledOnTouchOutside(true);
        helpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = getLayoutInflater().inflate(R.layout.layout_help_logger,
                null);

        helpDialog.setContentView(view);

        helpDialog.show();
    }

    private void startDataLog() {
        logData = true;
        dataLogger.startDataLog();
    }

    private void stopDataLog() {
        logData = false;
        dataLogger.stopDataLog();
    }

    private void writeData() {
        if(logData) {
            dataLogger.setAcceleration(acceleration);
        }
    }

    private void initViewModel() {
        SensorViewModel model = ViewModelProviders.of(this).get(SensorViewModel.class);

        model.getLinearAccelerationSensorLiveData().removeObservers(this);
        model.getLowPassLinearAccelerationSensorLiveData().removeObservers(this);
        model.getComplimentaryLinearAccelerationSensorLiveData().removeObservers(this);
        model.getKalmanLinearAccelerationSensorLiveData().removeObservers(this);
        model.getAccelerationSensorLiveData().removeObservers(this);

        if(PrefUtils.getPrefAndroidLinearAccelerationEnabled(this)) {
            model.getLinearAccelerationSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    acceleration = floats;
                    writeData();
                }
            });
        } else if(PrefUtils.getPrefFSensorLpfLinearAccelerationEnabled(this)){
            model.getLowPassLinearAccelerationSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    acceleration = floats;
                    writeData();
                }
            });
        } else if(PrefUtils.getPrefFSensorComplimentaryLinearAccelerationEnabled(this)) {
            model.getComplimentaryLinearAccelerationSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    acceleration = floats;
                    writeData();
                }
            });
        } else if(PrefUtils.getPrefFSensorKalmanLinearAccelerationEnabled(this)) {
            model.getKalmanLinearAccelerationSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    acceleration = floats;
                    writeData();
                }
            });
        } else {
            model.getAccelerationSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    acceleration = floats;
                    writeData();
                }
            });
        }
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST);
        }
    }
}
