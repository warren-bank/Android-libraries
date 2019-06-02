package net.rdrei.android.dirchooser;

import android.os.Parcelable;

import auto.parcel.AutoParcel;

@AutoParcel
public abstract class DirectoryChooserConfig implements Parcelable {
    /**
     * @return Builder for a new DirectoryChooserConfig.
     */
    public static Builder builder() {
        return new AutoParcel_DirectoryChooserConfig.Builder()
                .initialDirectory("");
    }

    /**
     * Optional argument to define the path of the directory
     * that will be shown first.
     * If it is not sent or if path denotes a non readable/writable directory
     * or it is not a directory, it defaults to
     * {@link android.os.Environment#getExternalStorageDirectory()}
     */
    abstract String initialDirectory();


    @AutoParcel.Builder
    public abstract static class Builder {
        public abstract Builder initialDirectory(String s);
        public abstract DirectoryChooserConfig build();
    }
}
