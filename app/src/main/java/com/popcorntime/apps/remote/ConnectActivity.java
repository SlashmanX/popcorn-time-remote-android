package com.popcorntime.apps.remote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.popcorntime.apps.remote.utils.BasicAuthenticator;
import com.popcorntime.apps.remote.utils.Utils;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;

import java.net.MalformedURLException;
import java.net.URL;


public class ConnectActivity extends Activity {

    private JSONRPC2Session mSession;
    private Button connectBtn;
    private EditText ipText, portText, passwordText, usernameText;
    SharedPreferences sharedPref;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connection_details);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        ctx = this.getApplication();

        connectBtn = (Button) findViewById(R.id.connectBtn);
        ipText = (EditText) findViewById(R.id.ipEditText);
        portText = (EditText) findViewById(R.id.portEditText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        usernameText = (EditText) findViewById(R.id.userEditText);

        usernameText.setText(sharedPref.getString(getString(R.string.pref_username), ""));
        portText.setText(sharedPref.getString(getString(R.string.pref_port), ""));
        ipText.setText(sharedPref.getString(getString(R.string.pref_ip), ""));
        passwordText.setText(sharedPref.getString(getString(R.string.pref_password), ""));

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ipText.getText().toString();
                String port = portText.getText().toString();
                if(!ip.startsWith("http://")) ip = "http://"+ ip;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.pref_ip), ip);
                editor.putString(getString(R.string.pref_port), port);
                editor.putString(getString(R.string.pref_password), passwordText.getText().toString());
                editor.putString(getString(R.string.pref_username), usernameText.getText().toString());
                editor.commit();
                editor.apply();

                URL serverURL = null;
                Utils.Log("IP: "+ ip);
                Utils.Log("Port: "+ port);
                try {
                    serverURL = new URL(ip + ":" + port);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                mSession = new JSONRPC2Session(serverURL);
                BasicAuthenticator auth = new BasicAuthenticator();
                auth.setCredentials(usernameText.getText().toString(), passwordText.getText().toString());
                mSession.setConnectionConfigurator(auth);

                JSONRPC2Response response = Utils.sendRequest(mSession, "ping", null);
                if(response != null && response.getError() == null) {
                    Intent intent = new Intent(ctx, Main.class);
                    startActivity(intent);
                }
                else {
                    Utils.Toast(ctx, "Error Connecting");
                }
            }
        });

    }

}

