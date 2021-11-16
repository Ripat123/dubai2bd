package com.sbitbd.dubai2bd.ui.faq;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.dubai2bd.Config.DoConfig;
import com.sbitbd.dubai2bd.R;

import java.util.HashMap;
import java.util.Map;

public class faq extends Fragment {


    private WebView webView;
    private DoConfig config = new DoConfig();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_faq, container, false);
        webView = root.findViewById(R.id.faq_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        loadData(root);
        return root;
    }
    private void loadData(View root){
        try {

            String sql = "SELECT `faq_infos`.`description` AS 'id' FROM `faq_infos`";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.equals("1")){
                                String encodedHtml = Base64.encodeToString(response.getBytes(),
                                        Base64.NO_PADDING);
                                webView.loadData(encodedHtml, "text/html", "base64");
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(root.getContext().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(root.getContext().getApplicationContext());
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
}