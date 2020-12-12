package com.zhaoweihao.architechturesample.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.base.BaseActivity;
import com.zhaoweihao.architechturesample.bean.Leave;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.adapter.HomeCourseMoreLeaveListAdapter;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;
import static com.zhaoweihao.architechturesample.util.Utils.*;

/**
 *
 */
/**
*@description 首页-课程-详细界面-更多-请假-展示所有请假条的Activity
*@author
*@time 2019/1/28 12:58
*/

public class HomeCourseMoreLeaveListActivity extends BaseActivity {

    private static final Class THIS_CLASS = HomeCourseMoreLeaveListActivity.class;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayout emptyView;

    private HomeCourseMoreLeaveListAdapter adapter;

    private ArrayList<Leave> leaveList = new ArrayList<>();

    private FloatingActionButton ftbn_leave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);
        // 初始化控件
        initViews();

        // 以获取当前用户请假条为例
        String suffix = Constant.QUERY_LEAVE_URL;
        // 获取当前用户学号(学生)或教师编号(教师)
        // 假设当前是学生用户，初始化参数名字
        String studentId = "studentId";// 学生应选用这个
        String teacherId = "teacherId";
        String url;
        //获取当前的学生的请假信息
        User user3 = DataSupport.findLast(User.class);
        // 这里判断是老师还是学生 (省略判断语句)
        if (user3.getTeacherId() != null) {
            // 拼接完整url
            url = suffix + "?" + teacherId + "=" + user3.getTeacherId();
            ftbn_leave.setVisibility(View.INVISIBLE);
        } else {
            // 拼接完整url
            url = suffix + "?" + studentId + "=" + user3.getStudentId();
            ftbn_leave.setVisibility(View.VISIBLE);
        }
        log(THIS_CLASS, url);

        swipeRefreshLayout.setOnRefreshListener(() ->{
            requestLeaveList(url);
        });
        Intent intent1=getIntent();
        ftbn_leave.setOnClickListener(v->{
           Intent intent=new Intent(HomeCourseMoreLeaveListActivity.this,HomeCourseMoreLeaveSubmitActivity.class);
           intent.putExtra("courseId",intent1.getIntExtra("courseId",0));
           startActivity(intent);
        });
        // 请求数据
        requestLeaveList(url);
    }

    private void initViews() {
        setSupportActionBar(findViewById(R.id.toolbar));
        recyclerView = findViewById(R.id.rv_leave_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.refresh);
        emptyView = findViewById(R.id.empty_view);
        ftbn_leave=findViewById(R.id.ftbn_leave);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    /**
     * @param url 拼接好的url
     *            此方法请求请假条数据
     */
    private void requestLeaveList(String url) {
        // 设置loading状态
        swipeRefreshLayout.setRefreshing(true);
        // 发送get请求
        sendGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                    if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                        // 转换json为 List<Leave>

                        leaveList.clear();

                        leaveList.addAll(new Gson().fromJson(restResponse.getPayload().toString(), new TypeToken<List<Leave>>() {
                        }.getType()));

                        // 将数据排序
                        Collections.sort(leaveList, (leave1, leave2) -> {
                            Integer id1 = leave1.getStatus();
                            Integer id2 = leave2.getStatus();
                            return id1.compareTo(id2);
                        });

                        // 将List展示到界面上，可用ListView 或者 RecyclerView
                        // 这里以RecyclerView为例


                        runOnUiThread(() -> {
                            // leaveList为空则代表没有任何请假条纪录
                            if (leaveList.isEmpty())
                                emptyView.setVisibility(View.VISIBLE);
                            if (adapter == null) {
                                adapter = new HomeCourseMoreLeaveListAdapter(HomeCourseMoreLeaveListActivity.this, leaveList);
                                recyclerView.setAdapter(adapter);
                            } else {
                                log(THIS_CLASS, "测试点");
                                adapter.notifyDataSetChanged();
                            }
                            // 撤销loading状态
                            swipeRefreshLayout.setRefreshing(false);
                            // 统计数量
                            int wait = 0, fail = 0, success = 0;
                            for (Leave leave : leaveList) {
                                switch (leave.getStatus()) {
                                    case 1:
                                        wait++;
                                        break;
                                    case 2:
                                        fail++;
                                        break;
                                    case 3:
                                        success++;
                                        break;
                                    default:

                                }
                            }
                            getSupportActionBar().setTitle("有" + wait + "条正在处理");

                            adapter.setItemClickListener((v, position) -> {
                                Intent intent = new Intent(HomeCourseMoreLeaveListActivity.this, HomeCourseMoreLeaveShowActivity.class);
                                intent.putExtra("leave", leaveList.get(position));
                                startActivity(intent);
                                // 处理单击事件
                            });
                            adapter.setItemLongClickListener((view, position) -> {
                                // 处理长按事件
                            });
                        });
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
