package domain.project1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Tab1 extends Fragment {

    RecyclerView recyclerView;
    TextView no_data;
    HomeRecyclerViewAdapter adapter;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View Tab1View = inflater.inflate(R.layout.activity_tab1,container,false);
        recyclerView = Tab1View.findViewById(R.id.recyclerview);
        no_data = Tab1View.findViewById(R.id.textView5);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new HomeRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        return Tab1View;

    }
}
