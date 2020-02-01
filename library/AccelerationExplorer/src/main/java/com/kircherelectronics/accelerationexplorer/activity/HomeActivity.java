package com.kircherelectronics.accelerationexplorer.activity;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.kircherelectronics.accelerationexplorer.R;
import com.kircherelectronics.accelerationexplorer.activity.config.FilterConfigActivity;
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
 * A class that provides a navigation menu to the features of Acceleration
 * Explorer.
 *
 * @author Kaleb
 */
public class HomeActivity extends AppCompatActivity  {
    private final static String tag = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_home);

        initButtonGauge();
        initButtonLogger();
        initButtonVector();
        initButtonSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
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

    private void initButtonGauge() {
        VectorDrawableButton button = (VectorDrawableButton) this.findViewById(R.id.button_gauge_mode);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,
                        GaugeActivity.class);

                startActivity(intent);
            }
        });
    }

    private void initButtonLogger() {
        VectorDrawableButton button = (VectorDrawableButton) this.findViewById(R.id.button_logger_mode);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,
                        LoggerActivity.class);

                startActivity(intent);
            }
        });
    }

    private void initButtonVector() {
        VectorDrawableButton button = (VectorDrawableButton) this.findViewById(R.id.button_vector_mode);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,
                        VectorActivity.class);

                startActivity(intent);
            }
        });
    }

    private void initButtonSettings() {
        VectorDrawableButton button = (VectorDrawableButton) this.findViewById(R.id.button_config_mode);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,
                        FilterConfigActivity.class);

                startActivity(intent);
            }
        });
    }

    private void showHelpDialog() {
        Dialog helpDialog = new Dialog(this);

        helpDialog.setCancelable(true);
        helpDialog.setCanceledOnTouchOutside(true);
        helpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = getLayoutInflater()
                .inflate(R.layout.layout_help_home, null);

        helpDialog.setContentView(view);

        helpDialog.show();
    }
}
