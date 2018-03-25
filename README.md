### Overview
HyperDocs is an Android SDK of HyperVerge's Artificial Intelligence Driven KYC Digitisation (OCR) for PAN Cards, Aadhaar cards and Passports (Driving Licenses and Voting Cards - coming soon)

HyperDocs is deep-learning based mobile SDK for OCR that integrates into your existing mobile applications (banking, financial services, telecom, etc.) in order to capture the customer's KYC document (PAN Card, Aaadhar, and Passport) and extracts the details from them with greater than 99% accuracy.

### Prerequisites
- Gradle Version: 3.3 (Recommended)
- Tested with Gradle Plugin for Android Studio - version 2.3.1 
- minSdkVersion 14
- targetSdkVersion 23

#### Integration Steps
- Add the following set of lines to your `app/build.gradle`

```groovy
android {
    defaultConfig {
        renderscriptTargetApi 19
        renderscriptSupportModeEnabled true
    }
}
dependencies {
    compile('co.hyperverge:hyperdocssdk:1.0.14@aar', {
        transitive = true
    })
}
```
- Add the following set of lines to the Project (top-level) `build.gradle`

```groovy
allprojects {
    repositories {
        maven {
            url "s3://hyperdocs-sdk/android/releases"
            credentials(AwsCredentials) {
                accessKey "aws_access_key"
                secretKey "aws_secret_pass"
            }
        }
    }
}
```
- **Permissions**: The app requires the following permissions to work.
    - *Camera*
    - *Autofocus*
    - *Read & Write External Storage*
    - *Internet*
    - *Access Network State*

    Kindly note that for android v23 (Marshmallow) and above, you need to handle the runtime permissions inside your app.

