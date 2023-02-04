package com.jeyymsantos.registrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    TextInputEditText etName, etEmail, etPassword;
    Button btnSubmit;
    String name, email, password;
    TextView tvError;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etName = findViewById(R.id.name);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btnSubmit = findViewById(R.id.submit);
        tvError = findViewById(R.id.error);
        progressBar = findViewById(R.id.loading);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                tvError.setVisibility(View.GONE);

                name = etName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://192.168.68.101/CTAPDEVL/registration-activity/register.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                if(response.equals("success")){
                                    Toast.makeText(Registration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), Login.class);
                                    startActivity(i);
                                    finish();
                                }else {
                                    tvError.setText(response);
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
                        paramV.put("name", name);
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