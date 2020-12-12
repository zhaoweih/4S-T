package com.zhaoweihao.architechturesample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.course.SendNoti;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewClickListener;
import com.zhaoweihao.architechturesample.interfaze.OnRecyclerViewLongClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeCourseMoreCourseNotiQueryAdapter extends RecyclerView.Adapter<HomeCourseMoreCourseNotiQueryAdapter.QueryViewHolder>{
    public static final String TAG = "HomeCourseMoreCourseNotiQueryAdapter";

    private final Context context;
    private LayoutInflater inflater;
    private final List<SendNoti> list;
    private final Boolean checkTecOrStu;
    private final Date todayDate=new Date();

    private OnRecyclerViewClickListener listener;
    private OnRecyclerViewLongClickListener longClickListener;
    public HomeCourseMoreCourseNotiQueryAdapter(Context context, ArrayList<SendNoti> list, Boolean checkTecOrStu) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
        this.checkTecOrStu = checkTecOrStu;
    }
    @Override
    public HomeCourseMoreCourseNotiQueryAdapter.QueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeCourseMoreCourseNotiQueryAdapter.QueryViewHolder(inflater.inflate(R.layout.query_noti_list_layout,parent,false),listener,longClickListener);
    }

    @Override
    public void onBindViewHolder(HomeCourseMoreCourseNotiQueryAdapter.QueryViewHolder holder, int position) {
        com.zhaoweihao.architechturesample.bean.course.SendNoti query = list.get(position);
        //if (checkTecOrStu)

        String string = query.getEndDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String todayString=sdf.format(todayDate);
            if(sdf.parse(todayString).after(sdf.parse(string))){
                holder.ry_query_noti_list.setVisibility(View.GONE);
                Log.v("tringModelImpl","hhhh"+query.getEndDate()+"hhh"+query.getDate()+"hhh"+position+"****");
                //list.remove(position);
               /* holder.tv_query_noti_list_endDate.setVisibility(View.GONE);
                holder.tv_query_noti_list_content.setVisibility(View.GONE);
                holder.tv_query_noti_list_date.setVisibility(View.GONE);*/
            }else {
                holder.tv_query_noti_list_endDate.setText(query.getEndDate());
                holder.tv_query_noti_list_content.setText(query.getContent());
                holder.tv_query_noti_list_date.setText(query.getDate());
            }
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();

        }


        //holder.iv_query_select_course_manage.setVisibility(View.VISIBLE);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClickListener(OnRecyclerViewClickListener listener) {
        this.listener = listener;
    }

    public void setItemLongClickListener(OnRecyclerViewLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public class QueryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        //TextView tv_query_select_course_id,tv_query_select_course_name,tv_query_select_course_teachername;
        TextView tv_query_noti_list_content,tv_query_noti_list_endDate,tv_query_noti_list_date;
        RelativeLayout ry_query_noti_list;
        ImageView iv_query_select_course_manage;

        OnRecyclerViewClickListener listener;
        OnRecyclerViewLongClickListener longClickListener;

        public QueryViewHolder(View itemView, OnRecyclerViewClickListener listener, OnRecyclerViewLongClickListener longClickListener) {
            super(itemView);

           // tv_query_select_course_id = itemView.findViewById(R.id.tv_query_select_course_id);
            //tv_query_select_course_name = itemView.findViewById(R.id.tv_query_select_course_name);
           // tv_query_select_course_teachername = itemView.findViewById(R.id.tv_query_select_course_teachername);
            //iv_query_select_course_manage= itemView.findViewById(R.id.iv_query_select_course_manage);去掉图片
            tv_query_noti_list_content=itemView.findViewById(R.id.tv_query_noti_list_content);
            tv_query_noti_list_endDate=itemView.findViewById(R.id.tv_query_noti_list_endDate);
            tv_query_noti_list_date=itemView.findViewById(R.id.tv_query_noti_list_date);
            ry_query_noti_list=itemView.findViewById(R.id.ry_query_noti_list);

            this.listener = listener;
            itemView.setOnClickListener(this);
            this.longClickListener = longClickListener;
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.OnClick(view, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (longClickListener != null) {
                longClickListener.OnLongClick(view, getLayoutPosition());
            }
            return true;
        }
    }
}
