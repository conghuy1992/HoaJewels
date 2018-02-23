package com.hoa.jewels;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.Line;
import com.google.gson.Gson;
import com.hoa.jewels._class.Const;
import com.hoa.jewels.adapters.SpinnerAdapter;
import com.hoa.jewels.barcode.BarcodeCaptureActivity;
import com.hoa.jewels.dto.ChangeUserDto;
import com.hoa.jewels.dto.GetRoleUserChangeDto;
import com.hoa.jewels.dto.LoginDto;
import com.hoa.jewels.dto.OrderInforDto;
import com.hoa.jewels.dto.UpdateOrderDto;
import com.hoa.jewels.dto.UserDto;
import com.hoa.jewels.interfaces.LoginCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;

    private TextView mResultTextView;
    private EditText edPassword;
    private EditText edEmail;
    private FrameLayout frSignUp;
    private RelativeLayout login_layout, infor_layout;
    private Context context;
    private ProgressBar progressBar;
    private LoginDto dto;
    private OrderInforDto orderInforDto;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
    }

    void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        frSignUp.setEnabled(false);
    }

    void hideLoading() {
        progressBar.setVisibility(View.GONE);
        frSignUp.setEnabled(true);
    }

    void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        edEmail = (EditText) findViewById(R.id.edEmail);
        edPassword = (EditText) findViewById(R.id.edPassword);

        frSignUp = (FrameLayout) findViewById(R.id.frSignUp);
        frSignUp.setOnClickListener(this);

        login_layout = (RelativeLayout) findViewById(R.id.login_layout);
        infor_layout = (RelativeLayout) findViewById(R.id.infor_layout);
    }

    @Override
    public void onClick(View v) {
        if (v == frSignUp) {
            actionSignUp();
        }
    }

    void doScanner() {
//        Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
        Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }


    private void actionSignUp() {

        String username = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString();

        if (username.length() == 0) {
            Const.showMsg(context, context.getResources().getString(R.string.not_username));
        } else if (password.length() == 0) {
            Const.showMsg(context, context.getResources().getString(R.string.not_password));
        } else {
            showLoading();
            String url = Const.Login(username, password);
            Const.CallAPI(context, url, new LoginCallback() {
                @Override
                public void onSuccess(String response) {
                    hideLoading();
                    Log.d(LOG_TAG, "onSuccess:" + response);
                    if (response.startsWith("{")) {
                        dto = new Gson().fromJson(response, LoginDto.class);
                        if (dto != null && dto.getStatus().trim().equals(Const.Success)) {
                            isLogin = true;
                            doScanner();
                            login_layout.setVisibility(View.GONE);
                            infor_layout.setVisibility(View.VISIBLE);
                        } else {
                            // fail
                            Const.showMsg(context, context.getResources().getString(R.string.login_fail));
                        }
                    } else {
                        // fail
                        Const.showMsg(context, context.getResources().getString(R.string.login_fail));
                    }

                }

                @Override
                public void onFail() {
                    hideLoading();
                    Const.showMsg(context, context.getResources().getString(R.string.login_fail));
                    Log.d(LOG_TAG, "onFail");
                }
            });

        }
    }

    //    void scannerQR() {
