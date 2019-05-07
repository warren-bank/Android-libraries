#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/nbsp-team/MaterialFilePicker)

__original library:__

* source code repo: [MaterialFilePicker](https://github.com/nbsp-team/MaterialFilePicker)
* author/copyright: [nbsp-team](https://github.com/nbsp-team)
* licenses:
  * [Apache 2.0](https://apache.org/licenses/LICENSE-2.0.txt)
  * [GNU GPL 2.0](https://www.gnu.org/licenses/gpl-2.0.txt)
* forked from commit SHA: [aa706ea](https://github.com/nbsp-team/MaterialFilePicker/tree/aa706eafedbe594b26dd949a55bcff66aec82bf8)
  * date of commit: Apr 15, 2018

__screenshot:__

![MaterialFilePicker Sample](https://i.imgur.com/mjxs05n.png)

__notes:__

* what it does:
  * Activity that can be opened for a result
    * displays a list of file system directories and files
    * can navigate up or down the tree
    * can "cancel"
      * closes Activity
      * returns a result that indicates no selection was made
    * can "confirm" the selection of a file
      * closes Activity
      * returns a result that includes the selected file
* what I like:
  * though I haven't inspected the code yet, it compiles perfectly
    * unlike many other libraries, that require varying amounts of effort
  * the API:
    * makes it incredibly easy to open the Activity
  * the UI:
    * minimal (in a good way)
      * contains only what is needed for the desired functionality, and no more
* what I dislike:
  * for what it is:
    * almost nothing.. great library
  * for what it is not:
    * I wish that it included:
      * Activity to choose a directory
      * configuration option to choose multiple files

__changes:__

* Gradle build scripts
  * updated some of the dependencies
* unit tests
  * removed all test code and related dependencies
