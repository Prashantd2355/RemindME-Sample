# RemindME - The Location Based Reminder

A location based reminder library for Android. The library will provide about 5 diffrent location based reminder feature ready to implement.

[![JitPack](https://jitpack.io/v/Prashantd2355/RemindME-Library.svg)](https://jitpack.io/#Prashantd2355/RemindME-Library)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/kizitonwose/CalendarView/blob/master/LICENSE.md) 

**With this library, you can access library features like this.**

![Preview](https://raw.githubusercontent.com/Prashantd2355/RemindME-Sample/master/images/image-all.png)

## Methods
1. Location Based Reminder
2. Calendar Events with one click navigate to event location
3. Nearby Landmarks - find near by place
4. Contact Book - Manage contact address book with no of visits log
5. Phone Call Reminder - Remind to call on specified geographical area

## Sample project

It's very important to check out the sample app. Most techniques that you would want to implement are already implemented in the example.

Download the sample app [here](https://github.com/Prashantd2355/RemindME-Sample/releases/download/1.0/sample.apk)

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
#### Step 2

Add RemindME-library to your app `build.gradle`:

```groovy
dependencies {
	implementation 'com.github.Prashantd2355:RemindME-Library:<latest-version>'
}
```

You can find the latest version of `RemindMe-Library` on the JitPack badge above the preview images.

## Usage

#### Step 1

Initialise RemindMe library in MainActivity onCreate():

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

	// Add below line to initialise reminder library
        remindME = RemindME().getInstance(this)!!
	
...
```
#### Step 2
After initialisation of library check for permissions and request for location permission

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
	
	// Add below line to initialise reminder library
        remindME = RemindME().getInstance(this)!!
	
	// Add below line to ask for location permission from user
        PermissionCheck.requestLocationPermission(this)
	
    }
}
```
**Note: The library has all the necessory location permission so no need to add any location permission in manifest. Library will first ask for regular location permission and then ask for background permission to "Allow all the time" from application location permission settings. In order to receive location in background Background Location Permission is mendatory to allow by user**

#### Step 3
If you want to stop foreground service any time just call below function.

```kotlin

  remindME.stopLocationService(this)
  
```

#### Location Based Reminder
This function is location-based, and in order to perform operations depending on location, a number of user inputs are needed. Using the given location coordinates as a starting point, the action can be started anywhere within a 500-meter radius. The alarm would sound, for instance, if the user entered the reminder's 500-meter location task radius.

#### Step 1
Import reminder listner to receive reminder results you can check response code by [ResultCodes](#ResultCodes).

```kotlin
class MainActivity : AppCompatActivity(), ReminderListner{
    override fun onCreate(savedInstanceState: Bundle?) {
    
   ...
   
   // Reminder success listner where reminder result object returns statusCode, message, reminderList.
    override fun onReminderSuccess(reminderResult: ReminderResult) {
        Log.i("result", reminderResult.statusCode);
    }

    // Reminder error listner where user will receive error message with error code.
    override fun onReminderError(errorCode: Int, message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
```

#### Step 2
To get list of location based reminders by:

```kotlin
 ...
 
var reminders = remindME.getLocationBasedReminderList(this)

...

```

#### Step 3
To create location based reminder:

```kotlin
 ...
 
// Create location based reminder params data class object and add reminder data.
var lbrActionParams = LBRActionParams(
                        etReminderTitle.text.toString(), // Reminder Title
                        etReminderDesc.text.toString(), //Reminder Description
                        etReminderLattitude.text.toString().toDouble(), // Task location accurate lattitude
                        etReminderLongitude.text.toString().toDouble(), // Task location accurate longitude
                        chkAlert.isChecked, // pass boolean value for if want to show reminder alert
                        isSoundEnabled = chkSound.isChecked, // pass boolean value if want to play sound when reminder trigger
                        isNotification = chkNotification.isChecked // boolean value for show reminder noitification
                    )
/** call createLocationBasedReminder method
 * lbrActionParams = Reminder Action Params object
 * this = Reminder listner
 * this = Activity context
 */
remindME.createLocationBasedReminder(lbrActionParams, this, this) 
...
```

#### ResultCodes.

- **200**: Success code .

- **400**: This status code is for invalid input or validation check .

- **401**: This status code is for permission check error .

- **402**: This status code about system alert permission denied.

- **403**: Request failed.
