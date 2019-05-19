#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/nujham/AnyCut)

__original application:__

* source code repo: [AnyCut](https://code.google.com/p/apps-for-android)
* author/copyright: [nujham](mailto:nujham@gmail.com)
* license: [Apache 2.0](https://apache.org/licenses/LICENSE-2.0.txt)
* forked from commit: [31026905d5bc](https://code.google.com/archive/p/apps-for-android/source/default/commits)
  * date of commit: 2012-07-10

__screenshot:__

![AnyCut](https://image.winudf.com/v2/image/Y29tLnNwcmluZy5iaXJkLmFueWN1dF9zY3JlZW5fMV8xNTI2MDE4NDg0XzAxMw/screen-1.jpg?h=500&fakeurl=1&type=.jpg)

__notes:__

* what it does:
  * Android application to create a homescreen shortcut that can launch another Activity
    * _Direct call_
      * chosen from _Contacts_
    * _Direct text message_
      * chosen from _Contacts_
    * _Activity_
      * chosen from sorted list of all _Activities_ that contain the filter _ACTION_MAIN_
    * _Make your own_
      * allows deep linking to an explicit _Activity_ by specifying:
        * _Action_
        * _Data_
          * -&gt; "Data URI"
        * _Type_
          * -&gt; "MIME Type"
* what I like:
  * absolutely everything
  * the APK is tiny (29 KB)
  * supports Android 1.0+
    * personally tested on Android 4.4 KitKat
      * works perfectly
* what I dislike:
  * that this app has been forgotten, and is now very difficult to find
    * links to F-Droid archive:
      * [v0.5 source code](https://f-droid.org/archive/com.example.anycut_5_src.tar.gz)
      * [v0.5 APK (built by F-Droid)](https://f-droid.org/archive/com.example.anycut_5.apk)

__changes:__

* added Gradle build scripts

__to do:__

* add ability to modify shortcut configuration w/ long-click
