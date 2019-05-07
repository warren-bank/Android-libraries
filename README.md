#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/iPaulPro/aFileChooser)

__original library:__

* source code repo: [aFileChooser](https://github.com/iPaulPro/aFileChooser)
* author/copyright: [Paul Burke](https://github.com/iPaulPro)
* license: [Apache 2.0](https://apache.org/licenses/LICENSE-2.0.txt)
* forked from commit SHA: [48d65e6](https://github.com/iPaulPro/aFileChooser/tree/48d65e6649d4201407702b0390326ec9d5c9d17c)
  * date of commit: Jan 4, 2014

__screenshot:__

![aFileChooser Sample](https://github.com/iPaulPro/aFileChooser/raw/48d65e6649d4201407702b0390326ec9d5c9d17c/screenshot-2.png)

__notes:__

* what it does:
  * Activity that can be opened for a result
    * left navigation drawer contains a selection of root directories
    * choosing any such root directory displays a list of file system subdirectories and files
    * can navigate up or down the tree
    * can "cancel"
      * closes Activity
      * returns a result that indicates no selection was made
    * can "confirm" the selection of a file
      * closes Activity
      * returns a result that includes the selected file
* what I like:
  * the APK is tiny (200 KB)
* what I'm "on the fence" about:
  * the navigation drawer:
    * it is _very_ functional, and yet.. it feels a bit cluttered
    * seems like the vast majority of the options would never be used, and yet.. nice to have?
* what I dislike:
  * in order to view the external SD card, need to configure:
    * `Settings` &gt; `Display Advanced Devices`
  * there is an issue with selecting files from the external SD card:
    * given that:
      * the setting is configured to display this device in the navigation drawer
      * the permission to read from this device is granted in the sample app Manifest
      * the file system is displayed correctly in the Activity
    * and yet..
      * when a file is selected, the Activity returns a `null` value

__changes:__

* Gradle build scripts
  * updated the dependencies
    * side effects:
      * as the result of updating _Android Support Library v4_:
        * from: revision 19 (Oct, 2013)
        * to: revision 28 (Sep, 2018)
      * the minSdk was increased:
        * from: API 7 (Android 2.1 Eclair)
        * to: API 14 (Android 4.0 Ice Cream Sandwich)
