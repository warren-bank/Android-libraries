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
                .initialDirectory("")
                .allowReadOnlyDirectory(false);
    }

    /**
     * Optional argument to define the path of the directory
     * that will be shown first.
     * If it is not sent or if path denotes a non readable/writable directory
     * or it is not a directory, it defaults to
     * {@link android.os.Environment#getExternalStorageDirectory()}
     */
    abstract String initialDirectory();

    /**
     * Argument to define whether or not the directory chooser
     * allows read-only paths to be chosen. If it false only
     * directories with read-write access can be chosen.
     */
    abstract boolean allowReadOnlyDirectory();


    @AutoParcel.Builder
    public abstract static class Builder {
        public abstract Builder initialDirectory(String s);
        public abstract Builder allowReadOnlyDirectory(boolean b);
        public abstract DirectoryChooserConfig build();
    }
}
