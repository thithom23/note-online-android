package com.thom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.thom.entity.Account;
import com.thom.ui.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public String name, birthDay;
    private Long id;
    NavigationView navigationView;
    TextView txtName,txtBirthDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        {
            Intent intent = getIntent();
            if (intent != null) {
                Bundle bundle = intent.getExtras();

                if (bundle != null) {
                    Bundle bundle1 = new Bundle();
                    id = bundle.getLong("ACCOUNTID");
                    name = bundle.getString("NAME");
                    HomeFragment fragobj = new HomeFragment();
                    fragobj.setArguments(bundle1);

                }
            }
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

            View headerView = navigationView.getHeaderView(0);
            txtName = (TextView) headerView.findViewById(R.id.accountName);
            txtBirthDay = (TextView) headerView.findViewById(R.id.birthDay);
            getDetailAccount();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery,  R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("ACCOUNTID",new Long(id));
                bundle.putLong("NOTEID", new Long(0));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private void getDetailAccount() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.ACCOUNT_GET_INFO_URL + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("--------------------------", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Account account = new Account(jsonObject);
                            if (account.getId()!=null) {
                                txtName.setText(account.getName());
                                txtBirthDay.setText(account.getBirthDay());
                            }

                        } catch (JSONException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(MainActivity.this,
                        "Lỗi kết nối", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.info:
                Intent openCreateReminderActivity = new Intent(getApplicationContext(), InfoActivity.class);
                startActivity(openCreateReminderActivity);
                return true;
            case R.id.logout:
                Intent openAboutActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(openAboutActivity);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}