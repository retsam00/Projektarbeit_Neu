package com.example.projektarbeit_neu;

import android.graphics.Bitmap;
import android.net.Network;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkManager  {
    URL url;
    public NetworkManager(String address)
    {
        try{
            url = new URL(address);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public JSONObject bitmapToJSON(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress (Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        JSONObject resultJson = new JSONObject();
        try{
            resultJson.put("image", encodedImage);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return resultJson;
    }
    public synchronized JSONObject postFile(JSONObject inputJSON) {
        StringBuilder sb = new StringBuilder();
        try  {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write(inputJSON.toString());
            wr.flush();
            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String line = null;
            while((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

        }
        catch(Exception e)
        {
            sb.append(e.toString());
        }
        JSONObject outputJSON=null;
        try {
            outputJSON=new JSONObject(sb.toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return outputJSON;
    }



}
