package com.sbitbd.dubai2bd.ui.track_order;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sbitbd.dubai2bd.Config.DoConfig;
import com.sbitbd.dubai2bd.R;
import com.sbitbd.dubai2bd.ui.cart_operation.operation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class order_info extends AppCompatActivity {

    private TextView order_id, or_date, or_amt, del_date_t, deli_add, total_t, paid_t, due_t;
    private View fast, fast_stick, second, second_stick, third, third_stick, forth;
    private DoConfig config = new DoConfig();
    private operation operation = new operation();
    private Button online_btn, bkash_btn, nagad_btn, rocket_btn;
    private BeautifulProgressDialog withLottie;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        try {
            order_id = findViewById(R.id.order_id);
            or_date = findViewById(R.id.or_date);
            or_amt = findViewById(R.id.or_amt);
            del_date_t = findViewById(R.id.del_date_t);
            deli_add = findViewById(R.id.deli_add);
            fast = findViewById(R.id.fast);
            fast_stick = findViewById(R.id.fast_stick);
            second = findViewById(R.id.second);
            second_stick = findViewById(R.id.second_stick);
            third = findViewById(R.id.third);
            third_stick = findViewById(R.id.third_stick);
            forth = findViewById(R.id.forth);
            online_btn = findViewById(R.id.online_btn);
            bkash_btn = findViewById(R.id.bkash_btn);
            nagad_btn = findViewById(R.id.nagad_btn);
            rocket_btn = findViewById(R.id.rocket_btn);
            total_t = findViewById(R.id.total_t);
            paid_t = findViewById(R.id.paid_t);
            due_t = findViewById(R.id.due_t);
            String order_id_get = getIntent().getStringExtra("order_id");
            String order_date_get = getIntent().getStringExtra("order_date");
            String order_state_get = getIntent().getStringExtra("order_state");
            if (order_id_get != null && !order_id_get.equals("")) {
                order_id.setText(order_id_get);
            }
            if (order_date_get != null && !order_date_get.equals("")) {
                or_date.setText(order_date_get);
            }
            getData(order_id_get);
            if (order_state_get != null && !order_state_get.equals("")) {
                switch (order_state_get) {
                    case "0":
                        fast.setBackground(getDrawable(R.drawable.done_state));
                        fast_stick.setBackground(getDrawable(R.drawable.done_state));
                        break;
                    case "1":
                        fast.setBackground(getDrawable(R.drawable.done_state));
                        fast_stick.setBackground(getDrawable(R.drawable.done_state));
                        second.setBackground(getDrawable(R.drawable.done_state));
                        second_stick.setBackground(getDrawable(R.drawable.done_state));
                        break;
                    case "2":
                        fast.setBackground(getDrawable(R.drawable.done_state));
                        fast_stick.setBackground(getDrawable(R.drawable.done_state));
                        second.setBackground(getDrawable(R.drawable.done_state));
                        second_stick.setBackground(getDrawable(R.drawable.done_state));
                        third.setBackground(getDrawable(R.drawable.done_state));
                        third_stick.setBackground(getDrawable(R.drawable.done_state));
                        break;
                    case "3":
                        fast.setBackground(getDrawable(R.drawable.done_state));
                        fast_stick.setBackground(getDrawable(R.drawable.done_state));
                        second.setBackground(getDrawable(R.drawable.done_state));
                        second_stick.setBackground(getDrawable(R.drawable.done_state));
                        third.setBackground(getDrawable(R.drawable.done_state));
                        third_stick.setBackground(getDrawable(R.drawable.done_state));
                        forth.setBackground(getDrawable(R.drawable.done_state));
                        break;
                    case "4":
                        fast.setBackground(getDrawable(R.drawable.off_state));
                        fast_stick.setBackground(getDrawable(R.drawable.off_stick));
                        second.setBackground(getDrawable(R.drawable.off_state));
                        second_stick.setBackground(getDrawable(R.drawable.off_stick));
                        third.setBackground(getDrawable(R.drawable.off_state));
                        third_stick.setBackground(getDrawable(R.drawable.off_stick));
                        forth.setBackground(getDrawable(R.drawable.off_state));
                        break;
                }
            }
            bkash_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show_bkash_form();
                }
            });
        } catch (Exception e) {
        }
    }

    private void getData(String id) {
        String sql = "SELECT `invoices`.`created_at`+ INTERVAL 2 DAY AS 'invoice_id',`invoices`." +
                "`created_at`+ INTERVAL 4 DAY AS 'product_name',invoices.grand_total AS 'created_at',delivery_infos.address AS 'status' FROM " +
                "`invoices` INNER JOIN delivery_infos ON invoices.delivery_id = delivery_infos.id WHERE invoice_id = '" + id + "'";
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.ORDER_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                showJson(response);
                            } else {
                                Toast.makeText(order_info.this, response, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(order_info.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(order_info.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showJson(String response) {
        String edate = "";
        String sdate = "";
        String address = "";
        String price = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                edate = collegeData.getString(config.PRO_NAME);
                price = collegeData.getString(config.PRO_SIZE);
                sdate = collegeData.getString(config.CAT_ID);
                address = collegeData.getString(config.PRO_IMAGE);
                del_date_t.setText(operation.reduce_date(sdate) + " and " + operation.reduce_date(edate));
                or_amt.setText(price);
                deli_add.setText(address);
            }
        } catch (Exception e) {
        }
    }

    private void show_bkash_form() {
        try {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(order_info.this   , R.style.CustomBottomSheetDialog);
            bottomSheetDialog.setDismissWithAnimation(true);
            bottomSheetDialog.setContentView(R.layout.pay_layout);
            Button submit = bottomSheetDialog.findViewById(R.id.submit_btn);
            EditText mobile = bottomSheetDialog.findViewById(R.id.mobile);
            EditText transaction = bottomSheetDialog.findViewById(R.id.trans);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mobile.getText().toString().trim().equals("")) {
                        mobile.setError("Empty Type");
                        Toast.makeText(getApplicationContext(), "Empty Mobile Number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mobile.getText().toString().length() < 11 || mobile.getText().toString().length() > 11) {
                        mobile.setError("Check your number length!");
                        Toast.makeText(getApplicationContext(), "Check your number length!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (transaction.getText().toString().trim().equals("")) {
                        transaction.setError("Empty Transaction ID");
                        Toast.makeText(getApplicationContext(), "Empty Transaction ID", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    withLottie = new BeautifulProgressDialog(order_info.this,
                            BeautifulProgressDialog.withLottie,
                            "Please wait");
                    withLottie.setLottieLocation("json_lottie/loading.json");
                    withLottie.setLayoutColor(Color.WHITE);
                    withLottie.setLayoutRadius(10f);
                    withLottie.setLayoutElevation(5f);
                    withLottie.show();

//                    String sql = "INSERT INTO `addfunds`(`date`, `member_id`, `customer_id`, `type`, " +
//                            "`amount`, `withdraw`, `number`, `details`, `status`) VALUES(" +
//                            "'" + config.getCreateDate() + "','" + homeViewModel.getSellerID(root1.getContext()
//                            .getApplicationContext()) + "','" + homeViewModel.getGuestID(root1.getContext()
//                            .getApplicationContext()) + "','" + type.getText().toString() + "','0.00" +
//                            "','" + amount.getText().toString().trim() + "','" + account.getText().toString()
//                            .trim() + "','" + det.getText().toString().trim() + "','0')";

                }
            });

            bottomSheetDialog.show();

        } catch (Exception e) {e.printStackTrace();
        }
    }
}