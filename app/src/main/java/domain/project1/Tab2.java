package domain.project1;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import java.util.HashMap;
import java.util.Iterator;

public class Tab2 extends Fragment {
    RecyclerView recyclerView;
    TextView no_books;
    ProgressBar bar;
    Context context;
    recentAdapter adapter;
    SharedPreferences preferences;
    HashMap<Integer,Object> detailMap = new HashMap<>();
    HashMap<Integer,String> dateMap = new HashMap<>();
    ArrayList<Integer> size = new ArrayList<>();
    int increment = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        preferences = context.getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recently_added,container,false);
        recyclerView = view.findViewById(R.id.rcview);
        no_books = view.findViewById(R.id.nobooks);
        bar = view.findViewById(R.id.bar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        getRecents();
        return view;

    }
    void getRecents(){

        String url = new getUrl().setUrl(context,"getRecents");
        final String course = preferences.getString("course","null");
        final String dept = preferences.getString("department","null");
        RequestQueue queue = Volley.newRequestQueue(context);
        final ArrayList<BookDetails> bookDetails = new ArrayList<>();
        no_books.setVisibility(View.INVISIBLE);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            bar.setVisibility(View.INVISIBLE);
                            Log.d("Response tab 2",response);
                            JSONObject object = new JSONObject(response);
                            int fetch = object.getInt("fetch");
                            if(fetch == 0){
                                no_books.setVisibility(View.VISIBLE);
                            }
                            else{
                                JSONObject oj = object.getJSONObject("data");
                                Iterator<?> keys = oj.keys();
                                while (keys.hasNext()){
                                    size.add(1);
                                    String date_key = (String)keys.next();
                                    detailMap.put(increment++,date_key);
                                    JSONObject ob = oj.getJSONObject(date_key);
                                    Iterator<?> k = ob.keys();

                                    while (k.hasNext()) {
                                        String book_id = (String)k.next();
                                        JSONObject obj = ob.getJSONObject(book_id);
                                        String book_name = obj.getString("book_name");
                                        BookDetails d = new BookDetails(book_id, book_name, course,
                                                dept, obj.getString("author"), obj.getString("semester"),obj.getString("subject"),
                                                obj.getString("faculty"),obj.getBoolean("renewable"), obj.getInt("volume"),
                                                obj.getInt("available"), obj.getInt("published"));
                                        bookDetails.add(d);
                                        size.add(2);
                                        detailMap.put(increment++,d);
                                    }

                                }
                                adapter = new recentAdapter(size,detailMap);
                                recyclerView.setAdapter(adapter);
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
                    body.put("course",course);
                    body.put("dept",dept);
                    body.put("flag",0); // 0 for home contents
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