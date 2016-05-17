package com.alfitra.heartfeels;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Ramona on 16/05/2016.
 */
public class IntentUtil {

    private Activity activity;

    // constructor
    public IntentUtil(Activity activity) {
        this.activity = activity;
    }

    public void showAccessToken() {
        Intent i = new Intent(activity, ShowAccessTokenActivity.class);
        activity.startActivity(i);
    }
}




