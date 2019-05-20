/*
    Open Manager For Tablets, an open source file manager for the Android system
    Copyright (C) 2011  Joe Berria <nexesdevelopment@gmail.com>

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;
import java.io.File;
import java.io.IOException;

import android.util.Log;

/**
 * This class is completely modular, which is to say that it has
 * no reference to the any GUI activity. This class could be taken
 * and placed into in other java (not just Android) project and work.
 * <br>
 * <br>
 * This class handles all file and folder operations on the system.
 * This class dictates how files and folders are copied/pasted, (un)zipped
 * renamed and searched. The EventHandler class will generally call these
 * methods and have them performed in a background thread. Threading is not
 * done in this class.
 *
 * @author Joe Berria
 *
 */
public class FileManager {
	private static final int BUFFER = 		2048;
	private static final int SORT_NONE = 	0;
	private static final int SORT_ALPHA = 	1;
	private static final int SORT_TYPE = 	2;
	private static final int SORT_SIZE = 	3;

	private boolean mShowHiddenFiles = false;
	private int mSortType = SORT_ALPHA;
	private long mDirSize = 0;
    private String homeDir;
	private Stack<String> mPathStack;
	private ArrayList<String> mDirContent;

	/**
	 * Constructs an object of the class
	 * <br>
	 * this class uses a stack to handle the navigation of directories.
	 */
	public FileManager() {
		mDirContent = new ArrayList<String>();
		mPathStack  = new Stack<String>();

        setHomeDir("/sdcard");
	}

	/**
	 * This will return a string of the current directory path
	 * @return the current directory
	 */
	public String getCurrentDir() {
		return mPathStack.peek();
	}

	/**
	 * This will return a string of the current home path.
	 * @return	the home directory
	 */
	public ArrayList<String> setHomeDir(String name) {
        homeDir = (name == null) ? "" : name.trim();

        mPathStack.clear();
        mPathStack.push("/");

        String[] dirs = homeDir.split("/");
        String   dir = "";
        for (int i=0; i < dirs.length; i++) {
            if (dirs[i].length() > 0) {
                dir += "/" + dirs[i];
                mPathStack.push(dir);
            }
        }

        return populate_list();
    }

    public String getHomeDir() {
        return homeDir;
    }

    public ArrayList<String> gotoHomeDir() {
        return setHomeDir(homeDir);
    }

	/**
	 * This will determine if hidden files and folders will be visible to the
	 * user.
	 * @param choice	true if user is veiwing hidden files, false otherwise
	 */
	public void setShowHiddenFiles(boolean choice) {
		mShowHiddenFiles = choice;
	}

	/**
	 *
	 * @param type
	 */
	public void setSortType(int type) {
		mSortType = type;
	}

	/**
	 * This will return a string that represents the path of the previous path
	 * @return	returns the previous path
	 */
	public ArrayList<String> getPreviousDir() {
		int size = mPathStack.size();

		if (size >= 2)
			mPathStack.pop();

		else if(size == 0)
			mPathStack.push("/");

		return populate_list();
	}

