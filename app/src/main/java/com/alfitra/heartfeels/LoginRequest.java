package com.alfitra.heartfeels;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Ramona on 18/05/2016.
 */
public class LoginRequest extends StringRequest{

    private static final String LOGIN_REQUEST_URL = "http://api.heartfeels.com/users/sessions.json";
    private Map<String, String> params;

    public LoginRequest(String email, String password, Response.Listener<String> listener){

        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError){

                try{
                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);

                }catch (JSONException e){

                }catch (UnsupportedEncodingException e){

                }
            }
        };
    }

    @Override
    public Map<String,String> getParams(){return params;}
}