//        mResultTextView = (TextView) findViewById(R.id.result_textview);
//
//        Button scanBarcodeButton = (Button) findViewById(R.id.scan_barcode_button);
//        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
//                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
//            }
//        });
//    }

    void actionChangeUser(final int index, final List<UserDto> ListUser, final FrameLayout frConfirm, final Dialog dialogChangeUser, final Dialog dialogCallback) {
        final String username_change = ListUser.get(index).getUsername().trim();

        if (orderInforDto != null) {
            String url = Const.update_order_role(orderInforDto.getOrderSimple().getOrderID(), dto.getUser().getID(), username_change);
            Const.CallAPI(context, url, new LoginCallback() {
                @Override
                public void onSuccess(String response) {
                    frConfirm.setEnabled(true);
                    Log.d(LOG_TAG, "actionChangeUser:" + response);
                    if (response.startsWith("{")) {
                        ChangeUserDto changeUserDto = new Gson().fromJson(response, ChangeUserDto.class);
                        if (changeUserDto != null && changeUserDto.getStatus().trim().equals(Const.Success)) {
                            dto.getUser().setID(ListUser.get(index).getUID());
                            dto.getUser().setUsername(username_change);
                            Const.showMsg(context, context.getResources().getString(R.string.change_user_success));
                            Log.d(LOG_TAG, "actionChangeUser onSuccess");
                            dialogChangeUser.dismiss();
                            dialogCallback.dismiss();
                            doScanner();
                        } else {
                            Log.d(LOG_TAG, "changeUserDto null");
                            Const.showMsg(context, context.getResources().getString(R.string.change_user_fail));
                        }
                    } else {
                        Log.d(LOG_TAG, "dont start with {");
                        Const.showMsg(context, context.getResources().getString(R.string.change_user_fail));
                    }
                }

                @Override
                public void onFail() {
                    frConfirm.setEnabled(true);
                    Const.showMsg(context, context.getResources().getString(R.string.change_user_fail));
                }
            });
        } else {
            doScanner();
        }

    }

    int index = 0;

    void dialogChangeId(final Dialog dialogCallback, final List<UserDto> ListUser) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.change_id_layout);


        SpinnerAdapter adapter = new SpinnerAdapter(context, ListUser);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                Log.d(LOG_TAG, "getUID:" + ListUser.get(position).getUID());
                Log.d(LOG_TAG, "getUsername:" + ListUser.get(position).getUsername());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        final EditText edChangUser = (EditText) dialog.findViewById(R.id.edChangUser);

        final FrameLayout frConfirm = (FrameLayout) dialog.findViewById(R.id.frConfirm);
        frConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frConfirm.setEnabled(false);
                actionChangeUser(index, ListUser, frConfirm, dialog, dialogCallback);
            }
        });
        dialog.show();
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    void dialogCallback(int requestCode, int resultCode, Intent data) {
        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_callback_layout);

        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);

        final ImageView ivJewels = (ImageView) dialog.findViewById(R.id.ivJewels);

        final LinearLayout frRequestSuccess = (LinearLayout) dialog.findViewById(R.id.frRequestSuccess);

        final FrameLayout frRequestFail = (FrameLayout) dialog.findViewById(R.id.frRequestFail);
        final FrameLayout frScanAgain = (FrameLayout) dialog.findViewById(R.id.frScanAgain);
        final FrameLayout frSignUp = (FrameLayout) dialog.findViewById(R.id.frSignUp);
        final FrameLayout frChangId = (FrameLayout) dialog.findViewById(R.id.frChangId);

        frScanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                doScanner();
            }
        });


        frSignUp.setEnabled(false);
        frChangId.setEnabled(false);


        //frSignUp click
        frSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frSignUp.setEnabled(false);
                confirm(frSignUp, dialog);
            }
        });

        //frChangId click
        frChangId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frChangId.setEnabled(false);
