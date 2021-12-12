package com.sbitbd.dubai2bd.ui.track_order;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sbitbd.dubai2bd.Config.DoConfig;
import com.sbitbd.dubai2bd.R;
import com.sbitbd.dubai2bd.ui.cart_operation.operation;
import com.sbitbd.dubai2bd.ui.checkout.checkout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class order_info extends AppCompatActivity {

    private TextView order_id, or_date, or_amt, del_date_t, deli_add, total_t, paid_t, due_t;
    private View fast, fast_stick, second, second_stick, third, third_stick, forth;
    private DoConfig config = new DoConfig();
    private operation operation = new operation();
    private Button online_btn, bkash_btn, nagad_btn, rocket_btn;
    private BeautifulProgressDialog withLottie;
    private int payment_check = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");
        getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
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
                    payment_check = 1;
                    show_bkash_form("Merchant Account", "01557-770122", "", "bKash");
                }
            });
            nagad_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    payment_check = 1;
                    show_bkash_form("Merchant Account", "01817549090", "", "Nagad");
                }
            });
            rocket_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    payment_check = 1;
                    show_bkash_form("Personal Account", "01832-3065409", "", "Rocket");
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

    private void show_bkash_form(String type_t, String num_t, String sql, String title) {
        try {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(order_info.this, R.style.CustomBottomSheetDialog);
            bottomSheetDialog.setDismissWithAnimation(true);
            bottomSheetDialog.setContentView(R.layout.pay_layout);
            Button submit = bottomSheetDialog.findViewById(R.id.submit_btn);
            EditText mobile = bottomSheetDialog.findViewById(R.id.mobile);
            EditText transaction = bottomSheetDialog.findViewById(R.id.trans);
            TextView type, num, title_t, date_id;
            MaterialCardView date;
            type = bottomSheetDialog.findViewById(R.id.type);
            date = bottomSheetDialog.findViewById(R.id.date);
            num = bottomSheetDialog.findViewById(R.id.num);
            title_t = bottomSheetDialog.findViewById(R.id.title);
            date_id = bottomSheetDialog.findViewById(R.id.date_id);
            type.setText(type_t);
            num.setText(num_t);
            title_t.setText(title + " Payment");

            MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
            materialDateBuilder.setTitleText("SELECT A DATE");
            materialDateBuilder.setTheme(R.style.RoundShapeCalenderTheme);

            final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            });
            materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onPositiveButtonClick(Long selection) {
                    TimeZone timeZoneUTC = TimeZone.getDefault();
                    String date;
                    // It will be negative, so that's the -1
                    int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                    SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    Date date1 = new Date(selection + offsetFromUTC);
                    date = simpleFormat.format(date1);
                    date_id.setText(date);
                }
            });
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
                    if (date_id.getText().toString().trim().equals("")) {
                        date_id.setError("Empty Transaction ID");
                        Toast.makeText(getApplicationContext(), "Empty Transaction ID", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    withLottie = new BeautifulProgressDialog(order_info.this,
                            BeautifulProgressDialog.withLottie, null);
                    withLottie.setLottieLocation("json_lottie/loading.json");
                    withLottie.setLayoutColor(Color.WHITE);
                    withLottie.setLayoutRadius(13f);
                    withLottie.setLayoutElevation(5f);
                    withLottie.show();

                    update("UPDATE `invoices` SET payment_type = '" + title.toLowerCase() + "',mobile_acc" +
                            "='" + mobile.getText().toString().trim() + "',trans_id = '" + transaction.getText()
                            .toString().trim() + "',updated_at = current_timestamp WHERE invoice_id = '" + order_id.getText().toString().trim() + "'");
                    bottomSheetDialog.dismiss();

                }
            });

            bottomSheetDialog.show();
        } catch (Exception e) {
        }
    }

    private void update(String sql) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (withLottie.isShowing())
                                withLottie.dismiss();
                            if (response.trim().equals("1")) {
                                payment_check++;
                                if (payment_check == 2){
                                    View view = LayoutInflater.from(order_info.this).inflate(R.layout.success_lay, null);

                                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(order_info.this,R.style.RoundShapeTheme);
//                                    dialogBuilder.setTitle("Sign In");
                                    dialogBuilder.setCancelable(true);
                                    dialogBuilder.setView(view);
                                    dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                                        dialog.dismiss();
                                    });
                                    dialogBuilder.show();
                                }
                            } else {
                                Toast.makeText(order_info.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (withLottie.isShowing())
                        withLottie.dismiss();
                    Toast.makeText(order_info.this, error.toString(), Toast.LENGTH_SHORT).show();
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
}