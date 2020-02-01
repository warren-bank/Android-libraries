package com.kircherelectronics.accelerationexplorer.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kircherelectronics.accelerationexplorer.R;
import com.kircherelectronics.accelerationexplorer.gauge.GaugeRotation;
import com.kircherelectronics.accelerationexplorer.prefs.PrefUtils;
import com.kircherelectronics.accelerationexplorer.viewmodel.SensorViewModel;

import java.util.Arrays;

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
 * Created by kaleb on 7/8/17.
 */

public class RotationGaugeFragment extends Fragment {

    private GaugeRotation gaugeRotation;
    private Handler handler;
    private Runnable runnable;

    private float[] rotation;

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                updateRotationGauge();
                handler.postDelayed(this, 20);
            }
        };

        rotation = new float[4];
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rotation_gauge, container, false);

        gaugeRotation = view.findViewById(R.id.gauge_rotation);

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

    private void updateRotationGauge() {
        gaugeRotation.updateRotation(rotation);
    }

    private void initViewModel() {
        SensorViewModel model = ViewModelProviders.of(getActivity()).get(SensorViewModel.class);

        model.getGyroscopeSensorLiveData().removeObservers(this);
        model.getComplimentaryGyroscopeSensorLiveData().removeObservers(this);
        model.getKalmanGyroscopeSensorLiveData().removeObservers(this);

        if(PrefUtils.getPrefAndroidLinearAccelerationEnabled(getContext())) {
            model.getGyroscopeSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    rotation = floats;
                }
            });
        } else if(PrefUtils.getPrefFSensorLpfLinearAccelerationEnabled(getContext())){
            model.getGyroscopeSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    rotation = floats;
                }
            });
        } else if(PrefUtils.getPrefFSensorComplimentaryLinearAccelerationEnabled(getContext())) {
            model.getComplimentaryGyroscopeSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    rotation = floats;
                }
            });
        } else if(PrefUtils.getPrefFSensorKalmanLinearAccelerationEnabled(getContext())) {
            model.getKalmanGyroscopeSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    rotation = floats;
                }
            });
        } else {
            model.getGyroscopeSensorLiveData().observe(this, new Observer<float[]>() {
                @Override
                public void onChanged(@Nullable float[] floats) {
                    rotation = floats;
                }
            });
        }
    }
}
