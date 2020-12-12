package sviolet.seatselectionview.demo;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.bean.RestResponse;
import com.zhaoweihao.architechturesample.bean.seat.Create;
import com.zhaoweihao.architechturesample.bean.seat.Record;
import com.zhaoweihao.architechturesample.bean.seat.SeatSel;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import sviolet.seatselectionview.view.Seat;
import sviolet.seatselectionview.view.SeatSelectionView;
import sviolet.seatselectionview.view.SelectedSeats;

import static com.zhaoweihao.architechturesample.util.HttpUtil.*;

/**
 * 选中座位控制器强化, 增加对底部栏的刷新
 *
 * Created by S.Violet on 2016/12/6.
 */
public class MySelectedSeats extends SelectedSeats {

    private static final String TAG = "MySelectedSeats";

    private AuditoriumInfo auditoriumInfo;
    private View bottomBar;
    private LinearLayout selectedItemContainer;
    private TextView totalPriceTextView;
    private TextView priceDetailTextView;
    private List<View> selectedItemViews;
    private List<TextView> selectedItemTextViews;
    private Button seatSelectionButton;



    private Animation bottomBarInAnimation;//底部栏动画
    private Animation bottomBarOutAnimation;//底部栏动画

    private SeatSel seatSel;
    private String classCode;
    private SeatSelectionActivity seatSelectionActivity;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Context context;

    private boolean isBottomBarShown = false;

    public MySelectedSeats(SeatSelectionView seatSelectionView, AuditoriumInfo auditoriumInfo,
                           Context context, View bottomBar, LinearLayout selectedItemContainer, TextView totalPriceTextView, TextView priceDetailTextView, Button seatSelectionButton, SeatSel seatSel, String classCode, SwipeRefreshLayout swipeRefreshLayout, SeatSelectionActivity seatSelectionActivity) {

        super(seatSelectionView, auditoriumInfo.getMaxSeatNum());

        if (bottomBar == null){
            throw new RuntimeException("[MySelectedSeats]bottomBar is null");
        }
        if (selectedItemContainer == null){
            throw new RuntimeException("[MySelectedSeats]selectedItemContainer is null");
        }
        if (totalPriceTextView == null){
            throw new RuntimeException("[MySelectedSeats]totalPriceTextView is null");
        }
        if (priceDetailTextView == null){
            throw new RuntimeException("[MySelectedSeats]priceDetailTextView is null");
        }

        this.auditoriumInfo = auditoriumInfo;
        this.bottomBar = bottomBar;
        this.selectedItemContainer = selectedItemContainer;
        this.totalPriceTextView = totalPriceTextView;
        this.priceDetailTextView = priceDetailTextView;
        this.seatSelectionButton = seatSelectionButton;
        this.selectedItemViews = new ArrayList<>(auditoriumInfo.getMaxSeatNum());
        this.selectedItemTextViews = new ArrayList<>(auditoriumInfo.getMaxSeatNum());

        this.seatSel = seatSel;
        this.classCode = classCode;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.context = context;
        this.seatSelectionActivity = seatSelectionActivity;



        //初始化动画
        bottomBarInAnimation = AnimationUtils.loadAnimation(context, R.anim.seat_selection_bottom_bar_in);//加载
        bottomBarOutAnimation = AnimationUtils.loadAnimation(context, R.anim.seat_selection_bottom_bar_out);//加载

        //底部栏当前是否显示
        isBottomBarShown = bottomBar.getVisibility() == View.VISIBLE;

        //实例化底部栏的选中项View
        LayoutInflater inflater = LayoutInflater.from(context);
        for (int i = 0 ; i < auditoriumInfo.getMaxSeatNum() ; i++){
            View itemView = inflater.inflate(R.layout.seat_selection_bottom_bar_item, null);
            TextView textView = (TextView) itemView.findViewById(R.id.seat_selection_bottom_bar_item);
            itemView.setTag(i);
            itemView.setOnClickListener(onSelectedItemClickListener);
            selectedItemViews.add(itemView);
            selectedItemTextViews.add(textView);
        }
    }

    @Override
    public boolean onSelect(Seat seat) {
        boolean result = super.onSelect(seat);
        //刷新数据
        if (result) {
            refreshBottomBar();
        }
        return result;
    }

    @Override
    public boolean onDeselect(Seat seat) {
        boolean result = super.onDeselect(seat);
        //刷新数据
        refreshBottomBar();
        return result;
    }

    public void refreshBottomBar(){
        refreshBottomBarSelectedItems();
        refreshBottomBarPrice();

    }

