package com.example.phogauss.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.phogauss.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;


import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;


import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import static android.util.Base64.DEFAULT;


public class MainActivity extends AppCompatActivity {

    final int RESULT_LOAD_IMAGE = 1;
    ImageView imageView;
    Bitmap bitmap = null;
    byte[] byteArray;
    Button convert;
    Uri selectedImage;
    public static String s;
    public static final String url = "https://phogauss.appspot.com/convert";//"http://localhost:6969/convert";
    EditText text;
    String output = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton cameraFab = findViewById(R.id.cameraFAB);
        FloatingActionButton galleryFab = findViewById(R.id.galleryFAB);
        text = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);
        convert = findViewById(R.id.submit);
        convert.setOnClickListener(view -> {
            //callServer();
           // new AsyncCaller().execute();
            //http(url, s);
//            Gson gson = new Gson();
//            gson.http(url,s);
            sendPost();
            text.setText(output);
            text.setVisibility(View.VISIBLE);

        });



        cameraFab.setOnClickListener(view -> {
            Snackbar.make(view, "Select Image", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(i, RESULT_LOAD_IMAGE);
        });

        galleryFab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

    }

    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://phogauss.appspot.com/convert");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add(0, s);
                    jsonParam.put("body", jsonArray);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonArray.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG", conn.getResponseMessage());

                        byte[] data = Base64.decode(conn.getResponseMessage(), Base64.DEFAULT);
                        output = new String(data, "UTF-8");


                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();


            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                convertBitMap(bitmap);
                convert.setText("Ready");
            }



        }
    }

    void convertBitMap(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        s = java.util.Base64.getEncoder().encodeToString(byteArray);

    }

    void callServer() {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);


        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                convert.setText("Received");
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                convert.setText(error.toString());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    convert.setText("Time out");
                } else if (error instanceof AuthFailureError) {
                    convert.setText("AUthFailureError");
                } else if (error instanceof ServerError) {
                    convert.setText("Server Error");
                } else if (error instanceof NetworkError) {
                    convert.setText("Network Error");
                } else if (error instanceof ParseError) {
                    convert.setText("Parse Error");
                }
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();


                MyData.put("body", s); //Add the data you'd like to send to the server.
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
        convert.setText("Sent");

    }
//
//
    public HttpResponse http(String url, String body) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(body);
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse result = httpClient.execute(request);

            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            try {
                JSONParser parser = new JSONParser();
                Object resultObject = parser.parse(json);

                if (resultObject instanceof JSONArray) {
                    JSONArray array = (JSONArray) resultObject;
                    for (Object object : array) {
                        JSONObject obj = (JSONObject) object;
                        System.out.println(obj.get("example"));
                        System.out.println(obj.get("fr"));
                    }

                } else if (resultObject instanceof JSONObject) {
                    JSONObject obj = (JSONObject) resultObject;
                    System.out.println(obj.get("example"));
                    System.out.println(obj.get("fr"));
                }

            } catch (Exception e) {
                // TODO: handle exception
            }

        } catch (IOException ex) {
        }
        return null;
    }



    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {

        public class Gson {

            public HttpResponse http(String url, String body) {

                try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                    HttpPost request = new HttpPost(MainActivity.url);
                    StringEntity params = new StringEntity(MainActivity.s);
                    request.addHeader("content-type", "application/json");
                    request.setEntity(params);
                    HttpResponse result = httpClient.execute(request);
                    String json = EntityUtils.toString(result.getEntity(), "UTF-8");

                    com.google.gson.Gson gson = new com.google.gson.Gson();
                    Response respuesta = gson.fromJson(json, Response.class);

                    System.out.println(respuesta.getExample());
                    System.out.println(respuesta.getFr());

                } catch (IOException ex) {
                    return null;
                }
                return null;
            }

            public class Response {

                private String example;
                private String fr;

                public String getExample() {
                    return example;
                }

                public void setExample(String example) {
                    this.example = example;
                }

                public String getFr() {
                    return fr;
                }

                public void setFr(String fr) {
                    this.fr = fr;
                }
            }
        }

        Gson gson;
       // ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // gson = new Gson();
            //this method will be running on UI thread
           // pdLoading.setMessage("\tLoading...");
            //pdLoading.show();
           // convert.setText("Sent");
        }
        @Override
        protected Void doInBackground(Void... param) {
            try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                    HttpPost request = new HttpPost(url);
                    StringEntity params = new StringEntity(s);
                    request.addHeader("content-type", "application/json");
                    request.setEntity(params);
                    HttpResponse result = httpClient.execute(request);
                    String json = EntityUtils.toString(result.getEntity(), "UTF-8");

                    com.google.gson.Gson gson = new com.google.gson.Gson();
                    Response respuesta = gson.fromJson(json, Response.class);

                } catch (IOException ex) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //this method will be running on UI thread
            //convert.setText("Received");
            //pdLoading.dismiss();
        }

    }

}






