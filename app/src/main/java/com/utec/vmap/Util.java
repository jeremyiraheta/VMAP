package com.utec.vmap;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class Util {
    public static void Save(Activity activity, String name, String value)
    {
        SharedPreferences pref = activity.getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(name,value);
        edit.commit();
    }
    public static String Load(Activity activity, String name)
    {
        SharedPreferences pref = activity.getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);
        return pref.getString(name,"");
    }
    public  static HashMap<String, String> getAllValues(Activity activity, String[] vals)
    {
        HashMap<String,String> ret = new HashMap<>();
        SharedPreferences pref = activity.getSharedPreferences(activity.getPackageName(), MODE_PRIVATE);
        for (String v:
             vals) {
            ret.put(v,pref.getString(v,""));
        }
        return ret;
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
