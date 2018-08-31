package com.example.rahul_jareda.karmyatra;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.example.rahul_jareda.karmyatra.MainActivity.movementDatabase;

public class insertDbOrServer{
    public static void insertDbOrServer_method(final Movements movement, final Boolean insertDbRequired, Context context){
        final String url = "https://karmyatra.000webhostapp.com/karmyatra_php_files/insert.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(MainActivity.this, "Success " + response, Toast.LENGTH_SHORT).show();
                //Log.i("Success", "" + response);
                //Sometimes connetion is established successfully but data is not inserted, post data to database in that case.
                final String response_trimmed = response.trim();
                final String response_expected = "data inserted";
                if (!response_trimmed.contentEquals(response_expected) && insertDbRequired == true){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            movementDatabase.daoAccess().insert(movement);
                        }
                    }).start();
                    //Log.i("data insertion", ": inserted to database");
                }
                if(response_trimmed.contentEquals(response_expected) && insertDbRequired == false){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            movementDatabase.daoAccess().delete(movement);
                        }
                    }).start();
                  //  Log.i("data insertion", ": inserted to server & deleted");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.i("Error", "" + error);
                final String error_trimmed = String.valueOf(error).trim();
                final String response_expected = "data inserted";
                if (!error_trimmed.contentEquals(response_expected) && insertDbRequired == true){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            movementDatabase.daoAccess().insert(movement);
                        }
                    }).start();
                    //Log.i("data insertion", ": inserted to database");
                }
                if(error_trimmed.contentEquals(response_expected) && insertDbRequired == false){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            movementDatabase.daoAccess().delete(movement);
                        }
                    }).start();
                    //Log.i("data insertion", ": inserted to server & deleted");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("mobile", movement.getMobile());
                map.put("latitude", movement.getLatitude());
                map.put("longitude", movement.getLongitude());
                map.put("time", movement.getTime());

                return map;
            }
        };
        //Log.i("String Request", ":" + request);

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}