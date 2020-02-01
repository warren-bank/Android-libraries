package com.kircherelectronics.accelerationexplorer.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.kircherelectronics.accelerationexplorer.R;
import com.kircherelectronics.accelerationexplorer.plot.DynamicChart;
import com.kircherelectronics.accelerationexplorer.prefs.PrefUtils;
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
 * Created by kaleb on 7/9/17.
 */

public class LineChartFragment extends Fragment {

    // Graph plot for the UI outputs
    private DynamicChart dynamicChart;

    private Handler handler;
    private Runnable runnable;

    private float[] acceleration;

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SensorViewModel model = ViewModelProviders.of(getActivity()).get(SensorViewModel.class);

        model.getAccelerationSensorLiveData().observe(this, new Observer<float[]>() {
            @Override
            public void onChanged(@Nullable float[] floats) {
                acceleration = floats;
            }
        });

        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                handler.postDelayed(this, 20);
                dynamicChart.setAcceleration(acceleration);
            }
        };

        acceleration = new float[4];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line_chart, container, false);

        // Create the graph plot
        LineChart plot = view.findViewById(R.id.line_chart);
        dynamicChart = new DynamicChart(getContext(), plot);

        return view;
    }

    @Override
    public void onPause() {
        handler.removeCallbacks(runnable);
        dynamicChart.onStopPlot();
        dynamicChart.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        initViewModel();
        handler.post(runnable);
        dynamicChart.onResume();
        dynamicChart.onStartPlot();
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
