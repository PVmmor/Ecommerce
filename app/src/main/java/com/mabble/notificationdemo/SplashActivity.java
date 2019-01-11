package com.mabble.notificationdemo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    boolean status = false;
    Boolean isFirstTime;
    //for permission
    String[] permissionsRequired = new String[]{Manifest.permission.CALL_PHONE};
    static final int PERMISSION_CALLBACK_CONSTANT = 100;
    static final int REQUEST_PERMISSION_SETTING = 101;
    SharedPreferences permissionStatus;
    boolean sentToSettings = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        status = checkInternetConnection();
        getPermissions();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                afterpermission();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                afterpermission();

            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Permissions");
                builder.setMessage(this.getString(R.string.app_name) + " app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SplashActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(this, "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean checkInternetConnection() {

        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] inf = connectivity.getAllNetworkInfo();
            if (inf != null) {
                for (int i = 0; i < inf.length; i++) {
                    return true;
                }
            }
        }
        return false;

    }

    public void getPermissions() {
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])) {
                //Show Information about why you need the permission

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Contacts and Location permissions.");

                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(SplashActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Multiple Permissio" +
                        "ns");
                builder.setMessage("This app needs Storage and settings permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage and settings permissions.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], false);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            afterpermission();

        }
    }


    //permission granted
    public void afterpermission() {
        if (status) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    //first time show only on installation......
                    SharedPreferences app_preferences = PreferenceManager
                            .getDefaultSharedPreferences(SplashActivity.this);
                    final SharedPreferences.Editor editor = app_preferences.edit();
                    isFirstTime = app_preferences.getBoolean("isFirstTime", true);
                    if (isFirstTime) {

                        //implement your first time logic......
                        editor.putBoolean("isFirstTime", false);
                        editor.commit();
                        startActivity(new Intent(SplashActivity.this, MobileActivity.class));
                        finish();

                    } else {

                        //app open directly from second time logic......
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                   /* Intent mainIntent = new Intent(SplashActivity.this, MobileActivity.class);
                    startActivity(mainIntent);
                    finish();*/
                }
            }, 5000);

        } else {
            Toast.makeText(getApplicationContext(), "You don't have Internet connection. Try Again", Toast.LENGTH_LONG).show();

        }
    }
}

