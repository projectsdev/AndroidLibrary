package domain.project1;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryContentAdapter extends   RecyclerView.Adapter<HistoryContentAdapter.HistoryHolder> {

    Context context;
    ArrayList<HistoryDetails> history;
    public HistoryContentAdapter(Context context,ArrayList<HistoryDetails> hist) {
        this.context = context;
        this.history = hist;
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.history_contents,null);
        HistoryHolder holder = new HistoryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HistoryHolder holder, int position) {
        final HistoryDetails h = history.get(position);
        holder.book_image.setImageResource(R.drawable.book);
        holder.book_name.setText(h.getBook_name());
        holder.author_name.setText("Author : "+h.getAuthor());
        holder.published.setText("Published Year : "+h.getPublished_year());
        holder.booked.setText(Html.fromHtml("Booked : <strong>"+ h.getBooked_no() + "</strong>"));
        holder.issue_date.setText(Html.fromHtml("Issued on: "+h.getIssue_date()));
        if(h.getSubject().equals("not defined")){
            holder.subject_name.setVisibility(View.GONE);
        }
        else
            holder.subject_name.setText("Subject : " + h.getSubject());
        if(expired(h.getLast_date())){
            holder.last_date.setText(Html.fromHtml("Last Date : <font color=#ff0000>" +h.getLast_date()+"</font>"));
            holder.renew.setBackgroundColor(Color.parseColor("#adff0000"));
        }
        else{
            holder.last_date.setText(Html.fromHtml("Last Date : <font color=#8470ff>" +h.getLast_date()+"</font>"));
            holder.renew.setBackgroundColor(Color.parseColor("#4eb175"));
        }
        holder.renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expired(h.getLast_date())){
                    Toast.makeText(context,"You can't renew this book",Toast.LENGTH_SHORT).show();
                }
                else{
                   new renewOrReturn(context).renewOrReturn(1,h.getTransaction_id(),h.getSerial_No(),h.getBook_id(),h.getDept(),
                           h.getCourse(),h.getSemester(),holder.bar,holder.renew,holder.return_,holder.issue_date,holder.last_date);
                }
            }
        });
        holder.return_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new renewOrReturn(context).renewOrReturn(2,h.getTransaction_id(),h.getSerial_No(),h.getBook_id(),h.getDept(),
                        h.getCourse(),h.getSemester(),holder.bar,holder.renew,holder.return_,holder.issue_date,holder.last_date);
                }
        });

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        return history.size();
    }

    boolean expired(String lastDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date last_date = dateFormat.parse(lastDate);
            Date today = dateFormat.parse(dateFormat.format(new Date()));
            if(today.after(last_date)){
                return true;
            }
            else
                return false;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
    public class HistoryHolder extends RecyclerView.ViewHolder{
        TextView book_name;
        TextView author_name;
        TextView published;
        TextView booked;
        TextView subject_name,issue_date,last_date;
        ImageView book_image;
        Button renew,return_;
        ProgressBar bar;
        public HistoryHolder(View convertView) {
            super(convertView);
            book_image = convertView.findViewById(R.id.book);
            book_name = convertView.findViewById(R.id.bookname);
            author_name = convertView.findViewById(R.id.author);
            published = convertView.findViewById(R.id.published);
            booked = convertView.findViewById(R.id.booked);
            subject_name = convertView.findViewById(R.id.subject);
            issue_date = convertView.findViewById(R.id.issue);
            last_date = convertView.findViewById(R.id.last);
            renew = convertView.findViewById(R.id.renew);
            return_ = convertView.findViewById(R.id.return_);
            bar = convertView.findViewById(R.id.bar);
        }
    }

}
