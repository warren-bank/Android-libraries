package com.aditya.filebrowser.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.aditya.filebrowser.FileBrowser;
import com.aditya.filebrowser.FileChooser;
import com.aditya.filebrowser.Constants;

import java.util.ArrayList;
import android.net.Uri;

public class FileBrowserSample extends Activity {
    private static final int REQUEST_FILE_S = 1;
    private static final int REQUEST_FILE_M = 2;
    private static final String TAG = "FileBrowserSample";
    private TextView mDirectoryTextView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mDirectoryTextView = (TextView) findViewById(R.id.textSelected);

        // Set up click handler for "FileBrowser" button
        findViewById(R.id.btn_open_filebrowser)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final Intent activityIntent = new Intent(
                            FileBrowserSample.this,
                            FileBrowser.class
                        );

                        startActivity(activityIntent);
                    }
                });

        // Set up click handler for "FileChooser (single)" button
        findViewById(R.id.btn_open_filechooser_s)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final Intent activityIntent = new Intent(
                            FileBrowserSample.this,
                            FileChooser.class
                        );

                        activityIntent.putExtra(
                            Constants.SELECTION_MODE,
                            Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal()
                        );

                        startActivityForResult(activityIntent, REQUEST_FILE_S);
                    }
                });

        // Set up click handler for "FileChooser (multi)" button
        findViewById(R.id.btn_open_filechooser_m)
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        final Intent activityIntent = new Intent(
                            FileBrowserSample.this,
                            FileChooser.class
                        );

                        activityIntent.putExtra(
                            Constants.SELECTION_MODE,
                            Constants.SELECTION_MODES.MULTIPLE_SELECTION.ordinal()
                        );

                        startActivityForResult(activityIntent, REQUEST_FILE_M);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_FILE_S) || (requestCode == REQUEST_FILE_M)) {
            Log.i(TAG, String.format("Return from FileChooser with result %d", resultCode));

            if (resultCode == RESULT_OK) {
                String strFiles;
                if (requestCode == REQUEST_FILE_S) {
                    Uri selectedFile = data.getData();
                    strFiles = getFiles(selectedFile);
                }
                else {
                  //requestCode == REQUEST_FILE_M
                    ArrayList<Uri> selectedFiles = data.getParcelableArrayListExtra(Constants.SELECTED_ITEMS);
                    strFiles = getFiles(selectedFiles);
                }

                mDirectoryTextView.setText(strFiles);
            } else {
                mDirectoryTextView.setText("nothing selected");
            }
        }
    }

    private String getFiles(ArrayList<Uri> selectedFiles) {
        String strFiles = "";
        for (int i = 0; i < selectedFiles.size(); i++) {
            if (i > 0) {
                strFiles += "\n";
            }
            strFiles += selectedFiles.get(i).getPath();
        }
        return strFiles;
    }

    private String getFiles(Uri selectedFile) {
        return selectedFile.getPath();
    }
}
