package com.example.rahul_jareda.karmyatra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText editTextMobile, editTextFirstName, editTextLastName, editTextPassword;
    //RadioGroup radioGroupGender;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressBar = findViewById(R.id.progressBar);

        //if the user is already logged in we will directly start the profile activity
       if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        editTextMobile = findViewById(R.id.editTextMobile);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    public void registerUser(View view) {
        final String mobile = editTextMobile.getText().toString().trim();
        final String firstname = editTextFirstName.getText().toString().trim();
        final String lastname = editTextLastName.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();


        //first we will do the validations

        if (TextUtils.isEmpty(mobile)) {
            editTextMobile.setError("Please enter mobile number");
            editTextMobile.requestFocus();
            return;
        }

        if(mobile.length() != 10){
            editTextMobile.setError("Please enter 10 digit mobile number");
            editTextMobile.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(firstname)) {
            editTextFirstName.setError("Please enter your email");
            editTextLastName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (!obj.getBoolean("error")) {
                               Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                               Log.i("Error_server", "" + obj.getString("message"));

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");

                                //creating a new user object
                                User user = new User(
                                        userJson.getInt("id"),
                                        userJson.getString("mobile"),
                                        userJson.getString("firstname"),
                                        userJson.getString("lastname")
                                );

                                //storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                                //starting the profile activity
                                finish();
                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                Log.i("Error_json", "" + obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.i("Error_volley", "" + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mobile);
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void goToLogin(View view) {
        //if user pressed on login
        //we will open the login screen
        finish();
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }

    @Override
    public void onBackPressed(){
        this.moveTaskToBack(true);
    }

}