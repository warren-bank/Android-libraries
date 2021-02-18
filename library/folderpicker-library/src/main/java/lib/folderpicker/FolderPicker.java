package lib.folderpicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

public class FolderPicker extends Activity {

    public static final FolderPickerBuilder withBuilder() {
        return new FolderPickerBuilder();
    }

    public static final String EXTRA_DATA = "data";

    protected static final String EXTRA_THEME             = "theme";
    protected static final String EXTRA_TITLE             = "title";
    protected static final String EXTRA_DESCRIPTION       = "desc";
    protected static final String EXTRA_LOCATION          = "location";
    protected static final String EXTRA_HOME_BUTTON       = "homeButton";
    protected static final String EXTRA_EMPTY_FOLDER      = "emptyFolder";
    protected static final String EXTRA_NEW_FILE_PROMPT   = "newFilePrompt";
    protected static final String EXTRA_PICK_FILE         = "pickFile";
    protected static final String EXTRA_PICK_FILE_PATTERN = "pickFilePattern";

    Comparator<FilePojo> comparatorAscending = new Comparator<FilePojo>() {
        @Override
        public int compare(FilePojo f1, FilePojo f2) {
            return f1.getName().compareTo(f2.getName());
        }
    };

    Pattern mFilePattern;
    FileFilter mFileFilter;

    //Folders and Files have separate lists because we show all folders first then files
    ArrayList<FilePojo> mFolderAndFileList;
    ArrayList<FilePojo> mFoldersList;
    ArrayList<FilePojo> mFilesList;

    TextView mTvLocation;
    String mHomeLocation;

