package domain.project1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class BookingBookAdapter extends  RecyclerView.Adapter<BookingBookAdapter.HomeHolder> implements Filterable {

    Context context;
    ArrayList<BookDetails> books,filterBooks;
    TextView no_books;
    RecyclerView recyclerView;
    ProgressBar proBar;
    RelativeLayout linearLayout;
    TextView order_text;
    HashMap<String,Integer> cartMap = new HashMap<>();
    int itemCount,addCount = 0;
    String course,dept,semester;
    int RN_NRN;

    public BookingBookAdapter(int RN_NRN,Context context, TextView no_books, RecyclerView recyclerView ,
                              RelativeLayout linearLayout, TextView order_text,ProgressBar proBar,
                              ArrayList<BookDetails> books, String course, String dept, String semester,int count) {
        this.context = context;
        this.books = books;
        this.filterBooks = books;
        this.no_books = no_books;
        this.recyclerView = recyclerView;
        this.linearLayout = linearLayout;
        this.order_text = order_text;
        this.course = course;
        this.dept = dept;
        this.RN_NRN = RN_NRN;
        this.semester = semester;
        this.proBar = proBar;
        this.itemCount = count;
        for(int i = 0; i<books.size(); i++){
            cartMap.put(books.get(i).getBook_id(),0);
        }

    }

    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.layout_book_booking,null);
        HomeHolder holder = new HomeHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HomeHolder holder, int position) {
        final BookDetails book = books.get(position);
        holder.book_name.setText(book.getBook_name());
        holder.author_name.setText("Author : "+book.getAuthor());
        holder.published.setText("Published Year : "+book.getPublished_year());
        holder.available.setText(Html.fromHtml("Available : <font color=#4eb175>"+book.getAvailable()+"</font>"));
        holder.book_image.setImageResource(R.drawable.book);
        holder.item_count.setText(String.valueOf(cartMap.get(book.getBook_id())));
        if(book.getFaculty().equals("nil")){
            holder.faculty.setVisibility(View.GONE);
        }
        else{
            holder.faculty.setText(Html.fromHtml("Faculty : "+book.getFaculty()));
        }
        if(book.isRenewable()){
            holder.renewable.setText(Html.fromHtml("<font color=#4eb175>Renewable</font>"));
        }
        else
            holder.renewable.setText(Html.fromHtml("<font color=#ff0000>Non Renewable</font>"));

        if(book.getSubject_name().equals("not defined")){
            holder.subject.setVisibility(View.GONE);
        }
        else
            holder.subject.setText("Subject : " + book.getSubject_name());

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(itemCount<3) {
                        itemCount++;
                        addCount++;
                        int count = cartMap.get(book.getBook_id());
                        cartMap.put(book.getBook_id(), count + 1);
                        order_text.setText("Added " +addCount+" Books | Order" );
                        linearLayout.setVisibility(View.VISIBLE);
                        holder.item_count.setText(String.valueOf(cartMap.get(book.getBook_id())));
                    }
                    else{
                        Toast.makeText(context,"Only 3 books can be booked",Toast.LENGTH_SHORT).show();
                    }



            }
        });
        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = cartMap.get(book.getBook_id());
                if(count!=0){
                    itemCount--;
                    addCount--;
                    cartMap.put(book.getBook_id(),count-1);
                    if(addCount == 0){
                        linearLayout.setVisibility(View.GONE);
                    }
                    else{
                        order_text.setText("Added " +addCount+" Books | Order" );
                    }
                }
                holder.item_count.setText(String.valueOf(cartMap.get(book.getBook_id())));
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new bookingFunction(context).parseDataAndSend(RN_NRN,cartMap,books,course,dept,semester,proBar,order_text);
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getItemCount() {
        return books.size();
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }
    Filter myFilter = new  Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint != null && constraint.length()>0 ) {
                ArrayList<BookDetails> filters=new ArrayList<>();
                for(int i=0;i<filterBooks.size();i++)
                {
                    if((filterBooks.get(i).getBook_name().toLowerCase()+filterBooks.get(i).getAuthor().toLowerCase()).contains(constraint))
                    {
                        BookDetails p=new BookDetails(filterBooks.get(i).getBook_id(),filterBooks.get(i).getBook_name(),
                                filterBooks.get(i).getCourse(),filterBooks.get(i).getDepartment(),filterBooks.get(i).getAuthor(),
                                filterBooks.get(i).getSemester(),filterBooks.get(i).getSubject_name(),filterBooks.get(i).getFaculty(),filterBooks.get(i).isRenewable(),
                                filterBooks.get(i).getVolume(),filterBooks.get(i).getAvailable(),filterBooks.get(i).getPublished_year());
                        filters.add(p);
                    }
                }
                results.count=filters.size();
                results.values=filters;
            }
            else
            {
                ArrayList<BookDetails> dFilters=new ArrayList<>();
                for(int i=0;i<filterBooks.size();i++){
                    BookDetails p=new BookDetails(filterBooks.get(i).getBook_id(),filterBooks.get(i).getBook_name(),
                            filterBooks.get(i).getCourse(),filterBooks.get(i).getDepartment(),filterBooks.get(i).getAuthor(),
                            filterBooks.get(i).getSemester(),filterBooks.get(i).getSubject_name(),filterBooks.get(i).getFaculty(),filterBooks.get(i).isRenewable(),
                            filterBooks.get(i).getVolume(),filterBooks.get(i).getAvailable(),filterBooks.get(i).getPublished_year());

                    dFilters.add(p);
                }
                results.count=dFilters.size();
                results.values=dFilters;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            books=(ArrayList<BookDetails>) results.values;
            if(books.isEmpty())
                no_books.setVisibility(View.VISIBLE);
            else
                no_books.setVisibility(View.INVISIBLE);
            notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();

        }
    };

    public class HomeHolder extends RecyclerView.ViewHolder{
        TextView book_name;
        TextView author_name,subject;
        TextView published;
        TextView available;
        TextView renewable;
        TextView add,sub,item_count,faculty;
        ImageView book_image;

        public HomeHolder(View convertView) {
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
}
