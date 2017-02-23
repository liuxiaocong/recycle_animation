package com.example.administrator.layoutmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.administrator.layoutmanager.ContactsAdapter.OnStartDragListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NameAdapter.OnStartDragListener,OnStartDragListener {

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.dropdown_list);

        //android.support.v7.widget.StaggeredGridLayoutManager layoutManager = new android.support.v7.widget.StaggeredGridLayoutManager(3,0);


        //FlowLayoutManager flowLayoutManager = new FlowLayoutManager();


        List<String> name = new ArrayList<>();
        name.add("music");
        name.add("sport");
        name.add("Internet");
        name.add("sex movie");
        name.add("oa");
        name.add("happy hour the time");
        name.add("cc");
        name.add("ddd");
        name.add("AAAAAAAAAAA");
        name.add("lol");
        name.add("dota");
        name.add("eeeeeee");
        name.add("occc");
        name.add("123");
        name.add("eoeo");


        NameAdapter nameAdapter = new NameAdapter(name);
        nameAdapter.setOnStartDragListener(this);
        nameAdapter.setTargetRecyclerView(mRecyclerView);

//        ContactsAdapter adapter = new ContactsAdapter(name);
//        adapter.setOnStartDragListener(this);
//        adapter.setTargetRecyclerView(mRecyclerView);


        mRecyclerView.setAdapter(nameAdapter);

        FlowLayoutManager layoutManager = new FlowLayoutManager();

        mRecyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(nameAdapter);

        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
