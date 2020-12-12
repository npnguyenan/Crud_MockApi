package com.example.api_crud_mock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnAdd,btnSave,btnRefresh,btnDelete;
    EditText txt_firstname,txt_lastname,txt_phone,txt_email;
    RadioButton rdb_male, rdb_female;
    ListView lv_name;
    TextView txt_id;
    ArrayList<String> namelist;
    ArrayList<String> idlist;
    ArrayAdapter<String> adapter;
    String url = "https://5fd32fc08cee610016ae005a.mockapi.io/api/user/infor";
    int index;
    String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_crud_mock);

        namelist = new ArrayList<>();
        idlist = new ArrayList<>();

        btnAdd = findViewById(R.id.btn_add);
        btnSave = findViewById(R.id.btn_save);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnDelete = findViewById(R.id.btn_delete);
        txt_firstname = findViewById(R.id.txt_firstname);
        txt_lastname = findViewById(R.id.txt_lastname);
        txt_email = findViewById(R.id.txt_email);
        txt_phone = findViewById(R.id.txt_phonenum);
        rdb_male = findViewById(R.id.rdb_male);
        rdb_female = findViewById(R.id.rdb_female);
        lv_name = findViewById(R.id.lv_name);
        txt_id = findViewById(R.id.txt_id);


        getDataList(url);


        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,namelist);
        lv_name.setAdapter(adapter);

        lv_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int i, long id) {
                index = i;
                flag =idlist.get(i) ;
                fillForm(url);

            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idlist.clear();
                getDataList(url);
                adapter.notifyDataSetChanged();
                refresh();
                Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostApi(url);
                adapter.notifyDataSetChanged();
                getDataList(url);

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PutApi(url);
                getDataList(url);
                adapter.notifyDataSetChanged();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeleteApi(url);
                adapter.notifyDataSetChanged();
                getDataList(url);
            }
        });
    }
    private void refresh(){
        txt_firstname.setText("");
        txt_lastname.setText("");
        txt_email.setText("");
        txt_phone.setText("");
        txt_id.setText("id");
        rdb_male.setChecked(true);
    }
    private  void fillForm(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url +"/"+ flag, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            txt_firstname.setText(response.getString("firstname"));
                            txt_lastname.setText(response.getString("lastname"));
                            txt_email.setText(response.getString("email"));
                            txt_phone.setText(response.getString("phone"));
                            txt_id.setText(response.getString("id"));
                            if(response.getString("gender").trim().equalsIgnoreCase("male")){
                                rdb_male.setChecked(true);
                            }
                            else{
                                rdb_female.setChecked(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error by fillform", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    private void getDataList(String url){
        namelist.clear();
//        idlist.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i<response.length(); i++){
                    try {
                        JSONObject object = (JSONObject) response.get(i);
                        String firstname = object.getString("firstname").trim();
                        String lastname = object.getString("lastname");
                        String name = firstname + " " + lastname;
                        namelist.add(name);
                        idlist.add(object.getString("id"));
//                        Toast.makeText(MainActivity.this, object.getString("id").toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error by get Json Array!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void GetData(final String url){
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
//                txt_firstname.setText(response.toString());
                getDataList(url);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error make by API server!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void GetJson(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                 Request.Method.GET, url +"/"+1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String txt = response.getString("LASTNAME").toString();
//                            tvDisplay.setText(txt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error by get JsonObject...", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    private void GetArrayJson(String url){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0; i<response.length(); i++){
                    try {
                        JSONObject object = (JSONObject) response.get(i);
                        Toast.makeText(MainActivity.this, object.toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error by get Json Array!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void PostApi(String url){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                namelist.add(txt_firstname.getText().toString()+""+txt_lastname.getText().toString());
                adapter.notifyDataSetChanged();
                refresh();
                Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error by Post data!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("firstname", txt_firstname.getText().toString());
                params.put("lastname", txt_lastname.getText().toString());
                params.put("gender", rdb_male.isChecked()?"male":"female");
                params.put("email", txt_email.getText().toString());
                params.put("phone", txt_phone.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void PutApi(String url){
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url + '/' + txt_id.getText(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                adapter.notifyDataSetChanged();
                refresh();
                Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error by Post data!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("firstname", txt_firstname.getText().toString());
                params.put("lastname", txt_lastname.getText().toString());
                params.put("gender", rdb_male.isChecked()?"male":"female");
                params.put("email", txt_email.getText().toString());
                params.put("phone", txt_phone.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void DeleteApi(String url){
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE, url + '/' + txt_id.getText(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                adapter.notifyDataSetChanged();
                refresh();
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error by Post data!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
