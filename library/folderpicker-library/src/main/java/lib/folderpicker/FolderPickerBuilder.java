package lib.folderpicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class FolderPickerBuilder {
    // used by: getIntent()
    private Integer EXTRA_THEME;
    private String  EXTRA_TITLE;
    private String  EXTRA_DESCRIPTION;
    private String  EXTRA_LOCATION;
    private Boolean EXTRA_PICK_FILES;
    private Boolean EXTRA_EMPTY_FOLDER;

    // required by: getIntent()
    // can be substituted for by: withActivity()
    private Context mContext;

    // required by: start(), which calls: getIntent()
    private Activity mActivity;
    private Integer mRequestCode;

    // internal
    private Class<? extends FolderPicker> mFolderPickerClass = FolderPicker.class;

    // constructor can only be called by: FolderPicker.withBuilder()
    // public method is static
    // builder methods are chained and ultimately either: return an Intent, or start the Intent for a result
    protected FolderPickerBuilder() {
    }

    public FolderPickerBuilder withTheme(int theme) {
        EXTRA_THEME = theme;
        return this;
    }
    public FolderPickerBuilder withTitle(String title) {
        EXTRA_TITLE = title;
        return this;
    }
    public FolderPickerBuilder withDescription(String description) {
        EXTRA_DESCRIPTION = description;
        return this;
    }
    public FolderPickerBuilder withPath(String location) {
        EXTRA_LOCATION = location;
        return this;
    }
    public FolderPickerBuilder withFilePicker(boolean pickFiles) {
        EXTRA_PICK_FILES = pickFiles;
        return this;
    }
    public FolderPickerBuilder withEmptyFolder(boolean emptyFolder) {
        EXTRA_EMPTY_FOLDER = emptyFolder;
        return this;
    }
    public FolderPickerBuilder withContext(Context context) {
        mContext = context;
        return this;
    }
    public FolderPickerBuilder withActivity(Activity activity) {
        if (mContext == null)
          mContext = (Context) activity;

        mActivity = activity;
        return this;
    }
    public FolderPickerBuilder withRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    public Intent getIntent() {
        if (mContext == null)
            return null;

        Intent intent = new Intent(mContext, mFolderPickerClass);

        if (EXTRA_THEME != null)
            intent.putExtra(FolderPicker.EXTRA_THEME, EXTRA_THEME);
        if (EXTRA_TITLE != null)
            intent.putExtra(FolderPicker.EXTRA_TITLE, EXTRA_TITLE);
        if (EXTRA_DESCRIPTION != null)
            intent.putExtra(FolderPicker.EXTRA_DESCRIPTION, EXTRA_DESCRIPTION);
        if (EXTRA_LOCATION != null)
            intent.putExtra(FolderPicker.EXTRA_LOCATION, EXTRA_LOCATION);
        if (EXTRA_PICK_FILES != null)
            intent.putExtra(FolderPicker.EXTRA_PICK_FILES, EXTRA_PICK_FILES);
        if (EXTRA_EMPTY_FOLDER != null)
            intent.putExtra(FolderPicker.EXTRA_EMPTY_FOLDER, EXTRA_EMPTY_FOLDER);

        return intent;
    }

    public boolean start() {
        if ((mActivity == null) || (mRequestCode == null))
            return false;

        Intent intent = getIntent();
        if (intent == null)
            return false;

        mActivity.startActivityForResult(intent, mRequestCode);
        return true;
    }
}
