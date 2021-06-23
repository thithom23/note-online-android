package com.thom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AddAccount extends AppCompatActivity {
    EditText password1, password2, hoTen, birthDay;
    Button dangKy, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        mapper();
        dangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!password1.getText().toString().equals(password2.getText().toString()))
                {
                    Toast toast = Toast.makeText(AddAccount.this,
                            "Password không đúng", Toast.LENGTH_LONG);
                    toast.show();
                }
                else
                {
                    RequestQueue requestQueue = Volley.newRequestQueue(AddAccount.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ACCOUNT_CREATE_ACCOUNT,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                        finish();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast toast = Toast.makeText(AddAccount.this,
                                    "Lỗi kết nối", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }) {
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                JSONObject jsonBody = new JSONObject();
                                jsonBody.put("birthDay", birthDay.getText());
                                jsonBody.put("name", hoTen.getText());
                                jsonBody.put("password", password1.getText());
                                String requestBody = jsonBody.toString();
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException | JSONException uee) {
                                Log.d("log==============", "error");
                                return null;
                            }
                        }

                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("Content-Type", "application/json; charset=utf-8");
                            return params;
                        }
                    };

                    requestQueue.add(stringRequest);
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void mapper()
    {
        dangKy = (Button) findViewById(R.id.btnDangKy);
        password1 = (EditText) findViewById(R.id.password1);
        password2 = (EditText) findViewById(R.id.password2);
        hoTen = (EditText) findViewById(R.id.hoTen);
        birthDay = (EditText) findViewById(R.id.birthDay);
        btnBack = (Button) findViewById(R.id.btnBack);
    }
}