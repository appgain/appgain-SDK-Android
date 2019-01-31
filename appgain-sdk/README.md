# appgain-SDK-Android

**Features**

-   One-line to initialize and integrate in your the app ,published on iOs
    cocapods and android bintary

-   available for iOS and Android

-   open source : its code is available for public on [Github](https://github.com/appgain/)

-   Parse Server SDK initialization : no need to write extra code to initialize
    parse server SDK , its initialization is embedded in the
    [Appgain.io](http://appgain.io/) SDK initialization

-   User Push token identification : link each user push token with his app
    userId , enabling O2O communication

-   Deferred Deep Linking :

	-   for identifying the user app install acquisition channel

	-   to route the user flow to target app screen even if he didn't have the app
    installed before opening the smart Deep Link

-   Smart deep linking creation : create smart deep links within your app , for example on user share app content , this enables use cases like user rewarding

-   Mobile Deep Pages creation : create mobile deep pages  within your app , for example on user share app content , this enables use cases like user rewarding


-   Push Notifications Conversation Tracking :

	-   Track Push Message Received Event (Android only )

	-   Track Push Message Dismissed Event (Android only )

	-   Track Push Message Open Event

	-   Track Push MessageConversion Event

-   App Marketing Automation : trigger multi channel messaging predefined
    scenarios based on user In-App actions

-   Built-in User communication preference management : user can opt-out /opt-in
    to in app push, email, SMS channels




### Installing SDK


Android Studio uses [Gradle](http://www.gradle.org/) (an advanced build toolkit) to automate and manage the build process while allowing you to define flexible custom build configurations. You can refer to [Android Studio’s Build Guide](http://developer.android.com/studio/build/index.html) for additional details.

To install Appgain SDK for Android in your Android Studio project, open your app/build.gradle file:

1-Add following line under dependencies:


![](https://lh5.googleusercontent.com/W-8j1nC5EBMyd8VRB8O16o7Ygl7lMXXt6FFHmGzul8tjsMK-cdBv7CBtWU5Cp0GZq_D1YV7ZmATaLaEe3sB9w8IwsEx2qLeINm5aAtwVFyt81TUeRlyxKz7fn15r3MO4Cx532WjT)

![Screenshot (33)](https://lh4.googleusercontent.com/1atsOhE3x6dNeqHPTtCBdOCtBLc2bQZQITYZhXjJ658TLmJcKHA0fGSsP52ROEKGKXxJhLtsbj3BiAZAlg_4lxNBXgStGWZatEcXlIXeT3K8oqdtlccJoKX12YzY4Y2PDrT1UcOS)


2-Sync your build.gradle and we are ready to go.


![Screenshot (34)](https://lh3.googleusercontent.com/43lRhn5KAKjAC0YyIiMwnirNUXrZSghEt8vEP8xA0tf45Gaw7ldgq9qluOjgoNMPbmL9xUGpH0UtvtvAyhsU8wHBHIgMyuJpHaBdN86BaTMK0mFxeGq9RP6ZI893zKKwLtVjYi5A)


3 - update your manifest permissions  
if you don’t have app backend or you not using parse you will need only the following permissions:

![](https://lh6.googleusercontent.com/E32SCXiaAUZii_fRqh5hwK3O3PeLlra6oDAhAHn4nb-eAivL0vJVY_MPyrZEn0mstMHqz1MTnr7XhaOQUdajAF1db7m2c5OPkYaEq8kYSLSZrV3-n9nzBJKWw67xw1WN9Y-Z1xDX)

4 - Create a new class:

4.1 make new class called AppController and extend application like that:

![Screenshot (35)](https://lh5.googleusercontent.com/6BzLtPln9PM20esGZ14pvRaKkZJMToAztWaxRFkF1zKrItbjSBE1S9mgQ5F2hNVHfaHEXhLYBoJ4Bh34HJ8pEapaXYguVODaUcZ4XCQfQhJdJDiE_Cp3gqrRKYuoiNP7XBcZqql6)



4.2 Add your new class name in name attribute inside application tag in your manifest file:


![Screenshot (36)](https://lh4.googleusercontent.com/5UIur1vfbg0OSZcZPqpV4qhNUP8M5kc66UQ9b8q-nmVOs-JS8VagOTg3287jEiB14UftkpIyRgpUyd25gIEwOYQh2li-NgK6832SLusJzt28EBL40w9o_feBBh8qI5ZpP8YEfhm_)



After SDK installation, now we would explain how to use the Appgain.io products with your app:



1.  [Smart deep Link Creation](https://docs.appgain.io/SDK/android/smartDeepLinkCreation/)

2.  [Deferred Deep Linking](https://docs.appgain.io/SDK/android/deferredDeepLinking/)

3.  [Mobile Deep Pages Creation](https://docs.appgain.io/SDK/android/mobileDeepPagesCreation/)

4.  [Parse Server Setup](https://docs.appgain.io/SDK/android/parseServer/)

