package com.kircherelectronics.accelerationexplorer.datalogger;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

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
 * Created by KircherEngineerH on 4/27/2016.
 */
public class DataLoggerManager implements Runnable {
    private static final String tag = DataLoggerManager.class.getSimpleName();

    public final static String DEFAULT_APPLICATION_DIRECTORY = "AccelerationExplorer";

    private final static long THREAD_SLEEP_TIME = 20;
    public final static String FILE_NAME_SEPARATOR = "-";

    // boolean to indicate if the data should be written to a file.
    private volatile boolean logData = false;

    // Log output time stamp
    protected long logTime = 0;

    private ArrayList<String> csvHeaders;
    private ArrayList<String> csvValues;

    private DataLoggerInterface dataLogger;

    private Context context;

    private volatile ArrayList<String> acceleration;

    private Thread thread;

    public DataLoggerManager(Context context) {
        this.context = context;
        csvHeaders = getCsvHeaders();
        csvValues = new ArrayList<>();
        acceleration = new ArrayList<>();
    }

    @Override
    public void run() {
        while (logData && !Thread.currentThread().isInterrupted()) {
            // Check if the row is filled and ready to be written to the
            // log.
            logData();

            try {
                Thread.sleep(THREAD_SLEEP_TIME);
            } catch (InterruptedException e) {
                // very important to ensure the thread is killed
                Thread.currentThread().interrupt();
            }
        }

        // very important to ensure the thread is killed
        Thread.currentThread().interrupt();
    }


    public void startDataLog() throws IllegalStateException {
        if (!logData) {
            logData = true;
            logTime = System.currentTimeMillis();
            dataLogger = new CsvDataLogger(context, getFile(this.getFilePath(), this.getFileName()));
            dataLogger.setHeaders(csvHeaders);
            thread = new Thread(this);
            thread.start();
        } else {
            throw new IllegalStateException("Logger is already started!");
        }
    }

    public void stopDataLog() throws IllegalStateException {
        if (logData) {
            logData = false;
            thread.interrupt();
            thread = null;
            dataLogger.writeToFile();
        }
    }

    public void setAcceleration(float[] acceleration) {
        synchronized (acceleration) {
            this.acceleration.clear();
            for (int i = 0; i < 3; i++) {
                this.acceleration.add(String.valueOf(acceleration[i]));
            }
        }
    }

    private void logData() {
        csvValues.clear();
        csvValues.add(String.valueOf((System.currentTimeMillis() - logTime) / 1000.0f));

        synchronized (acceleration) {
            csvValues.addAll(acceleration);
        }

        dataLogger.addRow(csvValues);
    }

    private File getFile(String filePath, String fileName) {
        File dir = new File(filePath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return new File(dir, fileName);
    }

    private String getFilePath() {
        return new StringBuilder().append(Environment.getExternalStorageDirectory()).append(File.separator).append
                (DEFAULT_APPLICATION_DIRECTORY).append(File.separator).toString();
    }

    private String getFileName() {
        Calendar c = Calendar.getInstance();

        return new StringBuilder().append(DEFAULT_APPLICATION_DIRECTORY).append(FILE_NAME_SEPARATOR)
                .append(c.get(Calendar.YEAR)).append(FILE_NAME_SEPARATOR).append(c.get(Calendar.MONTH) + 1).append
                        (FILE_NAME_SEPARATOR).
                        append(c.get(Calendar.DAY_OF_MONTH)).append(FILE_NAME_SEPARATOR).append(c.get(Calendar.HOUR))
                .append("-").append(c.get(Calendar.MINUTE)).append(FILE_NAME_SEPARATOR).append(c.get(Calendar.SECOND)
                ).append(".csv").toString();
    }

    private ArrayList<String> getCsvHeaders() {
        ArrayList<String> headers = new ArrayList<>();

        headers.add("Timestamp");
        headers.add("X");
        headers.add("Y");
        headers.add("Z");

        return headers;
    }
}
