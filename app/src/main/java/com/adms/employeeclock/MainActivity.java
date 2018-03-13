package com.adms.employeeclock;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.adms.employeeclock.Utility.ApiHandler;
import com.adms.employeeclock.Utility.Util;
import com.adms.employeeclock.databinding.ActivityMainBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;
    Activity mContext;
    String qrCodeStr, qyCodeNameStr;
    DecoratedBarcodeView barcodeView;
    ImageView barcode_imag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mContext = MainActivity.this;
        init();

    }

    public void init() {
        boolean result = Util.checkPermission(MainActivity.this);
        if (result) {

        } else {
            Util.ping(mContext, "Please Allow Camera Permission");
        }
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.zxing);
        barcode_imag = (ImageView) findViewById(R.id.barcode_imag);
        barcodeView.setStatusText("Scan your QR-Code");
        barcodeView.decodeSingle(callback);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result != null) {
                //if qrcode has nothing in it
                if (result.getText() == null) {
                    Toast.makeText(MainActivity.this, "QR-Code Not Match", Toast.LENGTH_LONG).show();
                } else {
//                    //if qr contains data
                    try {
//                  //converting the data to json
                        JSONObject obj = new JSONObject(result.getText());

                        qrCodeStr = obj.getString("qrcode");
//                        qyCodeNameStr=obj.getString("name");
//                        Log.d("Response","Code:"+qrCodeStr+"Name:"+qyCodeNameStr);
                        if (!qrCodeStr.equalsIgnoreCase("")) {
                            callEmployeeApi();
                        } else {
                            Util.ping(mContext, "QR-Code not Match. Try Again...");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.barcode_imag:
                barcodeView.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
                break;

            case R.id.continue_btn:
                recreate();
                break;

            case R.id.close_btn:
                finish();
                break;
        }

    }

    //Getting the scan results
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null) {
//            //if qrcode has nothing in it
//            if (result.getContents() == null) {
//                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
//            } else {
////                mainBinding.btnBarcodeScan.setVisibility(View.GONE);
////                mainBinding.
//                mainBinding.rightClick.setVisibility(View.VISIBLE);
//                mainBinding.empoloyeeName.setVisibility(View.VISIBLE);
//                //if qr contains data
//                try {
////                    //converting the data to json
//                    JSONObject obj = new JSONObject(result.getContents());
////                    //setting values to textviews
//                    qrCodeStr = obj.getString("qrcode");
//                    mainBinding.empoloyeeName.setText(obj.getString("name"));
//                    Toast.makeText(MainActivity.this, "EmpoyName:" + result.getContents(), Toast.LENGTH_LONG).show();
////                    callEmployeeApi();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    //if control comes here
//                    //that means the encoded format not matches
//                    //in this case you can display whatever data is available on the qrcode
//                    //to a toast
//                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
//                }
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    // CALL Staff Attendace API HERE
    private void callEmployeeApi() {

        if (!Util.checkNetwork(mContext)) {
            Util.showCustomDialog(getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), mContext);
            return;
        }

//        Util.showDialog(mContext);
        ApiHandler.getApiService().getEmpolyeeAttendace(getemployeeDetail(), new retrofit.Callback<EmployeeModel>() {

            @Override
            public void success(EmployeeModel employeeModel, Response response) {
                Util.dismissDialog();
                if (employeeModel == null) {
                    Util.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (employeeModel.getSuccess() == null) {
                    Util.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (employeeModel.getSuccess().equalsIgnoreCase("False")) {
                    Util.dismissDialog();
                    Util.ping(mContext, getString(R.string.false_msg));
                    recreate();
                    return;
                }
                if (employeeModel.getSuccess().equalsIgnoreCase("True")) {
                    Util.dismissDialog();

//                    if (employeeModel.getEmployeeName().equalsIgnoreCase(qyCodeNameStr)) {
                    barcodeView.setVisibility(View.GONE);
                    barcode_imag.setVisibility(View.GONE);
                    mainBinding.continueBtn.setVisibility(View.VISIBLE);
                    mainBinding.closeBtn.setVisibility(View.VISIBLE);
                    mainBinding.rightClick.setVisibility(View.VISIBLE);
                    mainBinding.empoloyeeName.setVisibility(View.VISIBLE);
                    mainBinding.empoloyeeName.setText(employeeModel.getEmployeeName());
//                    } else {
//                        Util.ping(mContext,"Not Match");
//                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Util.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                Util.ping(mContext, getString(R.string.something_wrong));
            }
        });

    }

    private Map<String, String> getemployeeDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("QRCode", qrCodeStr);
        return map;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

}
