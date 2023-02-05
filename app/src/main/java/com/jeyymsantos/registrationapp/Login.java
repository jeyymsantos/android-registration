package com.jeyymsantos.registrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextView tvRegisterNow;
    TextInputEditText etEmail, etPassword;
    Button btnLogin;
    TextView tvError;
    ProgressBar progressBar;
    String name, email, password, apiKey;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        tvRegisterNow = findViewById(R.id.registerNow);
        btnLogin = findViewById(R.id.submit);
        tvError = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);

        sharedPreferences = getSharedPreferences("MyAppName", MODE_PRIVATE);

        if(sharedPreferences.getString("logged", "false").equals("true")){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }

        tvRegisterNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Registration.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.GONE);

                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="https://jeyym-tech.preview-domain.com/login.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");

                                    if(status.equals("success")){
                                        name = jsonObject.getString("name");
                                        email = jsonObject.getString("email");
                                        apiKey = jsonObject.getString("apiKey");

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("logged", "true");
                                        editor.putString("name", name);
                                        editor.putString("email", email);
                                        editor.putString("apiKey", apiKey);
                                        editor.apply();

                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(i);
                                        finish();

                                    }else{
                                        tvError.setText(message);
                                        tvError.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    tvError.setText(e.getMessage());
                                    tvError.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvError.setText(error.getLocalizedMessage());
                        tvError.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", email);
                        paramV.put("password", password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}