    String mLocation;
    boolean mPickFile;
    Intent mReceivedIntent;
    boolean mEmptyFolder;
    String mNewFilePrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isExternalStorageReadable()) {
            Toast.makeText(FolderPicker.this, getString(R.string.no_access_to_storage), Toast.LENGTH_LONG).show();
            finish();
        }

        mReceivedIntent = getIntent();

        try {
            if (mReceivedIntent.hasExtra(EXTRA_THEME)) {
                int theme = mReceivedIntent.getIntExtra(EXTRA_THEME, -999);
                if (theme != -999) {
                    setTheme(theme);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.fp_main_layout);

        mTvLocation   = findViewById(R.id.fp_tv_location);
        mHomeLocation = Environment.getExternalStorageDirectory().getAbsolutePath();

        try {
            if (mReceivedIntent.hasExtra(EXTRA_TITLE)) {
                String title = mReceivedIntent.getStringExtra(EXTRA_TITLE);
                if (title != null) {
                    ((TextView)findViewById(R.id.fp_tv_title)).setText(title);
                }
            }

            if (mReceivedIntent.hasExtra(EXTRA_DESCRIPTION)) {
                String desc = mReceivedIntent.getStringExtra(EXTRA_DESCRIPTION);
                if (desc != null) {
                    TextView textView = findViewById(R.id.fp_tv_desc);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(desc);
                }
            }

            if (mReceivedIntent.hasExtra(EXTRA_LOCATION)) {
                String newLocation = mReceivedIntent.getStringExtra(EXTRA_LOCATION);
                if (newLocation != null) {
                    File folder = new File(newLocation);
                    if (folder.exists())
                        mHomeLocation = newLocation;
                }
            }

            if (mReceivedIntent.hasExtra(EXTRA_HOME_BUTTON)) {
                boolean homeButton = mReceivedIntent.getBooleanExtra(EXTRA_HOME_BUTTON, false);
                if (homeButton) {
                    findViewById(R.id.fp_btn_home).setVisibility(View.VISIBLE);
                }
            }

            if (mReceivedIntent.hasExtra(EXTRA_EMPTY_FOLDER)) {
                mEmptyFolder = mReceivedIntent.getBooleanExtra(EXTRA_EMPTY_FOLDER, false);
                if (mEmptyFolder) {
                    findViewById(R.id.fp_tv_empty_dir).setVisibility(View.VISIBLE);
                }
            }

            if (mReceivedIntent.hasExtra(EXTRA_NEW_FILE_PROMPT)) {
                mNewFilePrompt = mReceivedIntent.getStringExtra(EXTRA_NEW_FILE_PROMPT);
            }

            if (mReceivedIntent.hasExtra(EXTRA_PICK_FILE)) {
                mPickFile = mReceivedIntent.getBooleanExtra(EXTRA_PICK_FILE, false);
                if (mPickFile) {
                    findViewById(R.id.fp_btn_select).setVisibility(View.GONE);
                    findViewById(R.id.fp_btn_new).setVisibility(View.GONE);
                }
            }

            if (mReceivedIntent.hasExtra(EXTRA_PICK_FILE_PATTERN)) {
                String pickFilePattern = mReceivedIntent.getStringExtra(EXTRA_PICK_FILE_PATTERN);
                if (pickFilePattern != null) {
                    mFilePattern = Pattern.compile(pickFilePattern, Pattern.CASE_INSENSITIVE);

                    mFileFilter = new FileFilter() {
                        @Override
                        public boolean accept(File file) {
                            if (file.isDirectory()) return true;
                            if (!file.isFile()) return false;

                            String filename = file.getName();
                            return mFilePattern.matcher(filename).matches();
                        }
                    };
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        checkAndLoadLists(mHomeLocation);
    }

    /**
     * Checks if external storage is available to at least read
     */
    boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Cleans up directory paths entered manually
     *   ex: trailing directory separator, "/./", "/../", etc
     */
    private String normalizeLocation(String location) {
        if (location == null) return null;

        location = location.trim();
        if (location.isEmpty()) return null;

        try {
            File folder = new File(location);
            return folder.getCanonicalPath();
        }
        catch(Exception e) {
            return null;
        }
    }

    boolean checkAndLoadLists(String location, boolean showToast) {
        if (checkLocation(location, showToast)) {
            mLocation = location;
            loadLists();
            return true;
        }
        return false;
    }

    boolean checkAndLoadLists(String location) {
        return checkAndLoadLists(location, true);
    }

    /**
     * Check location and load lists if location is correct.
     * @param location
     * @param showToast
     * @return
     */
    private boolean checkLocation(String location, boolean showToast) {
        if ((location == null) || location.isEmpty()) {
            if (showToast) {
                Toast.makeText(FolderPicker.this, R.string.dir_is_not_exist, Toast.LENGTH_LONG).show();
            }
            return false;
        }

        File folder = new File(location);

        if (!folder.exists()) {
            if (showToast) {
                Toast.makeText(FolderPicker.this, R.string.dir_is_not_exist, Toast.LENGTH_LONG).show();
            }
            return false;
        }

        if (!folder.isDirectory()) {
            if (showToast) {
                Toast.makeText(FolderPicker.this, R.string.is_not_dir, Toast.LENGTH_LONG).show();
            }
            return false;
        }

        if (!folder.canRead()) {
            if (showToast) {
                Toast.makeText(FolderPicker.this, R.string.dir_read_permission_denied, Toast.LENGTH_LONG).show();
            }
            return false;
        }

        return true;
    }

    /**
     * Load lists and show.
     */
    void loadLists() {
        try {
            File folder = new File(mLocation);

            mTvLocation.setText(String.format(getString(R.string.location_mask), folder.getAbsolutePath()));
            File[] files = folder.listFiles(mFileFilter);

            mFoldersList = new ArrayList<>();
            mFilesList = new ArrayList<>();

            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    FilePojo filePojo = new FilePojo(currentFile.getName(), true);
                    mFoldersList.add(filePojo);
                } else if (currentFile.isFile()) {
                    FilePojo filePojo = new FilePojo(currentFile.getName(), false);
                    mFilesList.add(filePojo);
                }
            }

            // sort & add to final List - as we show folders first add folders first to the final list
            Collections.sort(mFoldersList, comparatorAscending);
            mFolderAndFileList = new ArrayList<>();
            mFolderAndFileList.addAll(mFoldersList);

            //if we have to show files, then add files also to the final list
            if (mPickFile) {
                Collections.sort(mFilesList, comparatorAscending );
                mFolderAndFileList.addAll(mFilesList);
            }

            showList();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Show list of folders and files.
     */
    void showList() {
        try {
            FolderAdapter FolderAdapter = new FolderAdapter(FolderPicker.this, mFolderAndFileList);
            ListView listView = findViewById(R.id.fp_listView);
            listView.setAdapter(FolderAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    listClick(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Click on the list item.
     * @param position
     */
    void listClick(int position) {
        if (mPickFile && !mFolderAndFileList.get(position).isFolder()) {
            String data = mLocation + (mLocation.equals(File.separator) ? "" : File.separator) + mFolderAndFileList.get(position).getName();
            exit(data);
        } else {
            String location = mLocation + (mLocation.equals(File.separator) ? "" : File.separator) + mFolderAndFileList.get(position).getName();
            checkAndLoadLists(location);
        }
    }

    public void home(View v) {
        checkAndLoadLists(mHomeLocation);
    }

    /**
     * Create new folder.
     * @param filename
     */
    void createNewFolder(String filename) {
        try {
            File file = new File(mLocation + (mLocation.equals(File.separator) ? "" : File.separator) + filename);
            file.mkdirs();
            checkAndLoadLists(mLocation);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(FolderPicker.this, String.format(getString(R.string.error_string_mask), e.toString()), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Show dialog to enter name for new folder or file
     */
    public void newFolderOrFileDialog(boolean isFolder) {
        File folder = new File(mLocation);
        boolean showToast = true;

        if (!folder.canWrite()) {
            if (showToast) {
                Toast.makeText(FolderPicker.this, R.string.dir_write_permission_denied, Toast.LENGTH_LONG).show();
            }
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(FolderPicker.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(FolderPicker.this);
        View view = inflater.inflate(R.layout.dialog_folder_name, null);
        builder.setView(view);
        builder.setTitle(
            isFolder
              ? getString(R.string.enter_folder_name)
              : mNewFilePrompt
        );

        final EditText et = view.findViewById(R.id.edit_text);

        final AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.create),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String name = et.getText().toString().trim();
                        if (name.isEmpty()) return;

                        if (isFolder) {
                            createNewFolder(name);
                        }
                        else {
                            if ((mFilePattern != null) && !mFilePattern.matcher(name).matches()) {
                                Toast.makeText(FolderPicker.this, getString(R.string.new_file_name_does_not_pass_filter), Toast.LENGTH_LONG).show();
                                return;
                            }

                            String data = mLocation + (mLocation.equals(File.separator) ? "" : File.separator) + name;

                            File file = new File(data);
                            if (file.exists()) {
                                Toast.makeText(FolderPicker.this, getString(R.string.new_file_name_already_exists), Toast.LENGTH_LONG).show();
                                return;
                            }

                            exit(data);
                        }
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (DialogInterface.OnClickListener)null);

        dialog.show();
    }

    /**
     * Show dialog to enter new folder name;
     * @param v
     */
    public void newFolderDialog(View v) {
        newFolderOrFileDialog(true);
    }

    /**
     * Select the destination folder or file.
     * @param v
     */
    public void select(View v) {

        if (mPickFile) {
            Toast.makeText(FolderPicker.this, getString(R.string.select_file), Toast.LENGTH_LONG).show();
        } else if (mNewFilePrompt != null) {
            newFolderOrFileDialog(false);
        } else if (mReceivedIntent != null) {
            if (mEmptyFolder && !isDirEmpty(mLocation)) {
                Toast.makeText(FolderPicker.this, getString(R.string.select_empty_folder), Toast.LENGTH_LONG).show();
                return;
            }
            mReceivedIntent.putExtra(EXTRA_DATA, mLocation);
            setResult(RESULT_OK, mReceivedIntent);
            finish();
        }
    }

    /**
     * Edit path manually.
     * @param v
     */
    public void edit(View v) {
        LayoutInflater inflater = LayoutInflater.from(FolderPicker.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(FolderPicker.this);
        View view = inflater.inflate(R.layout.dialog_edit_location, null);
        builder.setView(view);
        builder.setTitle(getString(R.string.edit_location));

        final EditText et = view.findViewById(R.id.edit_text);
        if (mLocation != null) {
            et.setText(mLocation);
            et.setSelection(mLocation.length());
        }

        final AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String location = et.getText().toString();
                        location = normalizeLocation(location);
                        checkAndLoadLists(location);
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (DialogInterface.OnClickListener)null);

        dialog.show();
    }

    /**
     * Check if folder is empty.
     * @param path
     * @return
     */
    boolean isDirEmpty(String path) {
        File dir = new File(path);
        File[] childs = dir.listFiles();
        return (childs == null || childs.length == 0);
    }

    @Override
    public void onBackPressed(){
        goBack(null);
    }

    /**
     * Load upper level path or exit.
     * @param v
     */
    public void goBack(View v) {
        if (mLocation != null && !mLocation.equals("") && !mLocation.equals("/")) {
            int start = mLocation.lastIndexOf('/');
            String newLocation = (start == 0) ? File.separator : mLocation.substring(0, start);

            if (!checkAndLoadLists(newLocation, false)) {
                exit();
            }
        } else {
            exit();
        }
    }

    public void cancel(View v) {
        exit();
    }

    /**
     * Set result and finish activity.
     */
    void exit() {
        exit(null);
    }

    void exit(String data) {
        if ((data == null) || data.isEmpty()) {
            setResult(RESULT_CANCELED, mReceivedIntent);
        }
        else {
            mReceivedIntent.putExtra(EXTRA_DATA, data);
            setResult(RESULT_OK, mReceivedIntent);
        }
        finish();
    }
}
