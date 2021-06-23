package com.thom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thom.entity.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    EditText editTextPassword;
    Button btnLogin, btnRegister;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        account = new Account();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapper();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loading = new Intent(LoginActivity.this, Loading.class);
                startActivity(loading);
                // mở form loading ra cho dui
                login(editTextPassword.getText().toString(), LoginActivity.this);
                // gọi hàm login
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dangKy = new Intent(LoginActivity.this, AddAccount.class);
                startActivity(dangKy);
            }
        });
    }

    private boolean login(String password, Context context) {
        // gọi API GET
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.LOGIN_URL + password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // khi API trả về
                        Log.d("--------------------------", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            // API trả về 1 JsonObject
                            if (jsonObject.getString("id").equals("null")) {
                                // API có id = null => đăng nhập sai
                                Toast toast = Toast.makeText(LoginActivity.this,
                                        "Password không đúng", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                account = new Account(jsonObject);
                                // chuyển JsonObject sang đối tượng account bằng contructor của Account
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("ACCOUNTID", new Long(account.getId()));
                                bundle.putString("NAME", new String(account.getName()));
                                bundle.putString("BIRTHDAY", new String(account.getBirthDay()));
                                intent.putExtras(bundle);
                                // gửi dữ liệu sang activitymain
                                startActivity(intent);
                                finish();
                            }

                        } catch (JSONException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        account.setName(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // khi API không trả về
                Toast toast = Toast.makeText(LoginActivity.this,
                        "Lỗi kết nối", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        requestQueue.add(stringRequest);
        // thêm cuộc gọi API vào Queue
        return true;
    }

    private void mapper() {
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
    }
}