#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/adityak368/Android-FileBrowser-FilePicker)

__original library:__

* source code repo: [Android-FileBrowser-FilePicker](https://github.com/adityak368/Android-FileBrowser-FilePicker)
* author/copyright: [adityak368](https://github.com/adityak368)
* license: [MIT](https://github.com/adityak368/Android-FileBrowser-FilePicker/raw/master/LICENSE)
* forked from commit SHA: [61c9ed1](https://github.com/adityak368/Android-FileBrowser-FilePicker/tree/61c9ed10ceceac19f96578935e531bff0006d362)
  * date of commit: Feb 2, 2019

__screenshot:__

![FileChooser Activity - single](https://cloud.githubusercontent.com/assets/19688735/25305188/6701de1e-2794-11e7-981f-7d6d0124b2b2.png)
![FileChooser Activity - multi](https://cloud.githubusercontent.com/assets/19688735/25305186/6701b33a-2794-11e7-8c58-d5da64e40768.png)

__notes:__

* what it does:
  * Activity that can be opened for a result
    * displays a list of file system directories and files
    * can navigate up or down the tree
    * can "cancel"
      * closes Activity
      * returns a result that indicates no selection was made
    * can "confirm" the selection of file(s)
      * closes Activity
      * returns a result that includes the selected file(s)
* what I like:
  * code is very well organized
* what I dislike:
  * cannot select (one or more) directories
  * the UI
    * list of drives is incomplete, and reported sizes are inaccurate
    * "Last Mofified"
    * when the Activity is configured to allow the selection of only a single file:
      * long click on any file enables multi selection mode
      * when multiple selections are made:
        * a warning message is displayed in a Toast
        * the Activity returns to single selection mode
    * when the Activity is configured to allow the selection of multiple files:
      * still need to long click on any file to enables multi selection mode
    * when multiple files are selected
      * clicking the "info" icon:
        * displays the combined disk space of all files
        * the Activity returns to single selection mode
  * includes unwanted functionality:
    * create new directory
    * sort
    * filter
    * info (single selection mode)
    * info (multi  selection mode)
  * includes several dependencies:
    * adds about 1MB to size of final APK
    * one of them (currently) causes ProGuard to fail
      * [the workaround](https://github.com/roughike/BottomBar/issues/456) is to add a rule to the ProGuard config file
* conclusions:
  * project makes a good attempt
  * imho, not quite ready for use

__changes:__

* Gradle build scripts
  * updated some of the dependencies
* unit tests
  * removed all test code and related dependencies
* sample app
  * added to demo the library
