package com.alfitra.heartfeels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Ramona on 4/19/2016.
 */
public class RegisterActivity extends AppCompatActivity {

    private final static String REGISTER_API_ENDPOINT_URL = "http://api.heartfeels.com/api/v1/auth/sign_up";
    private SharedPreferences mPreferences;
    private String mUserEmail;
   private String mUserFirstName;
    private String mUserLastName;
    private String mUserPassword;
    private String mGender;
    private String mUserPasswordConfirmation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
    }

    public void registerNewAccount(View button) {
        EditText userEmailField = (EditText) findViewById(R.id.userEmail);
        mUserEmail = userEmailField.getText().toString();
        EditText userFirstNameField = (EditText) findViewById(R.id.userFirstName);
        mUserFirstName = userFirstNameField.getText().toString();
        EditText userLastNameField = (EditText) findViewById(R.id.userLastName);
        mUserLastName = userLastNameField.getText().toString();
        EditText userPasswordField = (EditText) findViewById(R.id.userPassword);
        mUserPassword = userPasswordField.getText().toString();
          EditText userPasswordConfirmationField = (EditText) findViewById(R.id.userPasswordConfirmation);
           mUserPasswordConfirmation = userPasswordConfirmationField.getText().toString();

        mGender=(((RadioGroup)findViewById(R.id.genderGroup)).getCheckedRadioButtonId()==R.id.genderMale)?"male":"female";

        /*| mUserFirstName.length() == 0 || mUserLastName.length() == 0 |*/

        if (mUserEmail.length() == 0 || mUserFirstName.length() == 0 || mUserLastName.length() == 0 || mUserPassword.length() == 0 || mUserPasswordConfirmation.length() == 0) {
            // input fields are empty
            Toast.makeText(this, "Please complete all the fields",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            if (!mUserPassword.equals(mUserPasswordConfirmation)) {
                // password doesn't match confirmation
                Toast.makeText(this, "Your password doesn't match confirmation, check again",
                        Toast.LENGTH_LONG).show();
                return;
            } else {
                // everything is ok!
                RegisterTask registerTask = new RegisterTask(RegisterActivity.this);
                registerTask.setMessageLoading("Registering new account...");
                registerTask.execute(REGISTER_API_ENDPOINT_URL);
            }
        }

    }


    private class RegisterTask extends UrlJsonAsyncTask {
        public RegisterTask(Context context) {
            super(context);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {


            OkHttpClient httpClient=new OkHttpClient();

            JSONObject holder = new JSONObject();
            JSONObject userObj = new JSONObject();
            String response = null;
            JSONObject json = new JSONObject();

            try {
                try {
                    // setup the returned values in case
                    // something goes wrong
                    json.put("success", false);
                    json.put("info", "Something went wrong. Retry!");

                    // add the users's info to the post params
                    userObj.put("email", mUserEmail);
                    userObj.put("first_name", mUserFirstName);
                    userObj.put("last_name", mUserLastName);
                    userObj.put("password", mUserPassword);
                    userObj.put("password_confirmation", mUserPasswordConfirmation);
                    userObj.put("gender",mGender);
                    holder.put("user", userObj);


                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),holder.toString());
                    Request postReq=new Request.Builder().url(urls[0]).post(requestBody).build();

                    Response objRes = httpClient.newCall(postReq).execute();

                    Log.e(""+objRes.isSuccessful(),objRes.toString());

                    if(objRes.isSuccessful()){
                        response=objRes.body().string();
                        json = new JSONObject(response);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IO", "" + e);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON", "" + e);
            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {

                Log.e("result",json.toString());

                if (json.getBoolean("success")) {
                    // everything is ok
                    SharedPreferences.Editor editor = mPreferences.edit();
                    // save the returned auth_token into
                    // the SharedPreferences
                    editor.putString("AuthToken", json.getJSONObject("data").getString("auth_token"));
                    editor.commit();

                    // launch the HomeActivity and close this one
                   Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                Toast.makeText(context, json.getString("info"), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                // something went wrong: show a Toast
                // with the exception message
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                super.onPostExecute(json);
            }
        }
    }

}
