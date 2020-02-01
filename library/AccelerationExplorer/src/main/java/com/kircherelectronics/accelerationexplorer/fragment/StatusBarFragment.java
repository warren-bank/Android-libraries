package com.kircherelectronics.accelerationexplorer.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kircherelectronics.accelerationexplorer.R;
import com.kircherelectronics.accelerationexplorer.prefs.PrefUtils;
import com.kircherelectronics.accelerationexplorer.viewmodel.SensorViewModel;

import java.util.Locale;

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
 * Created by kaleb on 7/7/17.
 */

public class StatusBarFragment extends Fragment {

    private static final String tag = StatusBarFragment.class.getSimpleName();

    // Text views for real-time output
    private TextView textViewXAxis;
    private TextView textViewYAxis;
    private TextView textViewZAxis;
    private TextView textViewHzFrequency;

    private Handler handler;
    private Runnable runnable;

    private float[] acceleration;

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                handler.postDelayed(this, 20);
                updateAccelerationText();
            }
        };

        acceleration = new float[4];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_bar, container, false);

        textViewXAxis = view.findViewById(R.id.value_x_axis);
        textViewYAxis = view.findViewById(R.id.value_y_axis);
        textViewZAxis = view.findViewById(R.id.value_z_axis);
        textViewHzFrequency = view.findViewById(R.id.value_hz_frequency);

        return view;
    }

    @Override
    public void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        initViewModel();
        handler.post(runnable);
    }

    private void updateAccelerationText()
    {
        if(acceleration.length == 4) {
            // Update the acceleration data
            textViewXAxis.setText(String.format(Locale.getDefault(), "%.2f", acceleration[0]));
            textViewYAxis.setText(String.format(Locale.getDefault(), "%.2f", acceleration[1]));
            textViewZAxis.setText(String.format(Locale.getDefault(), "%.2f", acceleration[2]));
            textViewHzFrequency.setText(String.format(Locale.getDefault(), "%.0f", acceleration[3]));
        }
    }

    private void initViewModel() {
        SensorViewModel model = ViewModelProviders.of(getActivity()).get(SensorViewModel.class);

        model.getLinearAccelerationSensorLiveData().removeObservers(this);
        model.getLowPassLinearAccelerationSensorLiveData().removeObservers(this);
        model.getComplimentaryLinearAccelerationSensorLiveData().removeObservers(this);
        model.getKalmanLinearAccelerationSensorLiveData().removeObservers(this);
        model.getAccelerationSensorLiveData().removeObservers(this);

        if(PrefUtils.getPrefAndroidLinearAccelerationEnabled(getContext())) {
            model.getLinearAccelerationSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    acceleration = floats;
                }
            });
        } else if(PrefUtils.getPrefFSensorLpfLinearAccelerationEnabled(getContext())){
            model.getLowPassLinearAccelerationSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    acceleration = floats;
                }
            });
        } else if(PrefUtils.getPrefFSensorComplimentaryLinearAccelerationEnabled(getContext())) {
            model.getComplimentaryLinearAccelerationSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    acceleration = floats;
                }
            });
        } else if(PrefUtils.getPrefFSensorKalmanLinearAccelerationEnabled(getContext())) {
            model.getKalmanLinearAccelerationSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    acceleration = floats;
                }
            });
        } else {
            model.getAccelerationSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    acceleration = floats;
                }
            });
        }
    }
}
