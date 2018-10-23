package domain.project1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    EditText username,password;
    Button login;

    String uname = null,pass = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.loginbutton);

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
                if(pass.isEmpty()){
                    password.setError("Password cannot be empty");
                    password.requestFocus();
                    return;
                }

                signin();
            }
        });
    }

    private void signin() {

        String uri = "Hi";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Success")) {
                    //Go to next page
                    Intent intent = new Intent(Login.this, MainScreen.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Recheck Username and Password", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError{
                JSONObject body = new JSONObject();
                try {
                    body.put("Username",uname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("Password",pass);
                } catch (JSONException e) {
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
}