    public void refreshBottomBarSelectedItems(){
        Record record = new Record();

        record.setClassCode(classCode);

        User user = DataSupport.findLast(User.class);
        if (user == null) {
            return;
        }
        if (user.getStudentId() == null) {
            Toast.makeText(seatSelectionActivity, "你不是学生", Toast.LENGTH_SHORT).show();
            return;
        }
        record.setStudentId(user.getStudentId());

        //先移除全部View
        selectedItemContainer.removeAllViews();
        if (getSeatNum() > getMaxSeatNum()){
            throw new RuntimeException("[MySelectedSeats]size of selected seats is larger than max");
        }
        //根据座位情况塞入View
        for (int i = 0 ; i < getSeatNum() ; i++){
            Seat seat = getSeat(i);
            selectedItemTextViews.get(i).setText(seat.getRowId() + "排" + seat.getColumnId() + "座");
            selectedItemContainer.addView(selectedItemViews.get(i));
            seatSelectionButton.setOnClickListener(null);
            seatSelectionButton.setOnClickListener(v -> {
                record.setClassColumn(Integer.valueOf(seat.getColumnId()));
                record.setClassRow(Integer.valueOf(seat.getRowId()));
                //这里执行确认选位后的操作
                for (SeatSel.AddRowBean addRowBean: seatSel.getAddRow()) {
                    if (addRowBean.getRowId().equals(seat.getRowId())) {
                       String[] columnStatesArray = addRowBean.getColumnStates().split("\\|");
                       int same = 0;
                       for (String c : columnStatesArray) {
                           if (c.equals("A")) {
                               break;
                           } else {
                               same = same + 1;
                           }
                       }
                       columnStatesArray[Integer.valueOf(seat.getColumnId()) - 1 + same] = "U";
                       StringBuilder after = new StringBuilder("");
                       for (int j = 0; j < columnStatesArray.length; j++) {
                           if (j == 0) {
                              after.append(columnStatesArray[j]);
                           } else {
                               after.append("|");
                               after.append(columnStatesArray[j]);
                           }
                       }


                       String placeholder = after.toString();

                       // placeholder是拼装好的座位状态
                        addRowBean.setColumnStates(placeholder);

                        Create create = new Create();
                        create.setClassCode(classCode);
                        create.setSeatSel(seatSel);

                        String json = new Gson().toJson(create);
//                        String jsonString = new Gson().toJson(seatSel);

                        // 发送网络请求写入新的座位表
                        String suffix = "seat/update";
                        sendPostRequest(suffix, json, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String body = response.body().string();
                                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                                if (restResponse.getCode() == 200) {

                                    // 更新座位表成功，后续应该更新座位表
                                    seatSelectionActivity.runOnUiThread(() -> {
                                        Toast.makeText(seatSelectionActivity, "占位成功，请手动刷新查看", Toast.LENGTH_SHORT).show();
                                        addRecord(record);
                                    });
                                }
                            }
                        });

                    }
                }
            });
        }

        if (!isBottomBarShown && getSeatNum() > 0){
            //显示底边栏
            isBottomBarShown = true;
            bottomBar.startAnimation(bottomBarInAnimation);
            bottomBar.setVisibility(View.VISIBLE);
        } else if (isBottomBarShown && getSeatNum() <= 0){
            //隐藏底边栏
            isBottomBarShown = false;
            bottomBar.startAnimation(bottomBarOutAnimation);
            bottomBar.setVisibility(View.GONE);
            //清空监听器
            seatSelectionButton.setOnClickListener(null);
        }
    }

    private void addRecord(Record record) {
        String suffix = "seat/record/add";

        String json = new Gson().toJson(record);

        sendPostRequest(suffix, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                RestResponse restResponse = new Gson().fromJson(body, RestResponse.class);
                if (restResponse.getCode() == 200){
                    seatSelectionActivity.runOnUiThread(() -> {
                        Toast.makeText(seatSelectionActivity, "添加座位纪录成功", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    public void refreshBottomBarPrice(){
        int seatNum = getSeatNum();
        float totalPrice = seatNum * auditoriumInfo.getPrice();
        totalPriceTextView.setText("注意");
        priceDetailTextView.setText("提交后的座位不能更改");
    }

    private final View.OnClickListener onSelectedItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Seat seat = getSeat((int) v.getTag());
            //移除座位
            removeSeat(seat);
            //更新座位状态为未选中
            deselectSeat(seat);
            //刷新底部栏
            refreshBottomBar();
        }
    };

}
