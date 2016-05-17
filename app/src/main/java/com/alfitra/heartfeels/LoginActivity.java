package com.alfitra.heartfeels;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;




public class LoginActivity extends AppCompatActivity {

        private final static String LOGIN_API_ENDPOINT_URL ="http://api.heartfeels.com/api/v1/auth/sign_in";
        private SharedPreferences mPreferences;
        private String mUserEmail;
        private String mUserPassword;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

        private class LoginTask extends UrlJsonAsyncTask  {
            public LoginTask(Context context) {
                super(context);
            }

            @Override
            protected JSONObject doInBackground(String... urls) {
                OkHttpClient client = new OkHttpClient();



              //  HttpPost post = new HttpPost(urls[0]);
                JSONObject holder = new JSONObject();
                JSONObject userObj = new JSONObject();
                String response = null;
                JSONObject json = new JSONObject();

                try {

                        // setup the returned values in case
                        // something goes wrong
                        json.put("success", false);
                        json.put("info", "Something went wrong. Retry!");
                        // add the user email and password to
                        // the params
                        //userObj.put("email", mUserEmail);
                        //userObj.put("password", mUserPassword);
                      //  holder.put("user", userObj);

                        FormBody reqBody=new FormBody.Builder()
                        .add("email",mUserEmail).add("password", mUserPassword).build();

                       //RequestBody reqBody=RequestBody.create(JSON,userObj.toString());
                        Request postReq=new Request.Builder().url(urls[0])
                        .method("POST",reqBody).build();

                        Response res=client.newCall(postReq).execute();

                         Log.e("Response", "" + res.toString());
                        if(res.isSuccessful()) {
                            response = res.body().string();
                            Log.e("Response", "" + response);
                            return new JSONObject(response);
                        }else return json;

                    /*    StringEntity se = new StringEntity(holder.toString());
                        post.setEntity(se);

                        // setup the request headers
                        post.setHeader("Accept", "application/json");
                        post.setHeader("Content-Type", "application/json");

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        response = client.execute(post, responseHandler);
                        json = new JSONObject(response);*/

                    }  catch (IOException e) {
                        e.printStackTrace();
                        Log.e("IO", "" + e);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                return json;
            }

            @Override
            protected void onPostExecute(JSONObject json) {
                try {
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



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        }



        public void login(View button) {
            EditText userEmailField = (EditText) findViewById(R.id.userEmail);
            mUserEmail = userEmailField.getText().toString();
            EditText userPasswordField = (EditText) findViewById(R.id.userPassword);
            mUserPassword = userPasswordField.getText().toString();

            if (mUserEmail.length() == 0 || mUserPassword.length() == 0) {
                // input fields are empty
                Toast.makeText(this, "Please complete all the fields",
                        Toast.LENGTH_LONG).show();
                return;

            }

            else {

                LoginTask loginTask = new LoginTask(LoginActivity.this);
                loginTask.setMessageLoading("Logging in...");
                loginTask.execute(LOGIN_API_ENDPOINT_URL);
            }
        }


    }

