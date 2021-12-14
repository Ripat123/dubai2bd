package com.sbitbd.dubai2bd.ui.track_order;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aamarpay.library.AamarPay;
import com.aamarpay.library.DialogBuilder;
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
import com.sbitbd.dubai2bd.ui.home.HomeViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class order_info extends AppCompatActivity {

    private TextView order_id, or_date, or_amt, del_date_t, deli_add, total_t, paid_t, due_t;
    private View fast, fast_stick, second, second_stick, third, third_stick, forth;
    private DoConfig config = new DoConfig();
    private operation operation = new operation();
    private Button online_btn, bkash_btn, nagad_btn, rocket_btn;
    private BeautifulProgressDialog withLottie;
    private int payment_check = 0;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private DialogBuilder dialogBuilder;
    private HomeViewModel homeViewModel = new HomeViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_fragment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");
        getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        initView();
    }

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
            getPay(order_id.getText().toString());
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
            online_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editText = new EditText(order_info.this);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editText.setBackground(AppCompatResources.getDrawable(order_info.this,R.drawable.edittext));
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(order_info.this,R.style.RoundShapeTheme);
                    dialogBuilder.setTitle("Pay Amount");
                    dialogBuilder.setCancelable(false);
                    dialogBuilder.setView(editText);
                    dialogBuilder.setNegativeButton("NO",(dialog, which) -> {
                        dialog.dismiss();
                    });
                    dialogBuilder.setPositiveButton("Yes",(dialog, which) -> {
                        if (editText.getText().toString().equals("") || editText.getText().toString().equals("0")) {
                            Toast.makeText(order_info.this, "Empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        sixdms(editText.getText().toString().trim());
                    });
                    dialogBuilder.show();
                    editText.setText(total_t.getText().toString());
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
                                Toast.makeText(order_info.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
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

    private void getPay(String id) {
        String sql = "SELECT payment as 'one',due as 'two' FROM invoice_balance_sheet WHERE invoice_id = '" + id + "'";
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SIX_DMS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
                                show_pay(response.trim());
                            } else {
                                Toast.makeText(order_info.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
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
                total_t.setText(price);
                deli_add.setText(address);
            }
        } catch (Exception e) {
        }
    }
    private void show_pay(String response) {
        String one = "";
        String two = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                one = collegeData.getString(config.ONE);
                two = collegeData.getString(config.TWO);
                paid_t.setText(one);
                due_t.setText(two);
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

    private void sixdms(String amount) {
        String sql = "SELECT invoices.invoice_id, delivery_infos.`first_name` as 'one', delivery_infos.`email` " +
                "as 'two', delivery_infos.`address` as 'four', delivery_infos.`phone` as " +
                "'three' FROM `invoices` inner join delivery_infos on invoices.delivery_id=delivery_infos" +
                ".id where invoice_id = '"+order_id.getText().toString()+"'";

        alertDialog = new ProgressDialog(order_info.this);
        dialogBuilder = new DialogBuilder(order_info.this, alertDialog);

        // Private Dialog
        builder = new AlertDialog.Builder(order_info.this);
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.SIX_DMS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("")) {
//                                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                                try {
                                    String cus_name, cus_email, cus_phone, cus_add, cus_city, cus_country, trx_id;
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray(DoConfig.RESULT);
                                    JSONObject collegeData = result.getJSONObject(0);
                                    String bdt = "BDT";
                                    cus_name = collegeData.getString(DoConfig.ONE);
                                    cus_email = collegeData.getString(DoConfig.TWO);
                                    cus_phone = collegeData.getString(DoConfig.THREE);
                                    cus_add = collegeData.getString(DoConfig.FOUR);
                                    cus_city = collegeData.getString(DoConfig.FIVE);
                                    cus_country = collegeData.getString(DoConfig.SIX);

                                    AamarPay aamarPay = new AamarPay(order_info.this, "aamarpay", "28c78bb1f45112f5d40b956fe104645a");
                                    aamarPay.testMode(true);
                                    aamarPay.autoGenerateTransactionID(false);
//                                    Random rand = new Random();
//                                    int maxNumber = 5;
//                                    int randomNumber = rand.nextInt(maxNumber) + 1;
                                    aamarPay.setTransactionID(UUID.randomUUID().toString() +"-"+order_id.getText().toString());
//                                    trx_id = aamarPay.generate_trx_id();
                                    aamarPay.setTransactionParameter(amount, bdt, "Customer payment");

                                    aamarPay.setCustomerDetails(cus_name, cus_email, cus_phone, cus_add, deli_add.getText().toString(), "BD");
//                                        Toast.makeText(context, trx_id, Toast.LENGTH_LONG).show();
                                    aamarPay.initPGW(new AamarPay.onInitListener() {
                                        @Override
                                        public void onInitFailure(Boolean error, String message) {
                                            try {
                                                Log.d("TEST_IF", message);
                                                dialogBuilder.dismissDialog();
                                                dialogBuilder.errorPopUp(message);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        @Override
                                        public void onPaymentSuccess(JSONObject jsonObject) {
                                            try {
                                                Log.d("TEST_PS", jsonObject.toString());
                                                dialogBuilder.dismissDialog();
                                                double due;
                                                String mer_txnid,cus_name,cus_phone,cus_email,pg_service_charge_bdt,
                                                        amount_original,pg_card_bank_name,
                                                        card_number,currency_merchant,convertion_rate,ip_address,
                                                        other_currency,pay_status,pg_txnid,currency,store_amount,pay_time,
                                                        amount,bank_txn,card_type,pg_card_risklevel,pg_error_code_details;
                                                mer_txnid = jsonObject.getString(DoConfig.MER_TXNID);
                                                cus_name = jsonObject.getString(DoConfig.CUS_NAME);
                                                cus_phone = jsonObject.getString(DoConfig.cus_phone);
                                                cus_email = jsonObject.getString(DoConfig.cus_email);
                                                pg_service_charge_bdt = jsonObject.getString(DoConfig.processing_charge);
                                                amount_original = jsonObject.getString(DoConfig.amount);
                                                pg_card_bank_name = jsonObject.getString(DoConfig.payment_processor);
                                                card_number = jsonObject.getString(DoConfig.cardnumber);
                                                currency_merchant = jsonObject.getString(DoConfig.currency_merchant);
                                                convertion_rate = jsonObject.getString(DoConfig.convertion_rate);
                                                ip_address = jsonObject.getString(DoConfig.ip);
                                                other_currency = jsonObject.getString(DoConfig.other_amount);
                                                pay_status = jsonObject.getString(DoConfig.pay_status);
                                                pg_txnid = jsonObject.getString(DoConfig.pg_txnid);
                                                currency = jsonObject.getString(DoConfig.currency);
                                                store_amount = jsonObject.getString(DoConfig.rec_amount);
                                                pay_time = jsonObject.getString(DoConfig.date_processed);
                                                amount = jsonObject.getString(DoConfig.amount);
                                                bank_txn = jsonObject.getString(DoConfig.bank_trxid);
                                                card_type = jsonObject.getString(DoConfig.payment_type);
                                                pg_card_risklevel = jsonObject.getString(DoConfig.risk_level);
                                                pg_error_code_details = jsonObject.getString(DoConfig.error_code);
                                                balance_add(order_info.this,"INSERT INTO `online_payment_details`" +
                                                        "(`mer_txnid`, `customer_id`, `cus_name`, `cus_phone`, " +
                                                        "`cus_email`, `pg_service_charge_bdt`, `amount_original`, " +
                                                        " `pg_card_bank_name`, " +
                                                        "`card_number`, `currency_merchant`, " +
                                                        "`convertion_rate`, `ip_address`, `other_currency`, `pay_status`, " +
                                                        "`pg_txnid`, `currency`, `store_amount`, `pay_time`, `amount`, " +
                                                        "`bank_txn`, `card_type`, `pg_card_risklevel`, " +
                                                        "`pg_error_code_details`, `session_id`) VALUES ('"+mer_txnid+"'," +
                                                        "'"+homeViewModel.getGuestID(order_info.this)+"','"+cus_name+"','"+cus_phone+"'," +
                                                        "'"+cus_email+"','"+pg_service_charge_bdt+"','"+amount_original+"'," +
                                                        "'"+pg_card_bank_name+"','"+card_number+"','"+currency_merchant+"'," +
                                                        "'"+convertion_rate+"','"+ip_address+"','"+other_currency+"','"+pay_status+"'," +
                                                        "'"+pg_txnid+"','"+currency+"','"+store_amount+"','"+pay_time+"','"+amount+"'," +
                                                        "'"+bank_txn+"','"+card_type+"','"+pg_card_risklevel+"','"+pg_error_code_details+"','"+homeViewModel.getSession(order_info.this)+"')","failed payment entry");
                                                due = Double.parseDouble(due_t.getText().toString()) - Double.parseDouble(amount);
                                                balance_add(order_info.this, "UPDATE `invoice_balance_sheet` SET " +
                                                        "`payment`= payment + '"+amount+"', `due`=" +
                                                        "'" + due + "', `attempt`= attempt + 1,updated_at= current_timestamp where invoice_id='" + order_id.getText().toString() + "'","failed balance entry");
//                                                Invoice_json(response1, context, cuoponID, session, subT, disT, delT, totalT);

                                            }catch (Exception e){
                                            }
                                        }

                                        @Override
                                        public void onPaymentFailure(JSONObject jsonObject) {
                                            Log.d("TEST_PF", jsonObject.toString());
                                            dialogBuilder.dismissDialog();

                                            builder.setTitle("Payment Failed Response");
                                            builder.setMessage(jsonObject.toString());

                                            AlertDialog alertDialog1 = builder.create();
                                            alertDialog1.show();
                                        }

                                        @Override
                                        public void onPaymentProcessingFailed(JSONObject jsonObject) {
                                            Log.d("TEST_PPF", jsonObject.toString());
                                            dialogBuilder.dismissDialog();

                                            builder.setTitle("Payment Processing Failed Response");
                                            builder.setMessage(jsonObject.toString());

                                            AlertDialog alertDialog1 = builder.create();
                                            alertDialog1.show();
                                        }

                                        @Override
                                        public void onPaymentCancel(JSONObject jsonObject) {
                                            Log.d("TEST_PC", jsonObject.toString());
                                            try {

                                                // Call the transaction verification check validity
                                                aamarPay.getTransactionInfo(jsonObject.getString("trx_id"), new AamarPay.TransactionInfoListener() {
                                                    @Override
                                                    public void onSuccess(JSONObject jsonObject) {
                                                        Log.d("TEST_", jsonObject.toString());
                                                        dialogBuilder.dismissDialog();
//                                                        Invoice_json(response1, context, cuoponID, session, subT, disT, delT, totalT);
                                                        builder.setTitle("Trx Verification Success Response");
                                                        builder.setMessage(jsonObject.toString());

                                                        AlertDialog alertDialog1 = builder.create();
                                                        alertDialog1.show();
                                                    }

                                                    @Override
                                                    public void onFailure(Boolean error, String message) {
                                                        Log.d("TEST_", message);
                                                        dialogBuilder.dismissDialog();
                                                        dialogBuilder.errorPopUp(message);
                                                    }
                                                });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

//
                            } else {
                                Toast.makeText(order_info.this, "No data", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(order_info.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, sql);
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
    private void balance_add(Context context, String sql,String fmg) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.INSERT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.trim().equals("1")) {
                                Toast.makeText(context, fmg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }
}