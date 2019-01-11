package com.mabble.notificationdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MobileActivity extends AppCompatActivity {

    EditText et_mobilenumber;
    Button btn_registermobile;
    String mobile;
    String fcm_id;
    String MobilePattern = "[0-9]{10}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set Title in edittext...
        setContentView(R.layout.activity_mobile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        et_mobilenumber = findViewById(R.id.et_mobilenumber);
        btn_registermobile = findViewById(R.id.btn_registermobile);
        fcm_id = FirebaseInstanceId.getInstance().getToken();
        btn_registermobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = et_mobilenumber.getText().toString();


                // Validation for phone number edittext...
                if (mobile.equals("")) {
                    Toast.makeText(MobileActivity.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else {
                    //sendData();
                    Toast.makeText(MobileActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(MobileActivity.this, MainActivity.class));
                }
            }
        });
    }

    //function called when phonenumber  is added...
    public void sendData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.14/experiment/firebase/config.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //after response...

                        Log.i("prerna : responce", "" + response.toString());
                        //Toast.makeText(MobileActivity.this, "" + response.toString(), Toast.LENGTH_SHORT).show();

                     /*   try {
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("");

                            String msz=jsonArray.getJSONObject(i).getString("status");
                            //OR
                            String mszz=jsonObject.getString("status");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/




                       /* if (response.contains("success")) {
                            Toast.makeText(MobileActivity.this, "Registered Successfully!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MobileActivity.this, "Something  went wrong", Toast.LENGTH_LONG).show();
                        }*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //error messege...
                        Toast.makeText(MobileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(MobileActivity.this, "Slow internet,Please Try Again", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                //send data in backand...
                Map<String, String> params = new HashMap<String, String>();
                Log.i("prerna : data", fcm_id + "..." + mobile);
                params.put("ID", fcm_id);
                params.put("MobileNumber", mobile);
                return params;
            }
        };


        //for timeout handling...
        RequestQueue requestQueue = Volley.newRequestQueue(MobileActivity.this);
        int scoket = 30000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(scoket, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
    }

}
