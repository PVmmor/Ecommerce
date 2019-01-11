package com.mabble.notificationdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    WebView webview;
    String url = "https://www.shopclues.com/";
    ProgressDialog p;
    final Handler handler = new Handler();

    //testing
    private SwitchCompat switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        webview = findViewById(R.id.webview);


        p = new ProgressDialog(MainActivity.this);
        p.setMessage("loading");
        p.show();

        //calling function for webview
        web();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //testing
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_notification);
        View actionView = MenuItemCompat.getActionView(menuItem);

        switcher = actionView.findViewById(R.id.switcher);
        switcher.setChecked(true);
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, (switcher.isChecked()) ? "is checked!!!" : "not checked!!!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            webview.scrollTo(0, 0);
            web();
        } else if (id == R.id.nav_contact) {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:1234567890"));
            startActivity(callIntent);

        } else if (id == R.id.nav_whatsapp) {

            Intent i = new Intent();
            String url = null;
            try {
                url = "https://api.whatsapp.com/send?phone=" + "911234567890" + "&text=" + URLEncoder.encode("I want to knw about Femina !!", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            startActivity(i);

        } else if (id == R.id.nav_email) {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "prerna@mabble.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Thank You");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        } else if (id == R.id.nav_share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareSubText = "Be Beautiful with Femina";
            String shareBodyText = "https://play.google.com/store/apps/details?id=com.mabble.ecomnotification";
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(shareIntent, "Share With"));


        } else if (id == R.id.nav_register) {
            startActivity(new Intent(MainActivity.this, MobileActivity.class));

        } else if (id == R.id.nav_complete) {

        } else if (id == R.id.nav_notification) {

            //testing
            switcher.setChecked(!switcher.isChecked());
            Snackbar.make(item.getActionView(), (switcher.isChecked()) ? "is checked" : "not checked", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

        } else if (id == R.id.nav_exit) {

            //exit app
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setIcon(R.drawable.user)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                finishAndRemoveTask();
                            }
                            Intent mainIntent = new Intent(Intent.ACTION_MAIN);
                            mainIntent.addCategory(Intent.CATEGORY_HOME);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                            finish();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //function contains web url
    public void web() {

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.setMediaPlaybackRequiresUserGesture(true);
        }

        webSettings.setAllowContentAccess(true);
        webview.reload();
        webview.setAccessibilityDelegate(new View.AccessibilityDelegate());

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webview.setWebViewClient(webViewClient);
        webview.loadUrl(url);

    }

    //client for webview
    public class WebViewClientImpl extends WebViewClient {

        private Activity activity = null;
        public WebViewClientImpl(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (url.indexOf("https://www.shopclues.com/") > -1)
                return false;

            /*if (url.indexOf(url)>-1){
                return false;
            }*/

            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            p.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            p.show();

        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (1):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Log.i("prerna  : contact", "" + name);
                        // TODO Fetch other Contact details as you want to use

                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (webview.canGoBack()) {
                webview.goBack();
            } else {
                super.onBackPressed();
                handler.removeCallbacksAndMessages(null);
            }
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_POWER)) {

            Log.d("power", "power key pressed");

        }
        return super.onKeyDown(keyCode, event);
    }

}
