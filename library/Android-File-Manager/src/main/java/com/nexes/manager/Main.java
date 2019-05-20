/*
    Open Manager, an open source file manager for the Android system
    Copyright (C) 2009, 2010, 2011  Joe Berria <nexesdevelopment@gmail.com>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.nexes.manager;

import java.io.File;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StatFs;
import android.os.Environment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * This is the main activity. The activity that is presented to the user
 * as the application launches. This class is, and expected not to be, instantiated.
 * <br>
 * <p>
 * This class handles creating the buttons and
 * text views. This class relies on the class EventHandler to handle all button
 * press logic and to control the data displayed on its ListView. This class
 * also relies on the FileManager class to handle all file operations such as
 * copy/paste zip/unzip etc. However most interaction with the FileManager class
 * is done via the EventHandler class. Also the SettingsMangager class to load
 * and save user settings.
 * <br>
 * <p>
 * The design objective with this class is to control only the look of the
 * GUI (option menu, context menu, ListView, buttons and so on) and rely on other
 * supporting classes to do the heavy lifting.
 *
 * @author Joe Berria
 *
 */
public final class Main extends ListActivity {
    public static final String ACTION_WIDGET    = "com.nexes.manager.Main.ACTION_WIDGET";

    private static final String PREFS_NAME      = "ManagerPrefsFile";    //user preference file name
    private static final String PREFS_HIDDEN    = "hidden";
    private static final String PREFS_COLOR     = "color";
    private static final String PREFS_SORT      = "sort";
    private static final String PREFS_STORAGE   = "sdcard space";

    private static final int MENU_SETTING = 0x01;    //option menu id
    private static final int MENU_QUIT    = 0x02;    //option menu id
    private static final int SETTING_REQ  = 0x03;    //request code for intent

    private FileManager mFileMag;
    private EventHandler mHandler;
    private EventHandler.TableRow mTable;

    private SharedPreferences mSettings;
    private boolean mReturnIntent = false;
    private boolean mUseBackKey = true;
    private TextView mPathLabel, mStorageLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri;
        File file;
        String path, location;
        Intent intent = getIntent();

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        /*read settings*/
        mSettings     = getSharedPreferences(PREFS_NAME, 0);
        boolean hide  = mSettings.getBoolean(PREFS_HIDDEN, false);      // default = do NOT show hidden files
        int space     = mSettings.getInt(PREFS_STORAGE, View.VISIBLE);  // default = show free space stats for SD card (internal only)
        int color     = mSettings.getInt(PREFS_COLOR, -1);              // default = Color.WHITE
        int sort      = mSettings.getInt(PREFS_SORT, 1);                // default = "Alphabetical"

        mFileMag = new FileManager();
        mFileMag.setShowHiddenFiles(hide);
        mFileMag.setSortType(sort);

