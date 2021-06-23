package com.thom.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.thom.Constant;
import com.thom.EditActivity;
import com.thom.LoginActivity;
import com.thom.MainActivity;
import com.thom.R;
import com.thom.entity.Account;
import com.thom.entity.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ListView listView;
    private boolean flag = false;
    ArrayAdapter<Note> adapter;
    ArrayList<Note> listNote;
    String name;
    Long id;
    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // name = getArguments().getString("NAME");

        MainActivity mainActivity = (MainActivity) getActivity();
        id = mainActivity.getId();
        name = mainActivity.getName();

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        listNote = new ArrayList<>();
        mapper(root);
        receiveNote();
        registerForContextMenu(listView);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        adapter = new ArrayAdapter<Note>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, listNote);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("ACCOUNTID", new Long(id));
                bundle.putLong("NOTEID", new Long(listNote.get((int) ids).getId()));
                bundle.putString("NOTE", new String(listNote.get((int) ids).getNote()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete: {
                flag = false;
                deleteNote(listNote.get((int) info.position).getId());
                if(!flag)
                {
                    listNote.remove(info.position);
                    adapter.notifyDataSetChanged();
                }

                break;
            }
            case R.id.edit: {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("ACCOUNTID", new Long(id));
                bundle.putLong("NOTEID", new Long(listNote.get((int) info.position).getId()));
                bundle.putString("NOTE", new String(listNote.get((int) info.position).getNote()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void mapper(View root) {
        textView = root.findViewById(R.id.text_home);
        listView = (ListView) root.findViewById(R.id.listNote);
    }

    private void deleteNote(Long noteIdDelete) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, Constant.NOTE_URL_DELETE + noteIdDelete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        flag=false;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                flag = true;
                Toast toast = Toast.makeText(getActivity(),
                        "Lỗi kết nối", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void receiveNote() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.NOTE_URL_GET_ALL + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("temporaryName", jsonArray.get(i));
                                jsonObject = jsonObject.getJSONObject("temporaryName");
                                Note note = new Note(jsonObject);
                                listNote.add(note);
                                adapter.notifyDataSetChanged();
                            }

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


}