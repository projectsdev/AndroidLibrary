package domain.project1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class BookingHistory extends AppCompatActivity {
    TextView head;
    RecyclerView recyclerView;
    TextView no_books;
    ProgressBar bar;
    Context context;
    SharedPreferences preferences;
    LayoutAnimationController controller = null;
    ArrayList<HistoryDetails> historyArray = new ArrayList<>();
    HistoryContentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);
        context = this;
        head = findViewById(R.id.head);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_falldown);
        no_books = findViewById(R.id.no_books);
        bar = findViewById(R.id.bar);
        preferences = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
        getBookings();
        }
            void getBookings(){
                bar.setVisibility(View.VISIBLE);
                String url = new getUrl().setUrl(context,"getMyBookings");
                final String admission_no = preferences.getString("admission_no","null");
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    bar.setVisibility(View.GONE);
                                    JSONObject object = new JSONObject(response);
                                    int fetch = object.getInt("fetch");
                                    if(fetch == 0){
                                        no_books.setText("No Books");
                                        no_books.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        JSONObject first = object.getJSONObject("data");
                                        Iterator<?> f_keys = first.keys();
                                        while (f_keys.hasNext()){
                                            String Transaction_id = (String)f_keys.next();
                                            JSONObject temp = first.getJSONObject(Transaction_id).getJSONObject("MyBooks");
                                            Iterator<?> s_keys = temp.keys();
                                            while (s_keys.hasNext()){
                                                String innerKey = (String)s_keys.next();
                                                JSONObject second = temp.getJSONObject(innerKey);
                                                boolean returnStatus = second.getBoolean("return_status");
                                                if(!returnStatus) {
                                                    HistoryDetails hD = new HistoryDetails(Transaction_id, innerKey,second.getString("book_id"),second.getString("book_name"),
                                                            second.getString("subject"),second.getString("author_name"), second.getString("pick_date"), second.getString("last_date"),
                                                            second.getInt("published"),1, second.getBoolean("renewable"), returnStatus,second.getString("dept"),second.getString("course"),second.getString("semester"));
                                                    historyArray.add(hD);
                                                }
                                            }
                                        }
                                        if(historyArray.isEmpty()){
                                            no_books.setText("No Books");
                                            no_books.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            adapter = new HistoryContentAdapter(context, historyArray);
                                            recyclerView.setAdapter(adapter);
                                            recyclerView.setLayoutAnimation(controller);
                                            recyclerView.getAdapter().notifyDataSetChanged();
                                        }
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
                                bar.setVisibility(View.GONE);
                                no_books.setText("Network Error");
                                no_books.setVisibility(View.VISIBLE);
                            }
                        }
                ) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        JSONObject body = new JSONObject();
                        try {
                            body.put("admission_no",admission_no);


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

}
