package com.popcorntime.apps.remote;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.ConnectionConfigurator;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Main extends Activity {

    private String IP = "http://192.168.88.224";
    private String PORT = "8008";
    private JSONRPC2Session mSession;
    private ImageButton enterBtt, leftBtt, rightBtt, upBtt, downBtt, backBtt;
    int requestID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        URL serverURL = null;
        try {
            serverURL = new URL(IP +":"+ PORT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mSession = new JSONRPC2Session(serverURL);
        enterBtt = (ImageButton) findViewById(R.id.enterBtt);
        leftBtt = (ImageButton) findViewById(R.id.leftBtt);
        rightBtt = (ImageButton) findViewById(R.id.rightBtt);
        upBtt = (ImageButton) findViewById(R.id.upBtt);
        downBtt = (ImageButton) findViewById(R.id.downBtt);
        backBtt = (ImageButton) findViewById(R.id.backBtt);

        enterBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest("enter");
            }
        });

        upBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest("up");
            }
        });

        enterBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest("enter");
            }
        });

        leftBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest("left");
            }
        });

        rightBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest("right");
            }
        });

        downBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest("down");
            }
        });

        backBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest("backspace");
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

    public void sendRequest(String method) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("Param1", "value1");
        mSession.setConnectionConfigurator(new BasicAuthenticator());
        JSONRPC2Request request = new JSONRPC2Request(method, params, requestID);

        // Send request
        JSONRPC2Response response = null;

        try {
            response = mSession.send(request);

        } catch (JSONRPC2SessionException e) {

            System.err.println(e.getMessage());
            // handle exception...
        }

        // Print response result / error
        if (response != null && response.indicatesSuccess())
            System.out.println(response.getResult());
        else
            System.out.println(response.getError().getMessage());
    }
}

class BasicAuthenticator implements ConnectionConfigurator {

    private String user = "pocho";
    private String pass = "popcorn";
    public void configure(HttpURLConnection connection) {


        byte[] encodedBytes = Base64.encode((user+":"+pass).getBytes(), Base64.DEFAULT);

        // add custom HTTP header
        connection.addRequestProperty("Authorization", "Basic "+ new String(encodedBytes));
    }
}
