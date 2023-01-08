package com.example.memeshare;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import org.json.JSONObject;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private String url = "https://meme-api.com/gimme";
    String uri;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        LoadMeme();
    }

    private void LoadMeme() {
        progressBar.setVisibility(View.VISIBLE);
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
            public void onResponse(JSONObject response) {
            try {
                uri = response.getString("url");
                ImageView imageview = (ImageView) findViewById(R.id.imageView);
                Glide.with(MainActivity.this).load(uri).into(imageview);
            }
            catch (JSONException e) {
                // if we do not extract data from json object properly.
                // below line of code is use to handle json exception
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error :" + error.toString());
                Toast.makeText(MainActivity.this, "Please Check Your Internet Connectivity", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        mRequestQueue.add(jsonObjectRequest);
    }

    public void ShareMeme(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool meme I got from Reddit: "+uri);
        Intent chooser = Intent.createChooser(intent, "Share this meme using: ");
        startActivity(chooser);
    }

    public void NextMeme(View view) {
        LoadMeme();
    }
}