- Add the following line to your Application class (the class which extends android.app.Application) for initializing our Library. This must be run only once. Check [this](https://guides.codepath.com/android/Understanding-the-Android-Application-Class) link if you are unsure of what an Application class is. Here appId and appKey are the credentials that have been supplied by us.
```java
HyperDocsSDK.init(context, appId, appKey);
```

- For clicking photo of Id Card(Passport, Pan or Aadhaar) and reading text on it using our sophisticated OCR technology, call the corresponding function based on document type from your app. This will start a camera activity where you can click front, back or front+back images of the card, which in turn will be sent to our OCR for recognition of text in different fields and you can access it by implementing the above mentioned `CameraActivity.ImageListener()` in your code.
    - **Passport**

        ```java
        CameraActivity.startForPassport(context, passportFrontImagePath, passportBackImagePath, documentSide, new CameraActivity.ImageListener() {
            @Override
            public void onOCRComplete(JSONObject result) {
                //result JSONObject will have the detected text and the other information needed. We have documented each key in the object below
                //Implement relevant logic from the info detected by our OCR here
            }
        });
        ```
    - **Aadhaar**

        ```java
        CameraActivity.startForAadhaar(context, aadhaarFrontImagePath, aadhaarBackImagePath, documentSide, new CameraActivity.ImageListener() {
            @Override
            public void onOCRComplete(JSONObject result) {
                //result JSONObject will have the detected text and the other information needed. We have documented each key in the object below
                //Implement relevant logic from the info detected by our OCR here
            }
        });
        ```
    - **Pan Card**

        ```java
        CameraActivity.startForPan(context, panImagePath, new CameraActivity.ImageListener() {
            @Override
            public void onOCRComplete(JSONObject result) {
                //result JSONObject will have the detected text and the other information needed. We have documented each key in the object below
                //Implement relevant logic from the info detected by our OCR here
            }
        });
        ```

    where
    - `documentSide` is a variable of enum type `CameraActivity.DocumentSide` and can have `CameraActivity.DocumentSide.FRONT_BACK`, `CameraActivity.DocumentSide.FRONT` and `CameraActivity.DocumentSide.BACK` as possible values
    - `passportFrontImagePath`, `passportBackImagePath`, `aadhaarFrontImagePath`, `aadhaarBackImagePath` & `panImagePath` are the path to the image where we will be storing the image clicked by user. For example, if `passportFrontImagePath` parameter in startForPassport method is set to `/storage/emulated/0/hyperdocs/1498028857114_front.jpg`, then the front image of the passport will be saved to this path.

- Keys in the result JSONObject passed as a parameter to onOCRComplete method of CameraActivity.ImageListener interface arranged by the type of document:
    - **Passport**
        - **passport_num**:    Passport Number
        - **given_name**:    Name of the Owner
        - **surname**:    Surname of the Owner
        - **spouse**:    Name of spouse of the Owner
        - **dob**:    Date of Birth of the Owner
        - **gender**:    Gender of the Owner
        - **doi**:    Date of Issue of Passport
        - **doe**:    Date of expiry of the Passport
        - **country_code**:    Country Code as mentioned in the Passport
        - **nationality**:    Nationality of the Owner
        - **type**:    Type as mentioned in the Passport
        - **file_num**:    File Number as mentioned in the Passport
        - **father**:    Father of the Owner
        - **mother**:    Mother of the Owner
        - **address**:    Address of the Owner as mentioned in the Passport
        - **front_url**:    Local Url of front side of the Passport image clicked by the SDK
        - **back_url**:    Local Url of back side of the Passport image clicked by the SDK
    - **Aadhaar**
        - **name**:    Name of the Owner
        - **phone**:    Phone Number of the Owner
        - **gender**:    Gender of the Owner
        - **aadhaar**:    Aadhaar Number
        - **dob**:    Date of Birth of the Owner
        - **yob**:    Year of Birth of the Owner
        - **address**:    Address of the Owner
        - **pin**:    Pin Code of the Owner's Address
        - **front_url**:    Local Url of front side of the Aadhaar image clicked by the SDK
        - **back_url**:    Local Url of back side of the Aadhaar image clicked by the SDK
    - **Pan**
        - **name**:    Name of the Owner
        - **father**:    Father's Name of the Owner
        - **surname**:    Surname of the Owner
        - **pan_no**:    Pan Number
        - **front_url**:    Local Url of front(only) side of the Pan Card image clicked by the SDK

- **Data Logging**: HyperDocs APIs, by default, donot save the idcard images sent for KYC OCR. To allow the servers to persist those images, following code should be added before calling any start method.
    
    ```
    CameraActivity.toggleDataLogging(true);
    ```
    
    Please note that this is an optional feature and, by default, it is disabled. If enabled, the saved images can help us debug the failure scenerios better and the saved images will also be feeded back to the Deep Learning OCR system so as to increase the accuracy of the system.

- **Customizations**: Some text fields, element colors, font styles, and button icons are customizable so that the look and feel of the components inside SDK can be altered to match the look and field of the app using the SDK. `Kindly note that this step is optional`. Below is the list of items that are customizable grouped by the resource file/type where the customized value/file(s) should be placed in.
    - **strings.xml**:

        ```xml
            <string name="sdk_title_text">HYPERDOCS Override</string>
            <string name="instructions_text_passport">Place Passport page inside the box Override</string>
            <string name="instructions_text_pan">Place Pan Card inside the box Override</string>
            <string name="instructions_text_aadhaar">Place Aadhaar Card page inside the box Override</string>
            <string name="preview_header_text_front_pan">FRONT PAGE REVIEW PAN Override</string>
            <string name="preview_header_text_front_passport">FRONT PAGE REVIEW PASSPORT Override</string>
            <string name="preview_header_text_back_passport">BACK PAGE REVIEW PASSPORT Override</string>
            <string name="preview_header_text_front_aadhaar">FRONT PAGE REVIEW AADHAAR Override</string>
            <string name="preview_header_text_back_aadhaar">BACK PAGE REVIEW AADHAAR Override</string>
            <string name="step_front_text_pan">Front Page of PAN Override</string>
            <string name="step_front_text_passport">Front Page of Passport Override</string>
            <string name="step_back_text_passport">Back Page of Passport Override</string>
            <string name="step_front_text_aadhaar">Front Page of Aadhaar Override</string>
            <string name="step_back_text_aadhaar">Back Page of Aadhaar Override</string>
        ```

    - **colors.xml**:

        ```xml
            <color name="camera_button_color">#273646</color>
            <color name="top_bar_color">#FFFFFF</color>
            <color name="sdk_title_text_color">#9b9b9b</color>
        ```

    - **dimens.xml**:

        ```xml
            <dimen name="preview_title_text_font_size">16sp</dimen>
            <dimen name="camera_title_text_font_size">12sp</dimen>
        ```

    - **Drawable PNGs**:
        - **ic_camera_torch_off**: Max dimension size should be less than 35dp
        - **ic_camera_torch_on**: Max dimension size should be less than 35dp
        - **ic_camera_cross**: Max dimension size should be less than 35dp

    - **styles.xml**:
        - **TextStyleHeading**: Style value for the heading text. Can be used to set font style by using it in following way:
            
            ```xml    
                <style name="TextStyleHeading">
                    <item name="fontPath">fonts/Roboto_regular.ttf</item>
                </style>
            ```
            where fontPath is the path to font `ttf` or `otf` file relative to assets folder in the app.

### Sample Code
The sample code is present in the **sample** directory of this repo. In order to use the sample app, kindly add the `appId` and `appKey` given by HyperVerge to `APP_ID` and `APP_KEY` respectively in `sample/app/src/main/java/co/hyperverge/hyperdocscombinedapp/Utils/Configs.java`.

### Contact Us
If you are interested in integrating this SDK, please do send us a mail at [contact@hyperverge.co](mailto:contact@hyperverge.co) explaining your use case. We will give you the `aws_access_key`, `aws_secret_pass`, `appId` & `appKey`, so that you can try it out.
Meanwhile, the demo app can be downloaded from Play Store by clicking [here](https://play.google.com/store/apps/details?id=co.hyperverge.hyperdocscombinedapp).