	/**
	 *
	 * @param path
	 * @param isFullPath
	 * @return
	 */
	public ArrayList<String> getNextDir(String path, boolean isFullPath) {
		int size = mPathStack.size();

		if(!path.equals(mPathStack.peek()) && !isFullPath) {
			if(size == 1)
				mPathStack.push("/" + path);
			else
				mPathStack.push(mPathStack.peek() + "/" + path);
		}

		else if(!path.equals(mPathStack.peek()) && isFullPath) {
			mPathStack.push(path);
		}

		return populate_list();
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public boolean isDirectory(String name) {
		return new File(mPathStack.peek() + "/" + name).isDirectory();
	}

	/**
	 * converts integer from wifi manager to an IP address.
	 *
	 * @param des
	 * @return
	 */
	public static String integerToIPAddress(int ip) {
		String ascii_address = "";
		int[] num = new int[4];

		num[0] = (ip & 0xff000000) >> 24;
		num[1] = (ip & 0x00ff0000) >> 16;
		num[2] = (ip & 0x0000ff00) >> 8;
		num[3] = ip & 0x000000ff;

		ascii_address = num[0] + "." + num[1] + "." + num[2] + "." + num[3];

		return ascii_address;
	 }

	/**
	 *
	 * @param dir
	 * @param pathName
	 * @return
	 */
	public ArrayList<String> searchInDirectory(String dir, String pathName) {
		ArrayList<String> names = new ArrayList<String>();
		search_file(dir, pathName, names);

		return names;
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public long getDirSize(String path) {
		get_dir_size(new File(path));

		return mDirSize;
	}


	private static final Comparator alph = new Comparator<String>() {
		@Override
		public int compare(String arg0, String arg1) {
			return arg0.toLowerCase().compareTo(arg1.toLowerCase());
		}
	};

	private final Comparator size = new Comparator<String>() {
		@Override
		public int compare(String arg0, String arg1) {
			String dir = mPathStack.peek();
			Long first = new File(dir + "/" + arg0).length();
			Long second = new File(dir + "/" + arg1).length();

			return first.compareTo(second);
		}
	};

	private final Comparator type = new Comparator<String>() {
		@Override
		public int compare(String arg0, String arg1) {
			String ext = null;
			String ext2 = null;
			int ret;

			try {
				ext = arg0.substring(arg0.lastIndexOf(".") + 1, arg0.length()).toLowerCase();
				ext2 = arg1.substring(arg1.lastIndexOf(".") + 1, arg1.length()).toLowerCase();

			} catch (IndexOutOfBoundsException e) {
				return 0;
			}
			ret = ext.compareTo(ext2);

			if (ret == 0)
					return arg0.toLowerCase().compareTo(arg1.toLowerCase());

			return ret;
		}
	};

	/* (non-Javadoc)
	 * this function will take the string from the top of the directory stack
	 * and list all files/folders that are in it and return that list so
	 * it can be displayed. Since this function is called every time we need
	 * to update the the list of files to be shown to the user, this is where
	 * we do our sorting (by type, alphabetical, etc).
	 *
	 * @return
	 */
	private ArrayList<String> populate_list() {

		if(!mDirContent.isEmpty())
			mDirContent.clear();

		File file = new File(mPathStack.peek());

		if(file.exists() && file.canRead()) {
			String[] list = file.list();
			int len = list.length;
            int index;
            Object[] dirs_ar;
            String dir;

			/* add files/folder to arraylist depending on hidden status */
			for (index = 0; index < len; index++) {
				if(!mShowHiddenFiles) {
					if(list[index].toString().charAt(0) != '.')
						mDirContent.add(list[index]);

				} else {
					mDirContent.add(list[index]);
				}
			}

			/* sort the arraylist that was made from above for loop */
			switch(mSortType) {
				case SORT_NONE:
					//no sorting needed
					break;

				case SORT_ALPHA:
					index = 0;
					dirs_ar = mDirContent.toArray();
					dir = mPathStack.peek();

					Arrays.sort(dirs_ar, alph);

					mDirContent.clear();
					for (Object a : dirs_ar) {
						if(new File(dir + "/" + (String)a).isDirectory())
							mDirContent.add(index++, (String)a);
						else
							mDirContent.add((String)a);
					}
					break;

				case SORT_SIZE:
					index = 0;
					dirs_ar = mDirContent.toArray();
					dir = mPathStack.peek();

					Arrays.sort(dirs_ar, size);

					mDirContent.clear();
					for (Object a : dirs_ar) {
						if(new File(dir + "/" + (String)a).isDirectory())
							mDirContent.add(index++, (String)a);
						else
							mDirContent.add((String)a);
					}
					break;

				case SORT_TYPE:
					index = 0;
					dirs_ar = mDirContent.toArray();
					dir = mPathStack.peek();

					Arrays.sort(dirs_ar, type);
					mDirContent.clear();

					for (Object a : dirs_ar) {
						if(new File(dir + "/" + (String)a).isDirectory())
							mDirContent.add(index++, (String)a);
						else
							mDirContent.add((String)a);
					}
					break;
			}

		} else {
			mDirContent.add("Emtpy");
		}

		return mDirContent;
	}

	/*
	 *
	 * @param path
	 */
	private void get_dir_size(File path) {
		File[] list = path.listFiles();
		int len;

		if(list != null) {
			len = list.length;

			for (int i = 0; i < len; i++) {
				try {
					if(list[i].isFile() && list[i].canRead()) {
						mDirSize += list[i].length();

					} else if(list[i].isDirectory() && list[i].canRead() && !isSymlink(list[i])) {
						get_dir_size(list[i]);
					}
				} catch(IOException e) {
					Log.e("IOException", e.getMessage());
				}
			}
		}
	}

	// Inspired by org.apache.commons.io.FileUtils.isSymlink()
	private static boolean isSymlink(File file) throws IOException {
		File fileInCanonicalDir = null;
		if (file.getParent() == null) {
			fileInCanonicalDir = file;
		} else {
			File canonicalDir = file.getParentFile().getCanonicalFile();
			fileInCanonicalDir = new File(canonicalDir, file.getName());
		}
		return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
	}

	/*
	 * (non-JavaDoc)
	 * I dont like this method, it needs to be rewritten. Its hacky in that
	 * if you are searching in the root dir (/) then it is not going to be treated
	 * as a recursive method so the user dosen't have to sit forever and wait.
	 *
	 * I will rewrite this ugly method.
	 *
	 * @param dir		directory to search in
	 * @param fileName	filename that is being searched for
	 * @param n			ArrayList to populate results
	 */
	private void search_file(String dir, String fileName, ArrayList<String> n) {
		File root_dir = new File(dir);
		String[] list = root_dir.list();

		if(list != null && root_dir.canRead()) {
			int len = list.length;

			for (int i = 0; i < len; i++) {
				File check = new File(dir + "/" + list[i]);
				String name = check.getName();

				if(check.isFile() && name.toLowerCase().
										contains(fileName.toLowerCase())) {
					n.add(check.getPath());
				}
				else if(check.isDirectory()) {
					if(name.toLowerCase().contains(fileName.toLowerCase()))
						n.add(check.getPath());

					else if(check.canRead() && !dir.equals("/"))
						search_file(check.getAbsolutePath(), fileName, n);
				}
			}
		}
	}
}
