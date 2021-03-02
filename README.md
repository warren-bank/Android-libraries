#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/306470558/Android_show_screen_rtsp)

__original application:__

* source code repo: [Android_show_screen_rtsp](https://github.com/306470558/Android_show_screen_rtsp)
* author/copyright: [306470558](https://github.com/306470558)
* license: _none specified_
* forked from commit SHA: [5adbcb3](https://github.com/306470558/Android_show_screen_rtsp/tree/5adbcb3627df934c40c3542edab19216c00dc0d8)
  * date of commit: Dec 20, 2018

__notes:__

* what it does:
  * uses the [libstreaming](https://github.com/fyhertz/libstreaming) library to run a local RTSP server to broadcast a video stream that shows the screen on the local Android device
* what I like:
  * structure of project is clean and concise
  * it works (in a technical sense)
* what I dislike:
  * the video stream frame rate is not constant
    - it appears to be dependent on user interaction with the screen, which triggers repaint

__changes:__

* updated Gradle build scripts
* refactored translatable strings to xml
* removed unnecessary support libraries
* removed unnecessary plumbing for unit tests
