package domain.project1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    EditText username,password;
    Button login;
    ProgressBar bar;
    Context context;
    ImageView urlSetter;
    String uname = null,pass = null;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    boolean autologin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginbutton);
        bar = findViewById(R.id.bar);
        urlSetter = findViewById(R.id.img);
        urlSetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlSetterView();
            }
        });
        context = this;
        preferences = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
        autologin = preferences.getBoolean("autologin",false);
        if(autologin){
            LoadControlActivity();
        }
        else{
            bar.setVisibility(View.INVISIBLE);
            username.setVisibility(View.VISIBLE);
            password.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
        }

        editor = preferences.edit();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = username.getText().toString();
                pass = password.getText().toString();
                if(uname.isEmpty()){
                    username.setError("Username cannot be empty");
                    username.requestFocus();
                    return;
                }
                else if(pass.isEmpty()){
                    password.setError("Password cannot be empty");
                    password.requestFocus();
                    return;
                }
                else
                signin();
            }
        });
    }

    private void signin() {

        String url = new getUrl().setUrl(context,"login");
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    boolean success = object.getBoolean("success");
                    if(success){
                        JSONObject details = object.getJSONObject("details");
                        editor.putString("admission_no",uname.toUpperCase());
                        editor.putString("name",details.getString("name"));
                        editor.putString("course",details.getString("course"));
                        editor.putString("department",details.getString("department"));
                        editor.putString("year-of-joining",details.getString("year-of-joining"));
                        editor.putString("Date-of-birth",details.getString("Date-of-birth"));
                        editor.putBoolean("autologin",true);
                        editor.commit();
                        LoadControlActivity();
                    }
                    else{
                        String message = object.getString("reason");
                        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError{
                JSONObject body = new JSONObject();
                try {
                    body.put("admission_number", uname.toUpperCase());
                    body.put("password", pass);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return body.toString().getBytes();
            }

            @Override
            public String getBodyContentType (){
                return "application/json";
            }
        };

        queue.add(stringRequest);
    }
    void LoadControlActivity(){
        Intent intent = new Intent(Login.this, controlActivity.class);
        startActivity(intent);
        finish();
    }
    void urlSetterView(){
        View view = View.inflate(this,R.layout.url_view,null);
        TextView txt = view.findViewById(R.id.head);
        final EditText url_input = view.findViewById(R.id.url_input);
        Button set = view.findViewById(R.id.button2);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        String url = preferences.getString("currentLocalhost","null");
        if(!url.equals("null")){
            url_input.setText(url);
        }
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = url_input.getText().toString();
                if(ip.equals("")){
                    Toast.makeText(context,"Input an IP Address(open cmd and type : ifconfig/ipconfig",Toast.LENGTH_SHORT).show();
                }
                else{
                    editor.putString("currentLocalhost",ip);
                    editor.commit();
                    dialog.cancel();
                }
            }
        });
        dialog.show();

    }
}
