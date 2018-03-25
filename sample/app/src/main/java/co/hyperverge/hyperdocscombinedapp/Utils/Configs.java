package co.hyperverge.hyperdocscombinedapp.Utils;

import android.os.Environment;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by sanchit on 13/05/17.
 */

public class Configs {
    public static String FILE_DIR = Environment.getExternalStorageDirectory() + "/hyperdocs/original/";

    public static String APP_ID = "";           //assign the appId given by HyperVerge to APP_ID variable
    public static String APP_KEY = "";          //assign the appKey given by HyperVerge to APP_ID variable

    public static final String[] PASSPORT_ORDERED_FRONT_KEYS = {
            "given_name",
            "surname",
            "passport_num",
            "gender",
            "dob",
            "doi",
            "doe",
            "place_of_birth",
            "place_of_issue"
    };
    public static final String[] PASSPORT_ORDERED_BACK_KEYS = {
            "father",
            "mother",
            "spouse",
            "file_num",
            "address"
    };

    public static HashMap<String, String> PASSPORT_JSON_FIELD_KEY_TO_NAME_MAP(){
        HashMap<String, String> map = new HashMap<>();
        map.put("given_name", "Name");
        map.put("surname", "Surname");
        map.put("gender", "Gender");
        map.put("passport_num", "Passport Number");
        map.put("dob", "Date of Birth");
        map.put("doe", "Date of Expiry");
        map.put("doi", "Date of Issue");
        map.put("mother", "Mother");
        map.put("father", "Father");
        map.put("address", "Address");
        map.put("spouse", "Spouse");
        map.put("place_of_issue", "Place of Issue");
        map.put("place_of_birth", "Place of Birth");
        map.put("file_num", "File Number");
        return map;
    }

    public static final String[] AADHAAR_ORDERED_FRONT_KEYS = {
            "name",
            "phone",
            "aadhaar",
            "gender",
            "dob",
            "yob",
            //"address",
            //"pin"
    };
    public static final String[] AADHAAR_ORDERED_BACK_KEYS = {
            //"aadhaar",
            "address",
            "pin"
    };

    public static HashMap<String, String> AADHAAR_JSON_FIELD_KEY_TO_NAME_MAP(){
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Name");
        map.put("phone", "Phone No");
        map.put("gender", "Gender");
        map.put("aadhaar", "Aadhaar Number");
        map.put("dob", "Date of Birth");
        map.put("yob", "Year of Birth");
        map.put("address", "Address");
        map.put("pin", "Pin Code");
        return map;
    }

    public static final String[] PAN_ORDERED_KEYS = {
            "name",
            "father",
            "pan_no",
            "date"
    };

    public static HashMap<String, String> PAN_JSON_FIELD_KEY_TO_NAME_MAP(){
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Name");
        map.put("father", "Father");
        map.put("pan_no", "Pan Number");
        map.put("date", "Date of Birth");
        return map;
    }
}
