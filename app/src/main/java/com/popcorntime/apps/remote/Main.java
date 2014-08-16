package com.popcorntime.apps.remote;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.popcorntime.apps.remote.utils.BasicAuthenticator;
import com.popcorntime.apps.remote.utils.Utils;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;

import java.net.MalformedURLException;
import java.net.URL;


public class Main extends Activity {

    private JSONRPC2Session mSession;
    private ImageButton enterBtt, leftBtt, rightBtt, upBtt, downBtt, backBtt;
    int requestID = 1;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.Log("MAIN CREATED!!!!!");
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String ip = sharedPref.getString(getString(R.string.pref_ip), "http://127.0.0.1");
        String port = sharedPref.getString(getString(R.string.pref_port), "8008");
        String user = sharedPref.getString(getString(R.string.pref_username), "pocho");
        String pass = sharedPref.getString(getString(R.string.pref_password), "popcorn");
        URL serverURL = null;
        try {
            serverURL = new URL(ip +":"+ port);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mSession = new JSONRPC2Session(serverURL);
        BasicAuthenticator auth = new BasicAuthenticator();
        auth.setCredentials(user, pass);
        mSession.setConnectionConfigurator(auth);
        enterBtt = (ImageButton) findViewById(R.id.enterBtt);
        leftBtt = (ImageButton) findViewById(R.id.leftBtt);
        rightBtt = (ImageButton) findViewById(R.id.rightBtt);
        upBtt = (ImageButton) findViewById(R.id.upBtt);
        downBtt = (ImageButton) findViewById(R.id.downBtt);
        backBtt = (ImageButton) findViewById(R.id.backBtt);

        enterBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendRequest(mSession, "enter", null);
            }
        });

        upBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendRequest(mSession, "up", null);
            }
        });

        enterBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendRequest(mSession, "enter", null);
            }
        });

        leftBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendRequest(mSession, "left", null);
            }
        });

        rightBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendRequest(mSession, "right", null);
            }
        });

        downBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendRequest(mSession, "down", null);
            }
        });

        backBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sendRequest(mSession, "back", null);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
