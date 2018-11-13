package domain.project1;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import java.util.Iterator;

public class Tab1 extends Fragment {

    RecyclerView recyclerView;
    TextView no_data;
    ProgressBar bar;
    HomeRecyclerViewAdapter adapter;
    Context context;
    View Tab1View = null;
    SharedPreferences preferences;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        preferences = context.getSharedPreferences("UserDetails",Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(Tab1View == null ){
        Tab1View = inflater.inflate(R.layout.activity_tab1,container,false);
        recyclerView = Tab1View.findViewById(R.id.recycle);
        no_data = Tab1View.findViewById(R.id.message);
        bar = Tab1View.findViewById(R.id.progress);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        }
        return Tab1View;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bar.setVisibility(View.VISIBLE);
        getBooks();
    }

    void getBooks(){
        String url = new getUrl().setUrl(context,"getHomeContents");
        final String course = preferences.getString("course","null");
        final String dept = preferences.getString("department","null");
        RequestQueue queue = Volley.newRequestQueue(context);
        final ArrayList<BookDetails> bookDetails = new ArrayList<>();
        no_data.setVisibility(View.INVISIBLE);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            bar.setVisibility(View.INVISIBLE);
                            Log.d("Response",response);
                            JSONObject object = new JSONObject(response);
                            int fetch = object.getInt("fetch");
                            if(fetch == 0){
                                no_data.setVisibility(View.VISIBLE);
                            }
                            else{
                                JSONObject oj = object.getJSONObject("data");
                                Iterator<?> keys = oj.keys();
                                while (keys.hasNext()){
                                    String key = (String)keys.next();
                                    JSONObject ob = oj.getJSONObject(key);
                                    Iterator<?> k = ob.keys();
                                    while (k.hasNext()) {
                                        String key2 = (String)k.next();
                                        JSONObject obj = ob.getJSONObject(key2);
                                        String book_name = obj.getString("book_name");
                                        BookDetails d = new BookDetails(key2, book_name, course,
                                                dept, obj.getString("author"), obj.getString("semester"),obj.getString("subject"),
                                                obj.getString("faculty"),obj.getBoolean("renewable"), obj.getInt("volume"),
                                                obj.getInt("available"), obj.getInt("published"));
                                        bookDetails.add(d);
                                    }
                                }
                                adapter = new HomeRecyclerViewAdapter(context,bookDetails);
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
                        no_data.setVisibility(View.VISIBLE);
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
