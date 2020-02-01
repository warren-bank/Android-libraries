package com.kircherelectronics.accelerationexplorer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.kircherelectronics.accelerationexplorer.livedata.acceleration.AccelerationSensorLiveData;
import com.kircherelectronics.accelerationexplorer.livedata.acceleration.ComplimentaryLinearAccelerationSensorLiveData;
import com.kircherelectronics.accelerationexplorer.livedata.acceleration.KalmanLinearAccelerationSensorLiveData;
import com.kircherelectronics.accelerationexplorer.livedata.acceleration.LinearAccelerationSensorLiveData;
import com.kircherelectronics.accelerationexplorer.livedata.acceleration.LowPassLinearAccelerationSensorLiveData;
import com.kircherelectronics.accelerationexplorer.livedata.gyroscope.ComplimentaryGyroscopeSensorLiveData;
import com.kircherelectronics.accelerationexplorer.livedata.gyroscope.GyroscopeSensorLiveData;
import com.kircherelectronics.accelerationexplorer.livedata.gyroscope.KalmanGyroscopeSensorLiveData;

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
public class SensorViewModel extends AndroidViewModel {
    private AccelerationSensorLiveData accelerationSensorLiveData;
    private LinearAccelerationSensorLiveData linearAccelerationSensorLiveData;
    private LowPassLinearAccelerationSensorLiveData lowPassLinearAccelerationSensorLiveData;
    private ComplimentaryLinearAccelerationSensorLiveData complimentaryLinearAccelerationSensorLiveData;
    private KalmanLinearAccelerationSensorLiveData kalmanLinearAccelerationSensorLiveData;

    private GyroscopeSensorLiveData gyroscopeSensorLiveData;
    private ComplimentaryGyroscopeSensorLiveData complimentaryGyroscopeSensorLiveData;
    private KalmanGyroscopeSensorLiveData kalmanGyroscopeSensorLiveData;


    public SensorViewModel(Application application) {
        super(application);

        this.accelerationSensorLiveData = new AccelerationSensorLiveData(application);
        this.linearAccelerationSensorLiveData = new LinearAccelerationSensorLiveData(application);
        this.lowPassLinearAccelerationSensorLiveData = new LowPassLinearAccelerationSensorLiveData(application);
        this.complimentaryLinearAccelerationSensorLiveData = new ComplimentaryLinearAccelerationSensorLiveData(application);
        this.kalmanLinearAccelerationSensorLiveData = new KalmanLinearAccelerationSensorLiveData(application);

        this.gyroscopeSensorLiveData = new GyroscopeSensorLiveData(application);
        this.complimentaryGyroscopeSensorLiveData = new ComplimentaryGyroscopeSensorLiveData(application);
        this.kalmanGyroscopeSensorLiveData = new KalmanGyroscopeSensorLiveData(application);
    }

    public AccelerationSensorLiveData getAccelerationSensorLiveData() {
        return accelerationSensorLiveData;
    }

    public LinearAccelerationSensorLiveData getLinearAccelerationSensorLiveData() {
        return linearAccelerationSensorLiveData;
    }

    public LowPassLinearAccelerationSensorLiveData getLowPassLinearAccelerationSensorLiveData() {
        return lowPassLinearAccelerationSensorLiveData;
    }

    public ComplimentaryLinearAccelerationSensorLiveData getComplimentaryLinearAccelerationSensorLiveData() {
        return complimentaryLinearAccelerationSensorLiveData;
    }

    public KalmanLinearAccelerationSensorLiveData getKalmanLinearAccelerationSensorLiveData() {
        return kalmanLinearAccelerationSensorLiveData;
    }

    public GyroscopeSensorLiveData getGyroscopeSensorLiveData() {
        return gyroscopeSensorLiveData;
    }

    public ComplimentaryGyroscopeSensorLiveData getComplimentaryGyroscopeSensorLiveData() {
        return complimentaryGyroscopeSensorLiveData;
    }

    public KalmanGyroscopeSensorLiveData getKalmanGyroscopeSensorLiveData() {
        return kalmanGyroscopeSensorLiveData;
    }
}
