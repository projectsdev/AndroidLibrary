package domain.project1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
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
import java.util.HashMap;
import java.util.Map;

public class bookingFunction {
    Context context;
    SharedPreferences preferences;
    HashMap<String,Integer> object;
    ArrayList<BookDetails> listArrayBook;
    String course,dept,semester;
    ProgressBar proBar;
    TextView checkout;
    JSONObject json = new JSONObject();
    public bookingFunction(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("UserDetails",Context.MODE_PRIVATE);
    }
     void parseDataAndSend(HashMap<String,Integer> object, ArrayList<BookDetails> listArrayBook,
                           String course, String dept, String sem, ProgressBar progressBar, TextView checkout){
        this.object = object;
        this.listArrayBook = listArrayBook;
        this.course = course;
        this.dept = dept;
        this.semester = sem;
        this.proBar = progressBar;
        this.checkout = checkout;
        for(Map.Entry<String,Integer> entry:object.entrySet()){
            String entryKey = entry.getKey();
            Integer entryValue = entry.getValue();
            for(int i = 0; i<listArrayBook.size();i++){
                if(entryKey.equals(listArrayBook.get(i).getBook_id()) && entryValue!=0){
                    JSONObject ob = new JSONObject();
                    try {
                        ob.put("book_name",listArrayBook.get(i).getBook_name());
                        ob.put("author_name",listArrayBook.get(i).getAuthor());
                        ob.put("renewable",listArrayBook.get(i).isRenewable());
                        ob.put("published",listArrayBook.get(i).getPublished_year());
                        ob.put("semester",listArrayBook.get(i).getSemester());
                        ob.put("volume",listArrayBook.get(i).getVolume());
                        ob.put("course",course);
                        ob.put("dept",dept);
                        ob.put("semester",sem);
                        ob.put("booked_no",entryValue);
                        this.json.put(entryKey,ob);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
         updateFunction();
    }
    void updateFunction(){
        proBar.setVisibility(View.VISIBLE);
        checkout.setVisibility(View.GONE);
        String url = new getUrl().setUrl("bookMyBooks");
        final String admission_no = preferences.getString("admission_no","null");
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject object = new JSONObject(response);
                            int update = object.getInt("update");
                            if(update == 1){
                                Toast.makeText(context,"Booking Successful",Toast.LENGTH_SHORT).show();
                                proBar.setVisibility(View.GONE);
                                Intent intent = new Intent(context,BooksBooking.class);
                                intent.putExtra("course",course);
                                intent.putExtra("dept",dept);
                                intent.putExtra("semester",semester);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            else{
                                proBar.setVisibility(View.GONE);
                                checkout.setVisibility(View.VISIBLE);
                                Toast.makeText(context,"Booking Failed",Toast.LENGTH_SHORT).show();
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
                        proBar.setVisibility(View.GONE);
                        checkout.setVisibility(View.VISIBLE);
                        Toast.makeText(context,"Booking Failed",Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject body = new JSONObject();
                try {
                    body.put("admission_no",admission_no);
                    body.put("course",course);
                    body.put("dept",dept);
                    body.put("semester",semester);
                    body.put("my_books",json);

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
