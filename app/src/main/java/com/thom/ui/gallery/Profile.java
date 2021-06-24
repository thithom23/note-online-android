package com.thom.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thom.Constant;
import com.thom.EditActivity;
import com.thom.MainActivity;
import com.thom.R;
import com.thom.entity.Account;
import com.thom.entity.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Profile extends Fragment {
    Button capNhat;
    EditText hoTen, ngaySinh;
    private ProFileViewModel proFileViewModel;
    String name;
    Long id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        proFileViewModel =
                new ViewModelProvider(this).get(ProFileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        id = mainActivity.getId();
        name = mainActivity.getName();
        mapper(root);
        // theo trình tự nếu lộn xộn có thể không chạy
        receiveProfile();


        final TextView textView = root.findViewById(R.id.text_gallery);
        proFileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        capNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(mainActivity);
            }
        });
        return root;

    }

    private void receiveProfile() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constant.ACCOUNT_GET_INFO_URL + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Account account = new Account(jsonObject);
                            // hiển thị nó lên trên editText
                            ngaySinh.setText(account.getBirthDay());
                            hoTen.setText(account.getName());
                        } catch (JSONException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getActivity(),
                        "Lỗi kết nối", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void updateProfile(MainActivity mainActivity) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, Constant.ACCOUNT_UPDATE_INFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mainActivity.setName(hoTen.getText().toString());
                        Toast toast = Toast.makeText(getContext(),
                                "Cập nhật thành công", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getContext(),
                        "Lỗi kết nối", Toast.LENGTH_LONG);
                toast.show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    //jsonBody.put("accountId", accountId);
                    jsonBody.put("birthDay", ngaySinh.getText());
                    jsonBody.put("id", id);
                    jsonBody.put("name", hoTen.getText());
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
                // tham số đường dẫn gọi URL
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void mapper(View root) {
        capNhat = (Button) root.findViewById(R.id.capNhat);
        hoTen = (EditText) root.findViewById(R.id.hoTen);
        ngaySinh = (EditText) root.findViewById(R.id.ngaySinh);
    }
}
