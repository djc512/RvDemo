package com.example.djc512.rvdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout srf;
    private RecyclerView rv;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my);
        initView();
    }

    boolean isnotLoad = false;

    private void initView() {
        rv = (RecyclerView) findViewById(R.id.rv);
        srf = (SwipeRefreshLayout) findViewById(R.id.srf);

        for (int i = 0; i < 20; i++) {
            list.add("hello" + i);
        }
        final MyAdapter adapter = new MyAdapter(R.layout.item, list);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        srf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.add(0, "刷新");
                        srf.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });


        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isnotLoad) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.loadMoreEnd();
                            Toast.makeText(MainActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                    return;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.add("加载");
                        adapter.loadMoreComplete();
                        adapter.notifyDataSetChanged();
                        isnotLoad = true;
                    }
                }, 2000);
            }
        }, rv);
    }

    public class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public MyAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            TextView tv = helper.getView(R.id.tv);
            tv.setText(item);
        }
    }
}
