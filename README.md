#### [Android libraries](https://github.com/warren-bank/Android-libraries/tree/JakeWharton/SMSMorse)

__original application:__

* source code repo: [SMSMorse](https://github.com/JakeWharton/SMSMorse)
* author/copyright: [Jake Wharton](https://github.com/JakeWharton)
* license: _none specified_
* forked from commit SHA: [3f31267](https://github.com/JakeWharton/SMSMorse/tree/3f3126739b5b65f3abf14eb3a4b794626965406f)
  * date of commit: Mar 27, 2009
  * tag: 1.1.2

__screenshot:__

![SMSMorse](./.screenshots/1.jpg)
![SMSMorse](./.screenshots/2.jpg)

__notes:__

* what it does:
  * Broadcast receiver:
    * reads inbound SMS messages
    * reads the content of the message and the phone number of its sender
    * converts either/both of these values to Morse code
    * vibrates the Morse code
  * Activity:
    * configure preferences
* what I like:
  * quality of coding is excellent
  * the UI
    * minimal (in a good way)
      * contains only what is needed for the desired functionality, and no more

__changes:__

* added Gradle build scripts
