package com.zhaoweihao.architechturesample.activity;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.quiz.Query;
import com.zhaoweihao.architechturesample.adapter.HomeCourseMoreQuizRankAdapter;
import com.zhaoweihao.architechturesample.contract.HomeCourseMoreQuizRankContract;
import com.zhaoweihao.architechturesample.presenter.HomeCourseMoreQuizRankPresenter;


import java.util.ArrayList;
/**
*@description 首页-课程-详细界面-更多-答题排名
*@author
*@time 2019/1/28 12:59
*/
public class HomeCourseMoreQuizRankActivity extends BaseActivity implements HomeCourseMoreQuizRankContract.View {

    public static final String TAG = "HomeCourseMoreQuizRankActivity";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyView;
    private HomeCourseMoreQuizRankContract.Presenter presenter;
    private HomeCourseMoreQuizRankAdapter adapter;

    private String courseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        new HomeCourseMoreQuizRankPresenter(this, this);

        initViews(null);

        getSupportActionBar().setTitle("答题排行榜");

        courseId = String.valueOf(getIntent().getIntExtra("courseId", 0));

        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.queryQuizList(courseId);
            if (adapter != null)
            adapter.notifyDataSetChanged();
            stopLoading();
        });

        presenter.queryQuizList(courseId);


    }

    @Override
    public void showResult(ArrayList<Query> queryArrayList) {
        runOnUiThread(() -> {
            if (adapter == null) {
                adapter = new HomeCourseMoreQuizRankAdapter(this, queryArrayList);
                recyclerView.setAdapter(adapter);
            }
            else {
                adapter.notifyDataSetChanged();
            }
            adapter.setItemClickListener((v, position) -> {


            });
            adapter.setItemLongClickListener((view, position) -> {

            });
        });
    }

    @Override
    public void startLoading() {
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
    }

    @Override
    public void stopLoading() {
        if (swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(false));
        }
    }

    @Override
    public void showLoadError(String error) {
        Snackbar.make(recyclerView, error, Snackbar.LENGTH_SHORT)
                .setAction("重试", view -> presenter.queryQuizList(courseId)).show();
    }

    @Override
    public void showNothing() {
        runOnUiThread(() -> {
            emptyView.setVisibility(View.VISIBLE);
        });
    }


    @Override
    public void setPresenter(HomeCourseMoreQuizRankContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.refresh);
        emptyView = findViewById(R.id.empty_view);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }
}
