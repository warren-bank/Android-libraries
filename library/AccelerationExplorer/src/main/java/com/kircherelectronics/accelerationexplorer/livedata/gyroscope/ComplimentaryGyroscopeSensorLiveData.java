package com.kircherelectronics.accelerationexplorer.livedata.gyroscope;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.util.Log;

import com.kircherelectronics.fsensor.sensor.gyroscope.ComplimentaryGyroscopeSensor;

import java.util.Arrays;

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

public class ComplimentaryGyroscopeSensorLiveData extends LiveData<float[]> {
    private ComplimentaryGyroscopeSensor sensor;
    private CompositeDisposable compositeDisposable;

    public ComplimentaryGyroscopeSensorLiveData(Context context) {
        this.sensor = new ComplimentaryGyroscopeSensor (context);
    }

    @Override
    protected void onActive() {
        this.compositeDisposable = new CompositeDisposable();
        this.sensor.getPublishSubject().subscribe(new Observer<float[]>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(float[] values) {
                setValue(values);
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        });
        sensor.onStart();
    }

    @Override
    protected void onInactive() {
        this.compositeDisposable.dispose();
        this.sensor.onStop();
    }
}
