package com.fjdynamics.fjkey.view.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.fjdynamics.fjkey.R;
import com.fjdynamics.fjkey.base.BaseApplication;
import com.fjdynamics.fjkey.bean.Record;
import com.google.gson.Gson;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import org.json.JSONArray;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Administrator QQ:824679118
 * @class name：com.fjdynamics.fjkey.view.activity
 * @time 2019/3/17 12:58
 * @class describe
 */
public class UserHistory extends Activity implements View.OnClickListener {

    private static String TAG = "keven";

    private RecyclerView recyclerView;
    RecyclerviewAdapter adapter;
    private LoadingDialog ld;   // 显示正在加载的对话框
    private static List<Record> listData = new ArrayList<>();
    private EditText etTime;
    private Button btnSearch;
    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = (RecyclerView) findViewById(R.id.rv_history);
        etTime = (EditText) findViewById(R.id.et_time);

        long timeStamp = System.currentTimeMillis();
        Date date = new Date(timeStamp);
        etTime.setText(df.format(date));


        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);

        adapter = new RecyclerviewAdapter(this, listData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                ocRequest(etTime.getText().toString());
        }

    }

    public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {

        private Context context;
        private List<Record> listData;

        public RecyclerviewAdapter(Context context, List<Record> listData) {
            this.context = context;
            this.listData = listData;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.time.setText(listData.get(position).getTime().replace("T", " ").replace(".000+0000", ""));
            holder.name.setText(listData.get(position).getOpenName());
            holder.state.setText("1".equals(listData.get(position).getStatus()) ? "开锁" : "关锁");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView time;
            private TextView name;
            private TextView state;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.tv_user);
                time = (TextView) itemView.findViewById(R.id.tv_time);
                state = (TextView) itemView.findViewById(R.id.tv_state);
            }
        }
    }

    private void ocRequest(final String date) {

        ld = new LoadingDialog(UserHistory.this);
        ld.setLoadingText("加载中...");
        ld.setSuccessText("加载成功");
        ld.setFailedText("加载失败");

        ld.show();

        Request request = new Request.Builder()
                .url(BaseApplication.getStateUrl + "?date=" + date)
                .build();

        BaseApplication.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "getState exception: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ld.setFailedText("网络异常");
                        ld.loadFailed();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBodyData = response.body().string();
                Record[] array = new Gson().fromJson(responseBodyData, Record[].class);
                List<Record> list = Arrays.asList(array);
                listData.clear();
                for (Record l : list) {
                    listData.add(l);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ld.loadSuccess();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

}
