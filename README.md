#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/MostafaNasiri/AndroidFileChooser)

__original library:__

* source code repo: [AndroidFileChooser](https://github.com/MostafaNasiri/AndroidFileChooser)
* author/copyright: [Mostafa Nasiri](https://github.com/MostafaNasiri)
* license: [Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0)
* forked from commit SHA: [7add21d](https://github.com/MostafaNasiri/AndroidFileChooser/tree/7add21d4c1dba950eb3185009eec6911653afe34)
  * date of commit: Dec 25, 2018

__screenshot:__

![FileChooser Activity](https://github.com/MostafaNasiri/AndroidFileChooser/raw/7add21d4c1dba950eb3185009eec6911653afe34/AndroidFileChooser.png)

__notes:__

* what it does:
  * Fragment is created through a Builder
  * Builder allows for a lot of customization:
    * starting directory
    * text color
    * icons:
      * directory
      * file
      * navigate up (parent directory)
    * selection mode: files or directories
      * file selection mode: one or multiple
      * file filter(s)
    * buttons (color, font-size, background-color, content):
      * multiple file selection
      * directory selection
    * callback function:
      * passed a String containing the user's selection
* what I like:
  * easy to customize
  * can be used to either select file(s) or a directory
* what I dislike:
  * absolutely nothing.. seems like a good library

__changes:__

* Gradle build scripts
  * upgraded Gradle
  * updated many of the dependencies
* unit tests
  * removed all test code and related dependencies
* VectorDrawableCompat
  * added as a dependency
  * used by `FileChooser` to load XML files referenced in `R.drawable`
  * replaces call to: [`ContextCompat.getDrawable()`](https://github.com/MostafaNasiri/AndroidFileChooser/blob/7add21d4c1dba950eb3185009eec6911653afe34/android-file-chooser/src/main/java/ir/sohreco/androidfilechooser/FileChooser.java#L509)
    * which was throwing [`Resources$NotFoundException`](https://medium.com/androiddevelopers/appcompat-v23-2-age-of-the-vectors-91cbafa87c88) when tested on Android 4.4 (KitKat)
