package com.example.projektarbeit_neu;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;

import android.util.Base64;
import android.widget.TextView;

public class PostFile extends Activity {
    URL url;
    TextView tvResult;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postfile);
        try{
            url = new URL("http://127.0.0.1:8000/");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        tvResult = (TextView)findViewById(R.id.txtResult);
    }

    public String bitmapToString(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress (Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    public void postImage(Bitmap image) {
        try  {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            //String jsonInputString = "{"name": "Upendra", "job": "Programmer"}";
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
            wr.write("CLIENT_TEST_TEXT");
            wr.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            tvResult.append(sb);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}
