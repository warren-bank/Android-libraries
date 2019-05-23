#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/father2sisters/scale_videoview)

__original application:__

* source code repo: [scale_videoview](https://github.com/father2sisters/scale_videoview)
* author/copyright: [Father n 2 sisters](http://father2sisters.blogspot.com/2014/07/resize-videoview-on-android-with-pinch.html)
* license: _none specified_
* forked from commit SHA: [e5e5c56](https://github.com/father2sisters/scale_videoview/tree/e5e5c56be952077f428e2cdd8d2fbb51f911ef69)
  * date of commit: Jul 4, 2014

__screenshot:__

[![scale_videoview](https://img.youtube.com/vi/87Rlg4HUdH0/0.jpg)](https://www.youtube.com/watch?v=87Rlg4HUdH0)

__notes:__

* what it does:
  * bare-bones app to demonstrate usage of gesture detection to control and scale a `VideoView` component
* what I like:
  * it works, as a proof-of-concept
    * needs a lot of work
* what I dislike:
  * not written as a reusable library
  * assumes the `VideoView` is wrapped by a `ViewGroup`
  * resizes the `ViewGroup` as well as the `VideoView`
  * when the width of the `ViewGroup` exceeds the width of the screen
    * there is no way to scroll the video to see the portion of it that is off-screen

__considerations:__

* [Android Developers Blog](https://android-developers.googleblog.com/2011/11/android-40-graphics-and-animations.html)
  * introducing: [TextureView](https://developer.android.com/reference/android/view/TextureView.html)
* [Stack Overflow discussion](https://stackoverflow.com/questions/14590852/android-textureview-vs-videoview-performance)
  * performance comparison: [SurfaceView](https://developer.android.com/reference/android/view/SurfaceView.html) (ex: [VideoView](https://developer.android.com/reference/android/widget/VideoView)) vs. [TextureView](https://developer.android.com/reference/android/view/TextureView.html)
  * summary:
    * `SurfaceView` skips the normal view-compositing for rendering
      - goes straight to the GPU
      - "does not live in the application's Window, and cannot be transformed (ie: moved, scaled, rotated) efficiently"
      - performs much better on low-end hardware
    * `TextureView` "offers the same capabilities as `SurfaceView` but behaves as a regular `View`"
      - performs much worse on low-end hardware

__related:__

* [ZoomableTextureView](https://github.com/Manuiq/ZoomableTextureView)
