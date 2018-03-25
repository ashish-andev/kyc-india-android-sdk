package co.hyperverge.hyperdocscombinedapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import co.hyperverge.hyperdocscombinedapp.HyperDocsCombinedApp;

/**
 * Created by adarsh on 18/03/17.
 */
public class SpUtils {
    public static final String KEY_ORIGINAL = "keyOriginal";
    public static final String KEY_COMPRESSED = "keyCompressed";
    public static final String KEY_LIST = "keyList1";

    private static SharedPreferences sp = null;
    public static final String spInstant = "InstantSP";
    private static SharedPreferences.Editor editor = null;
    private static Boolean profileLikeSyncVariable = true;
    private static Boolean eventReceivedStreamsSyncVariable = true;

    private static SharedPreferences.Editor getEditor() {
        if (editor == null) {
            editor = getSP().edit();
        }
        return editor;
    }

    public static void init(Context context) {
        if (context != null) {
            sp = context.getSharedPreferences(spInstant, Context.MODE_PRIVATE);
            editor = sp.edit();
        }

    }

    private static SharedPreferences getSP() {
        if (sp == null) {
            init(HyperDocsCombinedApp.getInstance());
        }
        return sp;
    }

    public static void saveLists(List<String> fullList) {
        JSONArray savedList = new JSONArray();

        for(int i = 0; i < fullList.size(); i++) {
            savedList.put(fullList.get(i));
        }
        getEditor().putString(KEY_LIST, savedList.toString());
        getEditor().commit();
    }

    public static List<String> getLists() {
        String savedString = getSP().getString(KEY_LIST, "[]");
        List<String> savedArray = new ArrayList<>();

        try {
            JSONArray savedList = new JSONArray(savedString);
            for(int i = 0; i < savedList.length(); i++)
            {
                savedArray.add(savedList.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return savedArray;
    }


}