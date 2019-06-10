#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/ricardojlrufino/Shell)

__original library:__

* source code gist: [Shell.java](https://gist.github.com/ricardojlrufino/61dbc1e9a8120862791e71287b17fef8)
* author/copyright: [Ricardo JL Rufino](https://github.com/ricardojlrufino)
* license: _none specified_
* forked from commit SHA: [adfbf58](https://gist.github.com/ricardojlrufino/61dbc1e9a8120862791e71287b17fef8/raw/adfbf58830886eceb79fb7dd93747f7c07e542b2/Shell.java)
  * date of commit: Apr 28, 2017

__notes:__

* what it does:
  * utility class containing static methods to execute commands as `root` user on a rooted device

__changes:__

* refactored code
* changed signature of `execForResult`
  * returns instance of `Shell.Result` inner-class, which contains:
    * String stdout
    * String stderr
    * int status
* added `execScriptForResult`
* added `execScriptForResult` and `execScript` variations that take as input any of:
  * String
    * ex: `execScript("/path/to/script.sh")`
  * File
    * ex: `execScript(new File("/path/to/script.sh"))`
* renamed `startADB` to `startADBd`
* added:
  * `getADBdPort`
  * `isADBdRunning`
  * `stopADBd`
  * `getWlanIpAddress`
