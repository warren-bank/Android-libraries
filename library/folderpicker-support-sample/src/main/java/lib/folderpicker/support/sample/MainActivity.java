package lib.folderpicker.support.sample;

import lib.folderpicker.support.FolderPicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final int FOLDER_PICKER_CODE       = 1;
    private static final int EMPTY_FOLDER_PICKER_CODE = 2;
    private static final int FILE_PICKER_CODE         = 3;
    private static final int NEW_FILE_PICKER_CODE     = 4;

    TextView tvFolder, tvEmptyFolder, tvFile, tvNewFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initUI();
    }

    void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

    }

    void initUI() {

        findViewById(R.id.btn_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFolder();
            }
        });

        findViewById(R.id.btn_empty_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickEmptyFolder();
            }
        });

        findViewById(R.id.btn_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFile();
            }
        });

        findViewById(R.id.btn_newfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickNewFile();
            }
        });

        tvFolder      = (TextView) findViewById(R.id.tv_folder);
        tvEmptyFolder = (TextView) findViewById(R.id.tv_empty_folder);
        tvFile        = (TextView) findViewById(R.id.tv_file);
        tvNewFile     = (TextView) findViewById(R.id.tv_newfile);

    }

    void pickFolder() {
        FolderPicker
            .withBuilder()
            .withActivity(MainActivity.this)
            .withRequestCode(FOLDER_PICKER_CODE)
            .start();
    }

    void pickEmptyFolder() {
        FolderPicker
            .withBuilder()
            .withActivity(MainActivity.this)
            .withRequestCode(EMPTY_FOLDER_PICKER_CODE)
            .withTitle("Select folder")
            .withDescription("Cannot contain any files or subdirectories")
            .withEmptyFolder(true)
            .withTheme(R.style.AppTheme_FolderPicker1)
            .start();
    }

    void pickFile() {
        FolderPicker
            .withBuilder()
            .withActivity(MainActivity.this)
            .withRequestCode(FILE_PICKER_CODE)
            .withFilePicker(true)
            .withFileFilter("^.*\\.(?:png|apng|jng|mng)$")
            .withTitle("Select file to upload")
            .withDescription("Possibly a pretty PNG picture?")
            .withHomeButton(true)
            .withPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
            .withTheme(R.style.AppTheme_FolderPicker2)
            .start();
    }

    void pickNewFile() {
        FolderPicker
            .withBuilder()
            .withActivity(MainActivity.this)
            .withRequestCode(NEW_FILE_PICKER_CODE)
            .withNewFilePrompt( getString(R.string.prompt_newfile) )
            .withNewFileName("export.json")
            .withFileFilter("^.*\\.(?:json|txt)$")
            .withTitle("JSON Data Export")
            .withPath("/storage")
            .withTheme(R.style.AppTheme_FolderPicker3)
            .start();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == FOLDER_PICKER_CODE) {

            if (resultCode == Activity.RESULT_OK && intent.hasExtra(FolderPicker.EXTRA_DATA)) {
                String folderLocation = "<b>Selected Folder: </b>"+ intent.getExtras().getString(FolderPicker.EXTRA_DATA);
                tvFolder.setText( Html.fromHtml(folderLocation) );
            } else if (resultCode == Activity.RESULT_CANCELED) {
                tvFolder.setText(R.string.folder_pick_cancelled);
            }

        } else if (requestCode == EMPTY_FOLDER_PICKER_CODE) {

            if (resultCode == Activity.RESULT_OK && intent.hasExtra(FolderPicker.EXTRA_DATA)) {
                String folderLocation = "<b>Selected Folder: </b>"+ intent.getExtras().getString(FolderPicker.EXTRA_DATA);
                tvEmptyFolder.setText( Html.fromHtml(folderLocation) );
            } else if (resultCode == Activity.RESULT_CANCELED) {
                tvEmptyFolder.setText(R.string.folder_pick_cancelled);
            }

        } else if (requestCode == FILE_PICKER_CODE) {

            if (resultCode == Activity.RESULT_OK && intent.hasExtra(FolderPicker.EXTRA_DATA)) {
                String fileLocation = "<b>Selected File: </b>"+ intent.getExtras().getString(FolderPicker.EXTRA_DATA);
                tvFile.setText( Html.fromHtml(fileLocation) );
            } else if (resultCode == Activity.RESULT_CANCELED) {
                tvFile.setText(R.string.file_pick_cancelled);
            }

        } else if (requestCode == NEW_FILE_PICKER_CODE) {

            if (resultCode == Activity.RESULT_OK && intent.hasExtra(FolderPicker.EXTRA_DATA)) {
                String fileLocation = "<b>Selected File: </b>"+ intent.getExtras().getString(FolderPicker.EXTRA_DATA);
                tvNewFile.setText( Html.fromHtml(fileLocation) );
            } else if (resultCode == Activity.RESULT_CANCELED) {
                tvNewFile.setText(R.string.newfile_pick_cancelled);
            }

        }

    }

}
