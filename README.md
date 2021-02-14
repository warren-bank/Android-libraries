#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/kashifo/android-folder-picker-library)

__original application:__

* source code repo: [android-folder-picker-library](https://github.com/kashifo/android-folder-picker-library)
* author/copyright: [Kashif Anwaar](https://github.com/kashifo)
* license: [Apache 2.0](https://github.com/kashifo/android-folder-picker-library/blob/f9d1ea948ca63333540432d7fcf5276b071994df/LICENSE)
* forked from commit SHA: [f9d1ea9](https://github.com/kashifo/android-folder-picker-library/tree/f9d1ea948ca63333540432d7fcf5276b071994df)
  * date of commit: May 23, 2018
  * tag: v2.4

__screenshot:__

| ![folderpicker](./.screenshots/1.png) | ![folderpicker](./.screenshots/2.png) | ![folderpicker](./.screenshots/3.png) |
|:-----------------:|:-------------------:|:---------------:|
| Can pick a folder | Can create a folder | Can pick a file |

__notes:__

* what it does:
  * Activity that can be opened for a result
    * starting Intent can configure some features and behavior
      * `title`<br>_string_: prominently displayed in top center
      * `location`<br>_string_: initial directory path
      * `pickFiles`<br>_boolean_:
        * _true_: choose a file
        * _false_: choose a directory (default)
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
