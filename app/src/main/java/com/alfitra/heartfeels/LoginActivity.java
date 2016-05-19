package com.alfitra.heartfeels;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.alfitra.musicplayer.ch.vanilla.LibraryActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText userEmail = (EditText) findViewById(R.id.userEmail);
        final EditText userPassword = (EditText) findViewById(R.id.userPassword);
        final Button loginButton = (Button) findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                final Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success == true ) {


                                //get the user id for next api used and save it
                                int user_id = jsonResponse.getInt("id");

                                SharedPreferences sp = getSharedPreferences("userPreferences", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("user_id", user_id);
                                editor.commit();




                                Intent intent = new Intent(LoginActivity.this, LibraryActivity.class);
                                LoginActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                };

                    LoginRequest loginRequest = new LoginRequest(email,password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue((LoginActivity.this));
                    queue.add(loginRequest);



            }
        }

        );

    }

    }

