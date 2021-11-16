package com.sbitbd.dubai2bd.ui.login_page.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.dubai2bd.Config.DoConfig;
import com.sbitbd.dubai2bd.MainActivity;
import com.sbitbd.dubai2bd.R;
import com.sbitbd.dubai2bd.ui.create_admin.admin;
import com.sbitbd.dubai2bd.ui.forget.forget_code;
import com.sbitbd.dubai2bd.ui.home.HomeViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private TextView signup;
    private DoConfig config = new DoConfig();
    ProgressBar loadingProgressBar;
    HomeViewModel homeViewModel = new HomeViewModel();
    EditText usernameEditText,passwordEditText,phoneE;
    private Button forgot;
    private View view;
    private String forget_phone;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        forgot = findViewById(R.id.forgot_btn);
        loadingProgressBar = findViewById(R.id.loading);
        signup = findViewById(R.id.sign_up);
        signup.setLinksClickable(true);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.forgot_phone, null);
                phoneE = view.findViewById(R.id.phone_for);
                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(LoginActivity.this);
                dialogBuilder.setTitle("Forget Password");
                dialogBuilder.setMessage("Enter your Phone Number for a verification code");

                dialogBuilder.setView(view);
                dialogBuilder.setNegativeButton("Cancel",(dialog, which) -> {
                    forget_phone = "";
                    dialog.cancel();
                });
                dialogBuilder.setPositiveButton("Reset",(dialog, which) -> {
                    if (phoneE.getText().toString().equals("")){
                        phoneE.setError("Empty Number");
                        Toast.makeText(LoginActivity.this,"Empty Number",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (phoneE.getText().toString().length() < 11){
                        phoneE.setError("Phone Number must be 11 characters");
                        Toast.makeText(LoginActivity.this,"Phone Number must be 11 characters",Toast.LENGTH_LONG).show();
                        return;
                    }
                    forget_phone = phoneE.getText().toString();
                    getCode(forget_phone);
                });
                dialogBuilder.show();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, admin.class));
            }
        });

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                        updateUiWithUser(loginResult.getSuccess());
                }

                //Complete and destroy login activity once successful

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),LoginActivity.this);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                login(usernameEditText.getText().toString(),passwordEditText.getText().toString());
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString(),LoginActivity.this);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
            String welcome = getString(R.string.welcome) + model.getDisplayName();
            // TODO : initiate successful logged in experience
            Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();

    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    public void login(String username,String password){
        try {
//            String en_pass = config.encrypt(password);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.equals("1")){
                                showJson(response);
                            }
                            else {
                                Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_LONG).show();

                            }
                            loadingProgressBar.setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this,"Failed",Toast.LENGTH_LONG).show();
                    loadingProgressBar.setVisibility(View.GONE);
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
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
    private void showJson(String response){
        String name="",id = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(config.PRO_NAME);
                id = collegeData.getString(config.CAT_ID);
                updateUiWithUser(new LoggedInUserView(name));
                if(homeViewModel.checkUser(LoginActivity.this)){
                    homeViewModel.updateUser(LoginActivity.this,id);
                }else {
                    homeViewModel.insertuser(LoginActivity.this, "", "", usernameEditText.getText().toString()
                            , passwordEditText.getText().toString(), id);
                }
            }
        }catch (Exception e){
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
    }

    private void getCode(String phone){
        try {
            progressDialog = ProgressDialog.show(LoginActivity.this,"","Loading...",false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.FORGET,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if(!response.equals("1")) {
                                Intent intent = new Intent(LoginActivity.this,forget_code.class);
                                intent.putExtra("phone",phone);
                                startActivity(intent);
                            }else
                                Toast.makeText(LoginActivity.this,"Invalid Phone" , Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("phone", phone);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
}