        location = null;
        if (savedInstanceState != null) {
            location = savedInstanceState.getString("location");
        }
        else if ((intent != null) && (intent.getAction().equals(ACTION_WIDGET))) {
            uri = intent.getData();
            if (uri != null) {
                path = uri.getPath();
                if ((path != null) && (path.length() > 0) && (path.charAt(0) == '/') ) {
                    file = new File(path);
                    if (file.isDirectory() && file.canRead()) {
                        location = file.getAbsolutePath();
                    }
                }
            }
            if (location != null) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    path = extras.getString("folder");
                    if ((path != null) && (path.length() > 0) && (path.charAt(0) == '/') ) {
                        file = new File(path);
                        if (file.isDirectory() && file.canRead()) {
                            location = file.getAbsolutePath();
                        }
                    }
                }
            }
        }
        if (location == null) {
            mHandler = new EventHandler(Main.this, mFileMag);
        }
        else {
            mHandler = new EventHandler(Main.this, mFileMag, location);
        }

        mHandler.setTextColor(color);
        mTable = mHandler.new TableRow();

        /*sets the ListAdapter for our ListActivity and
         *gives our EventHandler class the same adapter
         */
        mHandler.setListAdapter(mTable);
        setListAdapter(mTable);

        mStorageLabel = (TextView)findViewById(R.id.storage_label);
        mPathLabel    = (TextView)findViewById(R.id.path_label);
        mPathLabel.setText("path: " + mFileMag.getHomeDir());

        updateStorageLabel();
        mStorageLabel.setVisibility(space);

        mHandler.setUpdateLabels(mPathLabel);

        /* setup buttons */
        int[] img_button_id = {R.id.back_button, R.id.home_button};

        ImageButton[] bimg = new ImageButton[img_button_id.length];

        for (int i = 0; i < img_button_id.length; i++) {
            bimg[i] = (ImageButton)findViewById(img_button_id[i]);
            bimg[i].setOnClickListener(mHandler);
        }

        if ((intent != null) && (intent.getAction().equals(Intent.ACTION_GET_CONTENT))) {
            mReturnIntent = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("location", mFileMag.getCurrentDir());
    }

    /*(non Java-Doc)
     * Returns the file that was selected to the intent that
     * called this activity. usually from the caller is another application.
     */
    private void returnIntentResults(File data) {
        mReturnIntent = false;

        Intent ret = new Intent();
        ret.setData(Uri.fromFile(data));
        setResult(RESULT_OK, ret);

        finish();
    }

    private void updateStorageLabel() {
        long total, aval;
        int kb = 1024;

        StatFs fs = new StatFs(Environment.
                                getExternalStorageDirectory().getPath());

        total = fs.getBlockCount() * (fs.getBlockSize() / kb);
        aval = fs.getAvailableBlocks() * (fs.getBlockSize() / kb);

        mStorageLabel.setText(String.format("sdcard: Total %.2f GB " +
                              "\t\tAvailable %.2f GB",
                              (double)total / (kb * kb), (double)aval / (kb * kb)));
    }

    private static String getMimeType(String filename) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(filename);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     *  To add more functionality and let the user interact with more
     *  file types, this is the function to add the ability.
     *
     *  (note): this method can be done more efficiently
     */
    @Override
    public void onListItemClick(ListView parent, View view, int position, long id) {
        final String item = mHandler.getData(position);
        File file = new File(mFileMag.getCurrentDir() + "/" + item);

        if(!file.exists())
            return;

        if (file.isDirectory()) {
            if(file.canRead()) {
                mHandler.updateDirectory(mFileMag.getNextDir(item, false));
                mPathLabel.setText(mFileMag.getCurrentDir());

                /*set back button switch to true
                 * (this will be better implemented later)
                 */
                if(!mUseBackKey)
                    mUseBackKey = true;

            } else {
                Toast.makeText(this, "Can't read folder due to permissions", Toast.LENGTH_SHORT).show();
            }
        }
        else if (mReturnIntent) {
            returnIntentResults(file);
        }
        else {
            Intent intent;
            String mime_type;

            intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);

            mime_type = getMimeType(item);
            if (mime_type == null) {
                intent.setData(Uri.fromFile(file));
            }
            else {
                intent.setDataAndType(Uri.fromFile(file), mime_type);
            }

            try {
                startActivity(intent);
            } catch(ActivityNotFoundException e) {
                Toast.makeText(this, "Sorry, couldn't find any app to open " + file.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences.Editor editor = mSettings.edit();
        boolean hide;
        int space, color, sort;

        /* resultCode must equal RESULT_CANCELED because the only way
         * out of that activity is pressing the back button on the phone
         * this publishes a canceled result code not an ok result code
         */
        if(requestCode == SETTING_REQ && resultCode == RESULT_CANCELED) {
            //save the information we get from settings activity
            hide = data.getBooleanExtra("HIDDEN", false);     // default = do NOT show hidden files
            space = data.getIntExtra("SPACE", View.VISIBLE);  // default = show free space stats for SD card (internal only)
            color = data.getIntExtra("COLOR", -1);            // default = Color.WHITE
            sort = data.getIntExtra("SORT", 1);               // default = "Alphabetical"

            editor.putBoolean(PREFS_HIDDEN, hide);
            editor.putInt(PREFS_STORAGE, space);
            editor.putInt(PREFS_COLOR, color);
            editor.putInt(PREFS_SORT, sort);
            editor.commit();

            mFileMag.setShowHiddenFiles(hide);
            mStorageLabel.setVisibility(space);
            mHandler.setTextColor(color);
            mFileMag.setSortType(sort);
            mHandler.updateDirectory(mFileMag.getNextDir(mFileMag.getCurrentDir(), true));
        }
    }

    /* ================Menus, options menu and context menu start here=================*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_SETTING, 0, "Settings").setIcon(R.drawable.setting);
        menu.add(0, MENU_QUIT, 0, "Quit").setIcon(R.drawable.logout);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case MENU_SETTING:
                Intent settings_int = new Intent(this, Settings.class);
                settings_int.putExtra("HIDDEN", mSettings.getBoolean(PREFS_HIDDEN, false));     // default = do NOT show hidden files
                settings_int.putExtra("SPACE", mSettings.getInt(PREFS_STORAGE, View.VISIBLE));  // default = show free space stats for SD card (internal only)
                settings_int.putExtra("COLOR", mSettings.getInt(PREFS_COLOR, -1));              // default = Color.WHITE
                settings_int.putExtra("SORT", mSettings.getInt(PREFS_SORT, 1));                 // default = "Alphabetical"

                startActivityForResult(settings_int, SETTING_REQ);
                return true;

            case MENU_QUIT:
                finish();
                return true;
        }
        return false;
    }

    /* ================Menus, options menu and context menu end here=================*/

    /*
     * (non-Javadoc)
     * This will check if the user is at root directory. If so, if they press back
     * again, it will close the application.
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
   public boolean onKeyDown(int keycode, KeyEvent event) {
        String current = mFileMag.getCurrentDir();

        if(keycode == KeyEvent.KEYCODE_BACK && mUseBackKey && !current.equals("/")) {
            mHandler.updateDirectory(mFileMag.getPreviousDir());
            mPathLabel.setText(mFileMag.getCurrentDir());
            return true;

        } else if(keycode == KeyEvent.KEYCODE_BACK && mUseBackKey && current.equals("/")) {
            Toast.makeText(Main.this, "Press back again to quit.", Toast.LENGTH_SHORT).show();

            mUseBackKey = false;
            mPathLabel.setText(mFileMag.getCurrentDir());

            return false;

        } else if(keycode == KeyEvent.KEYCODE_BACK && !mUseBackKey && current.equals("/")) {
            finish();

            return false;
        }
        return false;
    }
}
