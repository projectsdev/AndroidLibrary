package domain.project1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class renewOrReturn {

    Context context;
    SharedPreferences preferences;
    ArrayList<HistoryDetails> historyDetails;
    int position;
    HistoryContentAdapter historyAdap;
    public renewOrReturn(Context context, ArrayList<HistoryDetails> historyDetails, int position, HistoryContentAdapter historyAdap) {
        this.context = context;
        this.position = position;
        this.historyDetails = historyDetails;
        this.historyAdap = historyAdap;
        this.preferences = context.getSharedPreferences("UserDetails",Context.MODE_PRIVATE);

    }

    void renewOrReturn(final int renew_return, final String txn_id, final String serial_no, final String book_id,
                       final String dept, final String course, final String sem, final ProgressBar bar, final Button renew, final Button return_,
                       final TextView issue_date, final TextView final_date, final TextView no_nooks){
        bar.setVisibility(View.VISIBLE);
        renew.setVisibility(View.GONE);
        return_.setVisibility(View.GONE);
        String url = new getUrl().setUrl(context,"renewOrreturn");
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
                                issue_date.setText(Html.fromHtml("Issued on : " +object.getString("startDate")));
                                final_date.setText(Html.fromHtml("Last Date : <font color=#8470ff>" +object.getString("finalDate")+"</font>"));
                                bar.setVisibility(View.GONE);
                                renew.setVisibility(View.VISIBLE);
                                return_.setVisibility(View.VISIBLE);
                            }
                            else if(update == 2){
//                                Intent intent = new Intent(context,BookingHistory.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                context.startActivity(intent);
//                                ((Activity)context).finish();
                                historyDetails.remove(position);
                                historyAdap.notifyDataSetChanged();
                                bar.setVisibility(View.GONE);
                                renew.setVisibility(View.VISIBLE);
                                return_.setVisibility(View.VISIBLE);
                                if(historyDetails.isEmpty()){
                                    no_nooks.setVisibility(View.VISIBLE);
                                }

                            }
                            else if(update == 0){
                                bar.setVisibility(View.GONE);
                                renew.setVisibility(View.VISIBLE);
                                return_.setVisibility(View.VISIBLE);
                                Toast.makeText(context,"Update Failed",Toast.LENGTH_SHORT).show();
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
                        renew.setVisibility(View.VISIBLE);
                        return_.setVisibility(View.VISIBLE);
                        Toast.makeText(context,"Network Error",Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject body = new JSONObject();
                try {
                    body.put("admission_number",admission_no);
                    body.put("renew_return",renew_return);
                    body.put("txn_id",txn_id);
                    body.put("serial_no",serial_no);
                    body.put("dept",dept);
                    body.put("course",course);
                    body.put("semester",sem);
                    body.put("book_id",book_id);
                    boolean check = historyDetails.get(position).isRenewable();
                    if(check)
                    body.put("RN_NRN",1);
                    else
                    body.put("RN_NRN",2);

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
