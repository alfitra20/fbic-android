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
public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://api.heartfeels.com/users/registrations.json";
    private Map<String, String> params;

    public RegisterRequest(String email, String first_name, String last_name, String password, String password_confirmation, String gender, Response.Listener<String> listener) {

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("password_confirmation", password_confirmation);
        params.put("first_name", first_name);
        params.put("last_name", last_name);
        params.put("gender", gender);


        new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                try {
                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);

                } catch (JSONException e) {

                } catch (UnsupportedEncodingException e) {

                }
            }
        };
    }

    @Override
    public Map<String, String> getParams(){return params;}
}
