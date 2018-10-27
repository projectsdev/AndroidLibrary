package domain.project1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Booking extends AppCompatActivity {

    Spinner course,dept,sem,sub;
    Button find;
    Context context;
    String courses[],depts[];
    int inc = 0;
    String flagCourse,flagDept;
    ArrayList<String> semesters = new ArrayList<>();
    HashMap<String,HashMap<String,ArrayList<String>>> map = new HashMap<>();
    HashMap<String,ArrayList<String>> subvalues = new HashMap<>();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_booking);
        course = findViewById(R.id.courseSpinner);
        dept = findViewById(R.id.departmentSpinner);
        sem = findViewById(R.id.semesterSpinner);
        find = findViewById(R.id.find);
        getCourses();
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context,BooksBooking.class);
                intent.putExtra("course",(String)course.getSelectedItem());
                intent.putExtra("dept",(String)dept.getSelectedItem());
                intent.putExtra("semester",(String)sem.getSelectedItem());
                startActivity(intent);
            }
        });
    }
    void getCourses(){

        String url = new getUrl().setUrl(context,"getCourses");
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject first = new JSONObject(response);
                            int fetch = first.getInt("fetch");
                            if(fetch == 1) {
                                JSONObject second = first.getJSONObject("snapshot");
                                Iterator<?> iterator1 = second.keys();
                                while (iterator1.hasNext()) {
                                    String c = (String) iterator1.next();
                                    JSONObject third = first.getJSONObject("snapshot").getJSONObject(c);
                                    Iterator<?> iterator2 = third.keys();
                                    subvalues = new HashMap<>();
                                    while (iterator2.hasNext()) {
                                        semesters = new ArrayList<>();
                                        String de = (String) iterator2.next();
                                        int semvalue = third.getJSONObject(de).getInt("Semesters");
                                        int i = 1;
                                        while (i <= semvalue) {
                                            semesters.add("S" + i);
                                            i++;
                                        }
                                        subvalues.put(de, semesters);
                                        map.put(c, subvalues);
                                    }
                                }
                                courses = new String[map.size()];
                                for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry : map.entrySet()) {
                                    courses[inc++] = entry.getKey();
                                }
                                setSpinners();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject body = new JSONObject();
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return body.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        queue.add(postRequest);
    }

    void setSpinners(){
        course.setAdapter(new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item,courses));
        course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                flagCourse = (String) adapterView.getItemAtPosition(position);
                int size = map.get(flagCourse).size();
                depts = new String[size];
                int m = 0;
                for (Map.Entry<String, ArrayList<String>> entry : map.get(adapterView.getItemAtPosition(position)).entrySet()) {
                   depts[m++] = entry.getKey();
                }
                dept.setAdapter(new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item,depts));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                flagDept = (String) adapterView.getItemAtPosition(position);
                semesters = map.get(flagCourse).get(flagDept);
                sem.setAdapter(new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item,semesters));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}

