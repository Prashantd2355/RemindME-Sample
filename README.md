# RemindME - The Location Based Reminder

A location based reminder library for Android. The library will provide about 5 diffrent location based reminder feature ready to implement.

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/kizitonwose/CalendarView/blob/master/LICENSE.md) 

**With this library, you can access library features like this.**

![Preview](https://raw.githubusercontent.com/Prashantd2355/RemindME-Sample/master/images/image-all.png)

## Methods
1. Location Based Reminder
2. Calendar Events with one click navigate to event location
3. Nearby Landmarks - find near by place
4. Contact Book - Manage contact address book with no of visits log
5. Phone Call Reminder - Remind to call on specified geographical area

## Technologies Used
<h2>IDE </h2><br>
  <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/Android_Studio_Trademark.svg/1280px-Android_Studio_Trademark.svg.png" style="height:100px; width:300px;">

<h2>Languages Used</h2><br>
  <img src="https://cdn.iconscout.com/icon/free/png-256/xml-file-2330558-1950399.png" style="height:100px; width:100px;"><img src="https://upload.wikimedia.org/wikipedia/commons/d/d4/Kotlin_logo.svg" style="height:100px; width:150px;">
<h2>Databases:</h2><br>
  <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/SQLite370.svg/1024px-SQLite370.svg.png" style="height:100px; width:200px;">


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
	implementation 'com.github.Prashantd2355:RemindME-Library:1.0.0'
}
```

#### Step 3
Don't forget to update `AndroidManifest.xml` add reminder service and receivers :

``` xml
	// Reminder background location service for track user location in background
        <service
            android:name="com.pdhameliya.remindmelibrary.service.ReminderService"
            android:enabled="true"
            android:exported="false" />
	    
	// Reminder alert service to show alert of reminder task on top of any application
        <service
            android:name="com.pdhameliya.remindmelibrary.service.RemiderAlertService"
            android:enabled="true"
            android:exported="false" />
	    
	// Receiver for start service after device restart
        <receiver
            android:name="com.pdhameliya.remindmelibrary.service.StartReceiver"
            android:enabled="true"
            android:exported="false">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>
	
	// Receiver listen calendar events and show event alert
        <receiver
            android:name="com.pdhameliya.remindmelibrary.service.CalendarEventReceiver"
            android:enabled="true" />
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

#### Reminder Listner
Import reminder listner to receive reminder results you can check response code by [ResultCodes](#ResultCodes). Import this listner where you call create method of any reminder. This listner is for following methods:

1. Location Based Reminder
2. Calendar Events with one click navigate to event location
3. Contact Book - Manage contact address book with no of visits log
4. Phone Call Reminder - Remind to call on specified geographical area

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

#### Location Based Reminder
To set a reminder based on location, complete the steps below and call the 'createLocationBasedReminder()' function with the relevant action parameters. To use the create method of a location-based reminder, first initialise the library. Then, using the library access object, access the remining methods, which are create, update, and delete reminder. When the user enters 500 metres of the specified location's radius, the user will be alerted.

#### To get list of location based reminders:

```kotlin
 ...
 
var reminders = remindME.getLocationBasedReminderList(this)

...

```

#### To create location based reminder:

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
#### To update location based reminder:

```kotlin
 ...
 
// Pass LBRData class object with updated value.
var lbrData = LBRData(
                        etReminderTitle.text.toString(), // Updated Reminder Title
                        etReminderDesc.text.toString(), // Updated Reminder Description
                        etReminderLattitude.text.toString().toDouble(), // Updated Task location accurate lattitude
                        etReminderLongitude.text.toString().toDouble(), // Updated Task location accurate longitude
                        chkNotification.isChecked, // pass boolean value for if want to show reminder alert
                        chkAlert.isChecked, // pass boolean value if want to play sound when reminder trigger
                        chkSound.isChecked, // boolean value for show reminder noitification
                        false, // pass this default false; this is the flag for active and finished. true = finished & false = active
                        Constant.lbrData.CreatedDate, // Pass this default value which comes from library data
                        Constant.lbrData.UpdatedDate, // Pass this default value which comes from library data
                        LBRId = Constant.lbrData.LBRId // This id is mendatory to pass for update record.
                    )
/** call updateLocationBasedReminder method
 * this = Activity context
 * lbrData = Reminder list updated data object
 * this = Reminder listner
 */
remindME.updateLocationBasedReminder(this,lbrData, this) 
...
```
#### For show reminder alert:
To show reminder alert user must have to grant ACTION_MANAGE_OVERLAY_PERMISSION permission from settings. The library itself checked and send error code for that, on onReminderError need to handle error and match the error code that is 402 which is for overlay permission. You can manage like this:

```kotlin
 ...
override fun onReminderError(errorCode: Int, message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        when (errorCode) {
            403 -> {
	    // If received 403 then just call below intent to allow OVERLAY_PERMISSION.
                val intent =
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${packageName}")
                    )
                startActivityForResult(intent, Constant.CODE_DRAW_OVER_OTHER_APP_PERMISSION)
            }
        }
    }
...
```
This check is for all reminders where alert you want to add alert option for reminder.

#### To delete location based reminders:

```kotlin
 ...
 
 /** call deleteLocationBasedReminder method
 * this = Activity context
 * data = Reminder list data object which you want to delete
 */
remindME.deleteLocationBasedReminder(this,data as LBRData)

...

```


#### ResultCodes.

- **200**: Success code .

- **400**: This status code is for invalid input or validation check .

- **401**: This status code is for permission check error .

- **402**: This status code about system alert permission denied.

- **403**: Request failed.
