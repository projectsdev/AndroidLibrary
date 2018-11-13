package domain.project1;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class recentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    int[] size  = new int[]{1,2,2,1};
    ArrayList<Integer> size;
    HashMap<Integer,Object> hashMap;

    String date = null;
    public recentAdapter(ArrayList<Integer> size,HashMap<Integer, Object> hashMap) {
        this.size = size;
        this.hashMap = hashMap;
        Log.d("Map",hashMap.toString());
    }

    @Override
    public int getItemViewType(int position) {
        return size.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 1:
                View view = View.inflate(parent.getContext(),R.layout.recent_1,null);
                return new dateHolder(view);

            case 2:
                View view2 = View.inflate(parent.getContext(),R.layout.layout_book_booking,null);
                return new bookHolder(view2);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (size.get(position)){
            case 1:
                dateHolder dH = (dateHolder) holder;
                date = (String)hashMap.get(position);
                dH.date.setText(formatDate(date));
                break;

            case 2:
                bookHolder bH = (bookHolder) holder;
                bH.add.setVisibility(View.GONE);
                bH.sub.setVisibility(View.GONE);
                bH.item_count.setVisibility(View.GONE);
                BookDetails book = (BookDetails)hashMap.get(position);
                bH.book_name.setText(book.getBook_name());
                bH.author_name.setText("Author : "+book.getAuthor());
                bH.published.setText("Published Year : "+book.getPublished_year());
                bH.available.setText(Html.fromHtml("Available : <font color=#4eb175>"+book.getAvailable()+"</font>"));
                bH.book_image.setImageResource(R.drawable.book);
                if(book.getFaculty().equals("nil")){
                    bH.faculty.setVisibility(View.GONE);
                }
                else{
                    bH.faculty.setText(Html.fromHtml("Faculty : "+book.getFaculty()));
                }
                if(book.isRenewable()){
                    bH.renewable.setText(Html.fromHtml("<font color=#4eb175>Renewable</font>"));
                }
                else
                    bH.renewable.setText(Html.fromHtml("<font color=#ff0000>Non Renewable</font>"));

                if(book.getSubject_name().equals("not defined")){
                    bH.subject.setVisibility(View.GONE);
                }
                else
                    bH.subject.setText("Subject : " + book.getSubject_name());

                break;
        }
    }

    @Override
    public int getItemCount() {
        return size.size();
    }

    class dateHolder extends RecyclerView.ViewHolder{
        TextView date;
        public dateHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
        }
    }
    class bookHolder extends RecyclerView.ViewHolder{
        TextView book_name;
        TextView author_name,subject;
        TextView published;
        TextView available;
        TextView renewable;
        TextView add,sub,item_count,faculty;
        ImageView book_image;

        public bookHolder(View convertView) {
            super(convertView);
            book_image = convertView.findViewById(R.id.book);
            book_name = convertView.findViewById(R.id.bookname);
            author_name = convertView.findViewById(R.id.author);
            published = convertView.findViewById(R.id.published);
            available = convertView.findViewById(R.id.booked);
            renewable = convertView.findViewById(R.id.renewable);
            subject = convertView.findViewById(R.id.subject);
            add = convertView.findViewById(R.id.add);
            sub = convertView.findViewById(R.id.sub);
            item_count = convertView.findViewById(R.id.item_count);
            faculty = convertView.findViewById(R.id.faculty);
        }
    }
    String formatDate(String date){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
              SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
            try {
                date = dateFormat2.format(dateFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
    }
}
