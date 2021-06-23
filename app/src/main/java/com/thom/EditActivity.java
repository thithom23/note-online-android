package com.thom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.thom.ui.home.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {
    EditText editNote;
    Button btnXoa, btnThem, btnBack;
    boolean isEdit = false;
    Long accountId, noteId = (long) 0;
    String name, birthDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mapper();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Bundle bundle1 = new Bundle();
                accountId = bundle.getLong("ACCOUNTID");
                noteId = bundle.getLong("NOTEID");
                HomeFragment fragobj = new HomeFragment();
                fragobj.setArguments(bundle1);
                if (noteId != 0) {
                    btnThem.setText("Sửa");
                    editNote.setText(bundle.getString("NOTE"));
                }
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNote.setText("");
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent loading = new Intent(EditActivity.this, Loading.class);
                startActivity(loading);
                if(noteId==0) {
                    createNote();
                }
                else
                    updateNote();
            }
        });
    }
    private void updateNote()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(EditActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, Constant.NOTE_URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Intent intent = new Intent(EditActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putLong("ACCOUNTID", new Long(accountId));
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(EditActivity.this,
                        "Lỗi kết nối", Toast.LENGTH_LONG);
                toast.show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    //jsonBody.put("accountId", accountId);
                    jsonBody.put("noteId", noteId);
                    jsonBody.put("note", editNote.getText().toString());
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

    private void createNote() {
        RequestQueue requestQueue = Volley.newRequestQueue(EditActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.NOTE_URL_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Intent intent = new Intent(EditActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putLong("ACCOUNTID", new Long(accountId));
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(EditActivity.this,
                        "Lỗi kết nối", Toast.LENGTH_LONG);
                toast.show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("accountId", accountId);
                    jsonBody.put("note", editNote.getText().toString());
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

    public void mapper() {
        editNote = (EditText) findViewById(R.id.editNote);
        btnXoa = (Button) findViewById(R.id.btnXoa);
        btnThem = (Button) findViewById(R.id.btnThem);
        btnBack = (Button) findViewById(R.id.btnBack);

    }
}