package com.zhaoweihao.architechturesample.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhaoweihao.architechturesample.bean.OnStringListener;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.StringModelImpl;
import com.zhaoweihao.architechturesample.bean.course.QueryComment;
import com.zhaoweihao.architechturesample.contract.HomeCourseTaskTalkTopicCommentContract;
import com.zhaoweihao.architechturesample.database.User;
import com.zhaoweihao.architechturesample.util.Constant;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.zhaoweihao.architechturesample.util.HttpUtil.sendPostRequest;

public class HomeCourseTaskTalkTopicCommentPresenter implements HomeCourseTaskTalkTopicCommentContract.Presenter, OnStringListener {
    public static final String TAG = "HomeCourseTaskTalkTopicCommentPresenter";

    private HomeCourseTaskTalkTopicCommentContract.View view;
    private Context context;
    private StringModelImpl model;
    private ArrayList<QueryComment> queryList = new ArrayList<>();

    public HomeCourseTaskTalkTopicCommentPresenter(Context context, HomeCourseTaskTalkTopicCommentContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModelImpl(context);
    }

    @Override
    public void start() {

    }


    @Override
    public void onSuccess(String payload) {

        Log.e(TAG, payload);
        if (payload == null) {
            view.showConfirmSuccess(true);
            view.stopLoading();
            return;
        }
        try {
            List<QueryComment> QueryComments = new Gson().fromJson(payload, new TypeToken<List<QueryComment>>() {
            }.getType());
            queryList.clear();
            queryList.addAll(new Gson().fromJson(payload, new TypeToken<List<QueryComment>>() {
            }.getType()));
            view.showResult(queryList);
            view.stopLoading();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            view.showLoadError(e.toString());
            view.stopLoading();
        }
    }

    @Override
    public void onError(String error) {
        view.showLoadError(error);
        view.stopLoading();
    }

    @Override
    public void QueryComment(String url) {
        view.startLoading();
        model.sentGetRequestInSMI(url, this);

    }
    @Override
    public void sendComment(String url,String json,String urlRefresh) {
        view.startLoading();
        sendPostRequest(url, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //网络错误处理
                return;
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                //解析json数据组装RestResponse对象
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse.getCode() == Constant.FAIL_ORIGIN_CODE) {
                }
                // code 200等于登录成功
                if (restResponse.getCode() == Constant.SUCCESS_CODE) {
                   QueryComment(urlRefresh);
                }
            }
        });
    }

    @Override
    public ArrayList<QueryComment> getQueryList() {
        return queryList;
    }

    @Override
    public Boolean checkTecOrStu() {
        User user = DataSupport.findLast(User.class);
        if ( user == null ) {
            return false;
        }

        if ( user.getStudentId() != null) {
            return true;
        }

        if ( user.getTeacherId() != null) {
            return false;
        }

        return false;

    }


}
