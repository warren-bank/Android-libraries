package com.kircherelectronics.accelerationexplorer.livedata.acceleration;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.kircherelectronics.accelerationexplorer.prefs.PrefUtils;
import com.kircherelectronics.fsensor.filter.averaging.AveragingFilter;
import com.kircherelectronics.fsensor.filter.averaging.LowPassFilter;
import com.kircherelectronics.fsensor.filter.averaging.MeanFilter;
import com.kircherelectronics.fsensor.filter.averaging.MedianFilter;
import com.kircherelectronics.fsensor.sensor.acceleration.KalmanLinearAccelerationSensor;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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

public class KalmanLinearAccelerationSensorLiveData extends LiveData<float[]> {
    private KalmanLinearAccelerationSensor sensor;
    private CompositeDisposable compositeDisposable;
    private Context context;
    private AveragingFilter averagingFilter;

    public KalmanLinearAccelerationSensorLiveData(Context context) {
        this.context = context;
        this.sensor = new KalmanLinearAccelerationSensor(context);
    }

    @Override
    protected void onActive() {
        this.sensor.setSensorFrequency(PrefUtils.getSensorFrequencyPrefs(context));

        if(PrefUtils.getPrefLpfSmoothingEnabled(context)) {
            averagingFilter = new LowPassFilter();
            ((LowPassFilter) averagingFilter).setTimeConstant(PrefUtils.getPrefLpfSmoothingTimeConstant(context));
        } else if(PrefUtils.getPrefMeanFilterSmoothingEnabled(context)) {
            averagingFilter = new MeanFilter();
            ((MeanFilter) averagingFilter).setTimeConstant(PrefUtils.getPrefMeanFilterSmoothingTimeConstant(context));
        } else if(PrefUtils.getPrefMedianFilterSmoothingEnabled(context)) {
            averagingFilter = new MedianFilter();
            ((MedianFilter) averagingFilter).setTimeConstant(PrefUtils.getPrefMedianFilterSmoothingTimeConstant(context));
        } else {
            averagingFilter = null;
        }

        this.compositeDisposable = new CompositeDisposable();
        this.sensor.getPublishSubject().subscribe(new Observer<float[]>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(float[] values) {
                if(averagingFilter != null) {
                    setValue(averagingFilter.filter(values));
                } else {
                    setValue(values);
                }
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
        this.sensor.onStart();
    }

    @Override
    protected void onInactive() {
        this.compositeDisposable.dispose();
        this.sensor.onStop();
    }
}
