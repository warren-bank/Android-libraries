#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/iamcxa/android-play-rtsp-by-videoview)

__original application:__

* source code repo: [android-play-rtsp-by-videoview](https://github.com/iamcxa/android-play-rtsp-by-videoview)
* author/copyright: [Kent Chen](https://github.com/iamcxa)
* license: _none specified_
* forked from commit SHA: [9f037df](https://github.com/iamcxa/android-play-rtsp-by-videoview/tree/9f037dfb44d08385e5ad0022dcf231ae1cb2199c)
  * date of commit: Sep 6, 2016

__screenshot:__

![Android-File-Manager](./.screenshots/1.png)

__notes:__

* what it does:
  * bare-bones app to demonstrate usage of the `VideoView` component to play RTSP video streams

__changes:__

* replaced Gradle build scripts
* renamed package ID
* removed dependency on Android Support library
  * `appcompat-v7` was used to customize the theme
* removed custom theme
* replaced URL of default RTSP stream
  * the origin is now: `404`
  * [this _Big Buck Bunny_ RTSP stream](rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov) was found on the [demo page for _Streamedian_](http://streamedian.com/demo/free/)
* reduced the _minSDK_
  * currently set to: `14` (Android 4.0 ICS)
  * could probably be farther reduced to: `1`

__docs:__

* [`VideoView` component](https://developer.android.com/reference/android/widget/VideoView.html)
* [supported media formats](https://developer.android.com/guide/appendix/media-formats.html)
