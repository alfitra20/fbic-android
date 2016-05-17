package com.alfitra.heartfeels;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ramona on 16/05/2016.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate(){
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        printKeyHash();


    }

    public void printKeyHash(){

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.alfitra.heartfeels", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("SHA :", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
            } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        }
    }

