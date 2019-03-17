package com.fjdynamics.fjkey.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fjdynamics.fjkey.R;
import com.fjdynamics.fjkey.base.BaseApplication;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 主页面
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnOpen, btnClose;
    private TextView tvMsg ,tvHistory;
    private LinearLayout llOpen, llClose;
    private static final int REQUEST_CODE = 0x666;
    private static final String TAG = "MainActivity";
    private static String account, password;
    private LoadingDialog ld;   // 显示正在加载的对话框

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView() {
        btnOpen = (Button) findViewById(R.id.btn_open);
        btnOpen.setOnClickListener(this);
        btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        llOpen = (LinearLayout) findViewById(R.id.ll_open);
        llClose = (LinearLayout) findViewById(R.id.ll_close);

        tvMsg = (TextView) findViewById(R.id.tv_msg);
        tvHistory = (TextView) findViewById(R.id.tv_history);
        tvHistory.setOnClickListener(this);
//        Intent mIntent = getIntent();
//        account = mIntent.getStringExtra("account");
//        password = mIntent.getStringExtra("password");

        account = new LoginActivity().getLocalName();
        password = new LoginActivity().getLocalPassword();
        tvMsg.setText(account + "\n" + "欢迎使用");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open:
//                 扫码二维码功能
//                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
//                } else {
//                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                    startActivityForResult(intent, REQUEST_CODE);
//                }
                showNotice();
                break;
            case R.id.btn_close:
                new AlertDialog.Builder(this)
                        .setMessage("确定车钥匙已放规定位置？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                showConfirmWindowClose();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.tv_history:

                break;
            default:
                break;
        }
    }

    private void showConfirmWindowClose() {
        new AlertDialog.Builder(this)
                .setMessage("确定车窗已关？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showConfirmDoorClose();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create()
                .show();
    }


    private void showConfirmDoorClose() {
        new AlertDialog.Builder(this)
                .setMessage("确定车门已关？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showConfirmParking();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create()
                .show();
    }

    private void showConfirmParking() {
        new AlertDialog.Builder(this)
                .setMessage("确定车停放合规？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showConfirm();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create()
                .show();

    }

    private void showConfirm() {
        new AlertDialog.Builder(this)
                .setMessage("即将关锁，请确定？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ocRequest(false, "FJ666");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create()
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    // 拿到参数请求开门
                    ocRequest(true, result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void ocRequest(final boolean isOpen, String carId) {

        ld = new LoadingDialog(MainActivity.this);
        ld.setLoadingText(isOpen ? "开锁中..." : "关锁中...");
        ld.setSuccessText(isOpen ? "开锁成功" : "关锁成功");
        ld.setFailedText(isOpen ? "开锁失败" : "关锁失败");

        ld.show();

        RequestBody requestBody = new FormBody.Builder()
                .add("action",isOpen ? "FJ666_1":"FJ666_0")
                .add("userName" ,account)
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .url(BaseApplication.lockUrl)
                .build();

        BaseApplication.okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, isOpen ? "open" : "close" + "exception: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ld.loadFailed();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBodyData = response.body().string();
                Log.d(TAG, isOpen ? "open" : "close" + "success:" + responseBodyData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ld.loadSuccess();
                        if (isOpen) {
                            // 开锁成功则显示关锁按钮
                            llOpen.setVisibility(View.GONE);
                            llClose.setVisibility(View.VISIBLE);
                            tvMsg.setText(account + "\n" + "请安全驾驶");
                        } else {
                            // 开锁成功则显示关锁按钮
                            llClose.setVisibility(View.GONE);
                            llOpen.setVisibility(View.VISIBLE);
                            tvMsg.setText(account + "\n" + "欢迎使用");
                        }
                    }
                });
            }
        });
    }

    // 用户须知
    public void showNotice(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("电动车使用须知")
                .setMessage(R.string.notice)
                .setPositiveButton("继续开锁", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ocRequest(true, "FJ666");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create();

        dialog.show();

        try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = mAlert.get(dialog);
            Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
            mMessage.setAccessible(true);
            TextView mMessageView = (TextView) mMessage.get(mAlertController);
//            mMessageView.setTextColor(Color.BLUE);
            mMessageView.setTextSize(14);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
