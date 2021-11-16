package com.sbitbd.dubai2bd.ui.seller_login;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.dubai2bd.Config.DoConfig;
import com.sbitbd.dubai2bd.R;
import com.google.android.material.card.MaterialCardView;

import java.util.HashMap;
import java.util.Map;

public class seller_login extends Fragment {

    private SellerLoginViewModel mViewModel;
    private DoConfig config = new DoConfig();
    private ProgressDialog progressDialog;
    private EditText user,pass;
    private Button loginbtn;
    private MaterialCardView login_card;
    private WebView webView;

    public static seller_login newInstance() {
        return new seller_login();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.seller_login_fragment, container, false);
        user = root.findViewById(R.id.email_s);
        pass = root.findViewById(R.id.pass_s);
        loginbtn = root.findViewById(R.id.s_login_id);
        login_card = root.findViewById(R.id.loginId);
        webView= root.findViewById(R.id.seller_webview);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(user.getText().toString(),pass.getText().toString(),root);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SellerLoginViewModel.class);
        // TODO: Use the ViewModel
    }

    public void login(String username,String password,View root){
        try {
//            String en_pass = config.encrypt(password);

            progressDialog = ProgressDialog.show(root.getContext(),"","Loading",false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SELLER_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response != null && !response.equals("1")){
                                login_card.setVisibility(View.GONE);
                                webView.setVisibility(View.VISIBLE);
                                Toast.makeText(root.getContext().getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(root.getContext().getApplicationContext(),"Login failed",Toast.LENGTH_LONG).show();

                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(root.getContext().getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, username);
                    params.put(config.PRO_SIZE, password);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(root.getContext().getApplicationContext());
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }

}