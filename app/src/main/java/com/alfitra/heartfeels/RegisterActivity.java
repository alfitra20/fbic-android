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
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ramona on 4/19/2016.
 */
public class RegisterActivity extends AppCompatActivity {

   @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_register);

       final EditText userEmail = (EditText) findViewById(R.id.userEmail);
       final EditText userFirstName = (EditText) findViewById(R.id.userFirstName);
       final EditText userLastName = (EditText) findViewById(R.id.userLastName);
       final EditText userPassword = (EditText) findViewById(R.id.userPassword);
       final EditText userPasswordConfirmation = (EditText) findViewById(R.id.userPasswordCofirmation);
       //final String Gender = (((RadioGroup)findViewById(R.id.genderGroup)).getCheckedRadioButtonId()==R.id.genderMale)?"male":"female";
       final Button registerButton = (Button) findViewById(R.id.registerButton);

       registerButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v){
               final String email = userEmail.getText().toString();
               final String first_name = userFirstName.getText().toString();
               final String last_name = userLastName.getText().toString();
               final String password = userPassword.getText().toString();
               final String password_confirmation = userPasswordConfirmation.getText().toString();
               final String gender = (((RadioGroup)findViewById(R.id.genderGroup)).getCheckedRadioButtonId()==R.id.genderMale)?"male":"female";

               Response.Listener<String> responseListener = new Response.Listener<String>(){
               @Override
                   public void onResponse(String response){
                   try{
                       JSONObject jsonResponse = new JSONObject(response);
                       boolean success = jsonResponse.getBoolean("success");
                       if(success == true){
                           Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                           RegisterActivity.this.startActivity(intent);
                       }else{
                           AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                           builder.setMessage("Register Failed")
                                   .setNegativeButton("Retry", null)
                                   .create()
                                   .show();
                       }
                   }catch (JSONException e){
                       e.printStackTrace();
                   }
               }
               };
               RegisterRequest registerRequest = new RegisterRequest(email, first_name,last_name,password,password_confirmation,gender, responseListener);
               RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
               queue.add(registerRequest);


           }
       });

   }
}
