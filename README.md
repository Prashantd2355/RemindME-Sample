# RemindME - The Location Based Reminder

A location based reminder library for Android. The library will provide about 5 diffrent location based reminder feature ready to implement.

[![JitPack](https://jitpack.io/v/Prashantd2355/RemindME-Library.svg)](https://jitpack.io/#Prashantd2355/RemindME-Library)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/kizitonwose/CalendarView/blob/master/LICENSE.md) 

**With this library, you can access library features like this.**

![Preview](https://raw.githubusercontent.com/Prashantd2355/RemindME-Library/master/images/image-all.png)

## Methods
1. Location Based Reminder
2. Calendar Events with one click navigate to event location
3. Nearby Landmarks - find near by place
4. Contact Book - Manage contact address book with no of visits log
5. Phone Call Reminder - Remind to call on specified geographical area

## Sample project

It's very important to check out the sample app. Most techniques that you would want to implement are already implemented in the example.

Download the sample app [here](https://github.com/Prashantd2355/RemindME-Library/releases/download/1.0/sample.apk)

## Setup

The library uses java.time classes via Java 8+ API desugaring for backward compatibility since these classes were added in Java 8.

#### Step 1

Add the JitPack repository to your project level `build.gradle`:

```groovy
allprojects {
 repositories {
    google()
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

Add RemindME-library to your app `build.gradle`:

```groovy
dependencies {
	implementation 'com.github.Prashantd2355:RemindME-Library:<latest-version>'
}
```

You can find the latest version of `RemindMe-Library` on the JitPack badge above the preview images.
