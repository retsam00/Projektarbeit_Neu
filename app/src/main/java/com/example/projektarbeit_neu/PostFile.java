package com.example.projektarbeit_neu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Network;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PostFile extends Activity {
    URL url;
    TextView tvResult;
    Button btnSend;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postfile);


        tvResult = (TextView)findViewById(R.id.txtResult);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSend.setEnabled(false);
                new Thread() {
                   public boolean running = true;
                    public synchronized void run() {
                        if(running) {
                            JSONObject inputJSON = new JSONObject();
                            try {
                                inputJSON.put("image", "testimage");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            NetworkManager nm = new NetworkManager("http://192.168.178.107:8000/receive/");
                            JSONObject result = nm.postFile(inputJSON);
                            if(result != null) {
                                tvResult.append(result.toString());
                            }


                        }
                    }
                }.start();
                btnSend.setEnabled(true);
            }
        });
    }


}
