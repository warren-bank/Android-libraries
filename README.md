#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/fork/kashifo/android-folder-picker-library/PR13-gee12)

__original application:__

* source code repo: [android-folder-picker-library](https://github.com/gee12/android-folder-picker-library)
* author/copyright: [gee12](https://github.com/gee12)
* license: [Apache 2.0](https://github.com/gee12/android-folder-picker-library/blob/d85e79b697664cc081fef43dea431aa53a8e3b47/LICENSE)
* forked from commit SHA: [d85e79b](https://github.com/gee12/android-folder-picker-library/tree/d85e79b697664cc081fef43dea431aa53a8e3b47)
  * date of commit: Jan 04, 2021
  * PR: [#13](https://github.com/kashifo/android-folder-picker-library/pull/13)

__screenshot:__

| ![folderpicker](./.screenshots/1.png) | ![folderpicker](./.screenshots/2.png) | ![folderpicker](./.screenshots/3.png) |
|:-----------------:|:-------------------:|:---------------:|
| Can pick a folder | Can create a folder | Can pick a file |

__notes:__

* what it does:
  * Activity that can be opened for a result
    * starting Intent can configure some features and behavior
      * `title`<br>_string_: prominently displayed in top center
      * `desc`<br>_string_: subtitle displayed in smaller font below `title`
      * `location`<br>_string_: initial directory path
      * `pickFiles`<br>_boolean_:
        * _true_: choose a file
        * _false_: choose a directory (default)
      * `emptyFolder`<br>_boolean_:
        * _true_: when choosing a directory, only accept a directory selection that doesn't contain any contents (files or directories)
        * _false_: when choosing a directory, accept any (default)
    * displays a list of file system directories and files
    * can navigate up or down the tree
    * can "cancel"
      * closes Activity
      * returns a result that indicates no selection was made
    * when choosing a directory:
      * can create a "new" subdirectory in the current directory
      * can "select" the current directory
        * closes Activity
        * returns a result that includes the selected directory
    * when choosing a file:
      * can click on a file in the current directory
        * closes Activity
        * returns a result that includes the selected file
* what this fork adds:
  * "edit" button to change the current directory by manually entering a file path with the keyboard
  * "home" button to change the current directory to the [primary shared/external storage directory](https://developer.android.com/reference/android/os/Environment#getExternalStorageDirectory())
* what I like:
  * size of the library is incredibly compact
    * no dependencies
  * supports nearly all versions of Android
    * minSDK = 9
  * quality of coding is excellent
  * the UI:
    * minimal (in a good way)
      * contains only what is needed for the desired functionality, and no more
* what I dislike:
  * almost nothing.. great library
    * I only _dislike_ that I didn't find it sooner! :)

__changes:__

* added Gradle build scripts
* updated demo app:
  * removed support library
  * lowered minSDK to match the library