//                dialogChangeId(dialog);

                String url = Const.get_role_userchange(dto.getUser().getID());
                Const.CallAPI(context, url, new LoginCallback() {
                    @Override
                    public void onSuccess(String response) {
                        frChangId.setEnabled(true);
                        Log.d(LOG_TAG, "response:" + response);
                        GetRoleUserChangeDto getRoleUserChangeDto = new Gson().fromJson(response, GetRoleUserChangeDto.class);
                        if (response.startsWith("{")) {
                            if (getRoleUserChangeDto != null && getRoleUserChangeDto.getStatus().trim().equals(Const.Success)) {
                                if (getRoleUserChangeDto.getListUser() != null && getRoleUserChangeDto.getListUser().size() > 0) {
                                    Log.d(LOG_TAG, "get_role_userchange success");

                                    dialogChangeId(dialog, getRoleUserChangeDto.getListUser());
                                } else {
                                    Log.d(LOG_TAG, "getRoleUserChangeDto getListUser null");
                                    Const.showMsg(context, context.getResources().getString(R.string.get_role_userchange));
                                }
                            } else {
                                Log.d(LOG_TAG, "getRoleUserChangeDto null");
                                Const.showMsg(context, context.getResources().getString(R.string.get_role_userchange));
                            }
                        } else {
                            Log.d(LOG_TAG, "getRoleUserChangeDto dont start with {");
                            Const.showMsg(context, context.getResources().getString(R.string.get_role_userchange));
                        }
                    }

                    @Override
                    public void onFail() {
                        frChangId.setEnabled(true);
                        Const.showMsg(context, context.getResources().getString(R.string.get_role_userchange));
                    }
                });
            }
        });


        boolean flag = false; // if true -> get barcode success
        if (resultCode == CommonStatusCodes.SUCCESS) {
            if (data != null) {
//                Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                    Point[] p = barcode.cornerPoints;
//                tvCode.setText(barcode.displayValue);
//                String code = barcode.displayValue;

                String code = data.getStringExtra(Const.KEY_BARCODE);
                if (code == null) code = "";
                Log.d(LOG_TAG, "barcode:" + code);


//                for test
//                code = "12a4e89q52"; // phoi1
//                code = "4c8dw61r5w"; // phoi2
                progressBar.setVisibility(View.VISIBLE);
                String url = Const.Get_Order_info(code, dto.getUser().getID());
                Const.CallAPI(context, url, new LoginCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d(LOG_TAG, "response:" + response);
                        progressBar.setVisibility(View.GONE);
                        if (response.startsWith("{")) {
                            orderInforDto = new Gson().fromJson(response, OrderInforDto.class);
                            if (orderInforDto != null && orderInforDto.getStatus().trim().equals(Const.Success)) {
                                frSignUp.setEnabled(true);
                                frChangId.setEnabled(true);
                                Glide.with(context)
                                        .load(orderInforDto.getOrderSimple().getOrderQRIMG())
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(new GlideDrawableImageViewTarget(ivJewels) {
                                            @Override
                                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                ivJewels.setImageResource(R.mipmap.ic_launcher);
                                            }
                                        });
                            } else {
                                Log.d(LOG_TAG, "orderInforDto null");
                                Const.showMsg(context, context.getResources().getString(R.string.get_order_infor_fail));
                                frRequestFail.setVisibility(View.VISIBLE);
                                frRequestSuccess.setVisibility(View.GONE);
                            }
                        } else {
                            Log.d(LOG_TAG, "dont startsWith {");
                            Const.showMsg(context, context.getResources().getString(R.string.get_order_infor_fail));
                            frRequestFail.setVisibility(View.VISIBLE);
                            frRequestSuccess.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFail() {
                        Log.d(LOG_TAG, "onFail");
                        progressBar.setVisibility(View.GONE);
                        frRequestFail.setVisibility(View.VISIBLE);
                        frRequestSuccess.setVisibility(View.GONE);
                        Const.showMsg(context, context.getResources().getString(R.string.get_order_infor_fail));
                    }
                });

                flag = true;
            } else {
                frRequestFail.setVisibility(View.VISIBLE);
                frRequestSuccess.setVisibility(View.GONE);
                Const.showMsg(context, getResources().getString(R.string.no_barcode_captured));
            }
        } else if (resultCode == Const.ON_BACK) {
            finish();
        } else {
            frRequestFail.setVisibility(View.VISIBLE);
            frRequestSuccess.setVisibility(View.GONE);
            String msg = String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode));
            Log.e(LOG_TAG, msg);
            Const.showMsg(context, msg);
        }

        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    return true;
                    dialog.dismiss();
                    doScanner();
                }
                return false;
            }
        });
    }

    void confirm(final FrameLayout frSignUp, final Dialog dialog) {
        if (orderInforDto != null) {
            // call api confirm
            String url = Const.update_order(orderInforDto.getOrderSimple().getOrderID(), dto.getUser().getID());
            Const.CallAPI(context, url, new LoginCallback() {
                @Override
                public void onSuccess(String response) {
                    frSignUp.setEnabled(true);
                    Log.d(LOG_TAG, "update_order:" + response);
                    if (response.startsWith("{")) {
                        UpdateOrderDto updateOrderDto = new Gson().fromJson(response, UpdateOrderDto.class);
                        if (updateOrderDto != null && updateOrderDto.getStatus().trim().equals(Const.Success)) {
                            // update_order success
                            Log.d(LOG_TAG, "update_order success");
                            Const.showMsg(context, context.getResources().getString(R.string.update_order_success));
                            dialog.dismiss();
                            doScanner();

                        } else {
                            Const.showMsg(context, context.getResources().getString(R.string.update_order_fail));
                        }
                    } else {
                        Log.d(LOG_TAG, "dont start with {");
                        Const.showMsg(context, context.getResources().getString(R.string.update_order_fail));
                    }
                }

                @Override
                public void onFail() {
                    frSignUp.setEnabled(true);
                    Log.d(LOG_TAG, "onFail");
                    Const.showMsg(context, context.getResources().getString(R.string.update_order_fail));
                }
            });
        } else {
            // scan again
            doScanner();
        }


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (!isLogin) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {

            dialogCallback(requestCode, resultCode, data);

//            if (resultCode == CommonStatusCodes.SUCCESS) {
//                if (data != null) {
//                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
////                    Point[] p = barcode.cornerPoints;
////                    mResultTextView.setText(barcode.displayValue);
//                    Const.showMsg(context, barcode.displayValue);
//                } else
//                    Const.showMsg(context, getResources().getString(R.string.no_barcode_captured));
//            } else Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
//                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }
}