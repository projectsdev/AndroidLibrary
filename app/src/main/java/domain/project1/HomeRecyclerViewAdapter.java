package domain.project1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeRecyclerViewAdapter extends /*BaseAdapter{

    Context context;
    public HomeRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView book_name;
        TextView author_name;
        TextView published;
        TextView available;
        ImageView book_image;
        if(view == null){
            view = View.inflate(context,R.layout.activity_home_content_view,null);
            book_image = view.findViewById(R.id.book);
            book_name = view.findViewById(R.id.bookname);
            author_name = view.findViewById(R.id.author);
            published = view.findViewById(R.id.published);
            available = view.findViewById(R.id.available);
//            book_image.setImageResource(R.drawable.book);

        }
        return view;
    }*/
   RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeHolder> {

    Context context;
    ArrayList<BookDetails> books;
    public HomeRecyclerViewAdapter(Context context,ArrayList<BookDetails> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.activity_home_content_view,null);
        HomeHolder holder = new HomeHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HomeHolder holder, int position) {
        BookDetails book = books.get(position);
        holder.book_name.setText(book.getBook_name());
        holder.author_name.setText("Author : "+book.getAuthor());
        holder.published.setText("Published Year : "+book.getPublished_year());
        holder.available.setText(Html.fromHtml("Available : <font color=#4eb175>"+book.getAvailable()+"</font>"));
        holder.book_image.setImageResource(R.drawable.book);
        if(book.getSubject_name().equals("not defined")){
            holder.subject_name.setVisibility(View.GONE);
        }
        else
            holder.subject_name.setText("Subject : " + book.getSubject_name());
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        return books.size();
    }

    public class HomeHolder extends RecyclerView.ViewHolder{
        TextView book_name;
        TextView author_name;
        TextView published;
        TextView available;
        TextView subject_name;
        ImageView book_image;
        public HomeHolder(View convertView) {
            super(convertView);
            book_image = convertView.findViewById(R.id.book);
            book_name = convertView.findViewById(R.id.bookname);
            author_name = convertView.findViewById(R.id.author);
            published = convertView.findViewById(R.id.published);
            available = convertView.findViewById(R.id.booked);
            subject_name = convertView.findViewById(R.id.subject);
        }
    }
}
