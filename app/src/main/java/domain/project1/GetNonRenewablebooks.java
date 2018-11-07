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
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

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

public class GetNonRenewablebooks extends AppCompatActivity {

    TextView title,no_books;
    ProgressBar bar;
    SearchView searchView;
    RecyclerView recyclerView;
    Context context;
    String course,department,semester;
    BookingBookAdapter adapter;
    LayoutAnimationController controller = null;
    RelativeLayout linearLayout;
    TextView order_text;
    ProgressBar progress;
    SharedPreferences preferences;
    String admission_number;
    int count = 0;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,controlActivity.class).
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_booking);
        context = this;
        title = findViewById(R.id.head);
        no_books = findViewById(R.id.mid);
        bar = findViewById(R.id.bar);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recycler);
        linearLayout = findViewById(R.id.linear_L);
        order_text = findViewById(R.id.order);
        progress = findViewById(R.id.proBar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        course = "null";
        department = "null";
        semester = "null";
        title.setText("Search your book");
        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_falldown);
        preferences = getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
        admission_number = preferences.getString("admission_no","null");
        checkMyTransactionCount();
    }

    void getBooks(){
        String url = new getUrl().setUrl(context,"getNonRenewables");
        RequestQueue queue = Volley.newRequestQueue(context);
        final ArrayList<BookDetails> bookDetails = new ArrayList<>();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            bar.setVisibility(View.INVISIBLE);
                            Log.d("Response",response);
                            JSONObject object = new JSONObject(response);
                            int fetch = object.getInt("fetch");
                            if(fetch == 0){
                                no_books.setVisibility(View.VISIBLE);
                            }
                            else {
                                JSONObject oj = object.getJSONObject("data");
                                Iterator<?> keys = oj.keys();
                                while (keys.hasNext()) {
                                    String key = (String) keys.next();
                                    JSONObject obj = oj.getJSONObject(key);
                                    String book_name = obj.getString("book_name");
                                    BookDetails d = new BookDetails(key, book_name, course,
                                            department, obj.getString("author"), semester,"not defined",
                                            obj.getString("faculty"),
                                            obj.getBoolean("renewable"), obj.getInt("volume"),
                                            obj.getInt("available"), obj.getInt("published"));
                                    bookDetails.add(d);


                                    adapter = new BookingBookAdapter(2,context,
                                            no_books,recyclerView,linearLayout,order_text ,progress,bookDetails
                                            ,course,department,semester,count);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutAnimation(controller);
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                    recyclerView.scheduleLayoutAnimation();
                                    searchView.setQueryHint("Enter Book name/author...");
                                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                        @Override
                                        public boolean onQueryTextSubmit(String query) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onQueryTextChange(String newText) {
                                            adapter.getFilter().filter(newText);
                                            return false;
                                        }
                                    });
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
                        bar.setVisibility(View.INVISIBLE);
                        no_books.setVisibility(View.VISIBLE);
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject body = new JSONObject();
                try {
                   /* body.put("course",course);
                    body.put("dept",department);
                    body.put("semester",semester);
                    body.put("flag",1); // 1 for booking contents*/
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

    void checkMyTransactionCount(){

        String url = new getUrl().setUrl(context,"MyTransactionCount");
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            boolean proceed = object.getBoolean("proceed");
                            if(!proceed){
                                count = 3;
                            }
                            else{
                                count = object.getInt("remaining");
                            }
                            getBooks();
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
                    body.put("admission_no",admission_number);
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
