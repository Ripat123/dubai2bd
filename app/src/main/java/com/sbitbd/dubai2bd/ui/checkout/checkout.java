package com.sbitbd.dubai2bd.ui.checkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.dubai2bd.Adapter.cat_model;
import com.sbitbd.dubai2bd.Adapter.checkout_pro_adapter;
import com.sbitbd.dubai2bd.Adapter.checkout_pro_model;
import com.sbitbd.dubai2bd.Config.DoConfig;
import com.sbitbd.dubai2bd.R;
import com.sbitbd.dubai2bd.ui.cart_operation.operation;
import com.sbitbd.dubai2bd.ui.home.HomeViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class checkout extends AppCompatActivity implements AdapterView.OnItemClickListener {

    RecyclerView recyclerView;
    checkout_pro_model checkout_pro_model;
    checkout_pro_adapter checkout_pro_adapter;
    RadioButton cash_rd, bkash_rd, rocket_rd, nagad_rd,online_id;
    DoConfig config;
    HomeViewModel homeViewModel = new HomeViewModel();
    operation operation = new operation();
    private TextView subT, disT, deliT, grnT;
    private AutoCompleteTextView district, thana;
    private Button submit, promo_btn;
    private EditText first_name, Phone, fulladdress, promo_field;
    private static final List<cat_model> districtList = new ArrayList<>();
    private static final List<cat_model> thanaList = new ArrayList<>();
    private static final List<String> model = new ArrayList<>();
    private static final List<String> thanamodel = new ArrayList<>();
    private static List<String> shipping_id = new ArrayList<>();
    private double del_ch = 0;
    private double gr_t = 0;
    private double Total = 0;
    private static String mobileNO = "", trnID = "", pay_type = "", disTK = "0", subTK, totalTK;
    private static String username, pass;
    private EditText firstname_ed, phone_ed, email_ed, address_ed, password_ed, con_password_ed;
    private View dialog_view;
    private View signin_view, singup_view;
    private EditText phone_n, trn_n;
    private String districtid = "", session = "", id = "", thana_id = "", gid = "",email="";
    private int coupon_check = 0;
    private ProgressDialog progressDialog, progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        initview();
        initpro_check();
        showDistrict();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(checkout.this,
                R.layout.item_name, model);
        district.setAdapter(dataAdapter);

    }

    private void initview() {
        cash_rd = findViewById(R.id.cash_id);
        bkash_rd = findViewById(R.id.bkash_id);
        rocket_rd = findViewById(R.id.rocket_id);
        nagad_rd = findViewById(R.id.nagad_id);
        online_id = findViewById(R.id.online_id);
        subT = findViewById(R.id.total_id);
        disT = findViewById(R.id.dis_id);
        deliT = findViewById(R.id.delivery_id);
        grnT = findViewById(R.id.grand_id);
        district = findViewById(R.id.district);
        thana = findViewById(R.id.thana);
        submit = findViewById(R.id.submit_btn);
        promo_btn = findViewById(R.id.apply_prormo_btn);
        first_name = findViewById(R.id.first);
        Phone = findViewById(R.id.ph);
        fulladdress = findViewById(R.id.addl);
        promo_field = findViewById(R.id.promo_text);

        dialog_view = LayoutInflater.from(checkout.this).inflate(R.layout.payment_dialog, null);
        phone_n = dialog_view.findViewById(R.id.phone_di);
        trn_n = dialog_view.findViewById(R.id.trnid);
        district.setOnItemClickListener(this);
        thana.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int item;
                String inQuery = "";
                cat_model cat_model = thanaList.get(position);
                gr_t = Double.parseDouble(grnT.getText().toString());
//            Total =  gr_t - del_ch;

                item = checkout_pro_adapter.getItemCount();
                for (int i = 0; i < item; i++) {
                    if (inQuery.equals(""))
                        inQuery = "'" + shipping_id.get(i) + "'";
                    else
                        inQuery = inQuery + "," + "'" + shipping_id.get(i) + "'";
                }

                getCharge(cat_model.getId(), inQuery);
//            Total = del_ch + Total;
//            deliT.setText(cat_model.getId());
//            grnT.setText(String.valueOf(Total));
                thana_id = cat_model.getId();

                totalTK = grnT.getText().toString();
                subTK = subT.getText().toString();
            }
        });

        gid = homeViewModel.getGuestID(checkout.this);
        if (gid != null && !gid.equals("")) {
            getGuestData(gid);
        } else {
            gid = "1";
//            sign_in(checkout.this,null,null,"1");
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAction();
            }
        });
        promo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coupon_check != 1) {
                    coupon_check = 1;
                    getdiscount();
                } else {
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
                    dialogBuilder.setTitle("Promotion");
                    dialogBuilder.setMessage("Already Applied");
                    dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                        dialog.cancel();
                    });
                    dialogBuilder.show();
                }
            }
        });
        nagad_rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog_view = LayoutInflater.from(checkout.this).inflate(R.layout.payment_dialog, null);
                    phone_n = dialog_view.findViewById(R.id.phone_di);
                    trn_n = dialog_view.findViewById(R.id.trnid);
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
                    dialogBuilder.setTitle("Nagad (Personal Account)");
                    dialogBuilder.setMessage("+880197132322");

                    dialogBuilder.setView(dialog_view);
                    if (pay_type != null && pay_type.equals("nagad")) {
                        phone_n.setText(mobileNO);
                        trn_n.setText(trnID);
                    }
                    dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                        mobileNO = "";
                        trnID = "";
                        nagad_rd.setSelected(false);
                    });
                    dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                        mobileNO = phone_n.getText().toString();
                        trnID = trn_n.getText().toString();
                        pay_type = "nagad";
                    });
                    dialogBuilder.show();
                } catch (Exception e) {
                }

            }
        });
        rocket_rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog_view = LayoutInflater.from(checkout.this).inflate(R.layout.payment_dialog, null);
                    phone_n = dialog_view.findViewById(R.id.phone_di);
                    trn_n = dialog_view.findViewById(R.id.trnid);
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
                    dialogBuilder.setTitle("Rocket (Personal Account)");
                    dialogBuilder.setMessage("+8801971323232");

                    dialogBuilder.setView(dialog_view);
                    if (pay_type != null && pay_type.equals("rocket")) {
                        phone_n.setText(mobileNO);
                        trn_n.setText(trnID);
                    }
                    dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                        mobileNO = "";
                        trnID = "";
                        rocket_rd.setSelected(false);
                    });
                    dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                        mobileNO = phone_n.getText().toString();
                        trnID = trn_n.getText().toString();
                        pay_type = "rocket";
                    });
                    dialogBuilder.show();
                } catch (Exception e) {
                }

            }
        });
        bkash_rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog_view = LayoutInflater.from(checkout.this).inflate(R.layout.payment_dialog, null);
                    phone_n = dialog_view.findViewById(R.id.phone_di);
                    trn_n = dialog_view.findViewById(R.id.trnid);
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
                    dialogBuilder.setTitle("Bkash (Merchant Account)");
                    dialogBuilder.setMessage("+8801317696136");

                    dialogBuilder.setView(dialog_view);
                    if (pay_type != null && pay_type.equals("bkash")) {
                        phone_n.setText(mobileNO);
                        trn_n.setText(trnID);
                    }
                    dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                        mobileNO = "";
                        trnID = "";
                        bkash_rd.setSelected(false);
                    });
                    dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                        mobileNO = phone_n.getText().toString();
                        trnID = trn_n.getText().toString();
                        pay_type = "bkash";
                    });
                    dialogBuilder.show();
                } catch (Exception e) {
                }

            }
        });
        cash_rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_type = "cash";
                mobileNO = "";
                trnID = "";
            }
        });
        online_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_type = "online";
                mobileNO = "";
                trnID = "";
            }
        });
    }

    private void initpro_check() {
        recyclerView = findViewById(R.id.check_pro);
        checkout_pro_adapter = new checkout_pro_adapter(checkout.this, subT, grnT);
        GridLayoutManager manager = new GridLayoutManager(checkout.this, 1);
        recyclerView.setLayoutManager(manager);
        getOnlinedata();
        recyclerView.setAdapter(checkout_pro_adapter);
    }

    private void getdiscount() {
        try {
            progressDialog = ProgressDialog.show(checkout.this, "", "", false, false);
            String sql = "SELECT `id`,`discout_price` FROM `coupons` WHERE `coupon_name` = '" + promo_field.getText().toString() + "' " +
                    "AND `start_date` <= CURDATE() AND `end_date` >= CURDATE() AND `min_price` <= '" + subT.getText().toString() + "' AND `status` = '1'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.COUPON,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (!response.equals("1")) {
                                showDicountjson(response);
                            } else {
                                MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
                                dialogBuilder.setTitle("Promotion");
                                dialogBuilder.setMessage("Promotion not found");
                                dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
                                    dialog.cancel();
                                });
                                dialogBuilder.show();
                                coupon_check = 0;
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void showDicountjson(String response) {
        String discount = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DoConfig.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                discount = collegeData.getString(DoConfig.PRO_DISCOUNT_PRICE);
                id = collegeData.getString(DoConfig.CAT_ID);
                disT.setText(discount);
                double subtotal = Double.parseDouble(grnT.getText().toString());
                double total = subtotal - Double.parseDouble(discount);
                grnT.setText(String.valueOf(total));
                disTK = disT.getText().toString();
                subTK = subT.getText().toString();
            }
        } catch (Exception e) {

        }
    }

    //    private void stockMaintain(){
//        homeViewModel = new HomeViewModel();
//        String session = homeViewModel.getSession(checkout.this);
//        sqliteDB sqlite_db = new sqliteDB(checkout.this);
//        try {
//            Cursor cursor = sqlite_db.getUerData("SELECT * FROM shopping_carts WHERE session_id " +
//                    "= '"+session+"'");
//            if (cursor.getCount() > 0){
//                if(cursor.moveToNext()){
//                    session = cursor.getString(cursor.getColumnIndex("session_data"));
//                }
//            }else {
//                Toast.makeText(checkout.this,"Session not found",Toast.LENGTH_LONG).show();
//            }
//        }catch (Exception e){
//        }
//        finally {
//            try {
//                sqlite_db.close();
//            }catch (Exception e){
//            }
//        }
//    }
    private void getOnlinedata() {
        try {
            homeViewModel = new HomeViewModel();
            String session = homeViewModel.getSession(checkout.this);
            String sql = "SELECT `shopping_carts`.`product_id`,`shopping_carts`.`quantity`,`product_productinfo`." +
                    "`product_name`,`product_productinfo`.`current_price`,`product_productinfo`.`shipping_id` FROM `shopping_carts` INNER JOIN " +
                    "`product_productinfo` ON `shopping_carts`.`product_id` = `product_productinfo`.id WHERE " +
                    "`shopping_carts`.`session_id` = '" + session + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.CHECK_PRO_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            shipping_id = operation.showCheck_data(response, checkout_pro_adapter);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {

        }
    }

    private void showDistrict() {
        try {
            String sql = "SELECT districts.district_name AS 'charge',`districts`.`id` FROM `districts`";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.DISTRICT_DELIVERY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            showDistrictList(response);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {

        }
    }

    private void showThana(String thID) {
        try {
            String sql = "SELECT thana_name AS 'charge',id FROM `thanas` WHERE `district_id` = '" + thID + "'";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.DISTRICT_DELIVERY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            showThanaList(response);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {

        }
    }

    private void showDistrictList(String response) {
        String id = "";
        String name = "";
        String quant = "";
        cat_model cat_models;
        try {
            districtList.clear();
            model.clear();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DoConfig.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(DoConfig.PRO_NAME);
                quant = collegeData.getString(DoConfig.PRO_CURRENT_PRICE);
                id = collegeData.getString(DoConfig.CAT_ID);
                cat_models = new cat_model(quant, name, id);
                districtList.add(cat_models);
                model.add(quant);
            }

        } catch (Exception e) {
        }
    }

    private void showThanaList(String response) {
        String id = "";
        String name = "";
        String quant = "";
        cat_model cat_models;
        try {
            thanaList.clear();
            thanamodel.clear();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DoConfig.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(DoConfig.PRO_NAME);
                quant = collegeData.getString(DoConfig.PRO_CURRENT_PRICE);
                id = collegeData.getString(DoConfig.CAT_ID);
                cat_models = new cat_model(quant, name, id);
                thanaList.add(cat_models);
                thanamodel.add(quant);
            }


        } catch (Exception e) {
        }
    }

    private void submitAction() {
        try {
            if (first_name.getText().toString().trim().equals("")) {
                first_name.setError("Empty First Name");
                Toast.makeText(checkout.this, "Empty First Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Phone.getText().toString().trim().equals("")) {
                Phone.setError("Empty Phone Number");
                Toast.makeText(checkout.this, "Empty Phone Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (district.getText().toString().trim().equals("") && districtid.equals("")) {
                district.setError("Select District");
                Toast.makeText(checkout.this, "Select District", Toast.LENGTH_SHORT).show();
                return;
            }
            if (fulladdress.getText().toString().trim().equals("")) {
                fulladdress.setError("Empty Full Address");
                Toast.makeText(checkout.this, "Empty Full Address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!cash_rd.isSelected() && !pay_type.equals("cash") && !online_id.isSelected() && !pay_type.equals("online")) {
                if (mobileNO.equals("")) {
                    Toast.makeText(checkout.this, "Empty Mobile No", Toast.LENGTH_SHORT).show();
                    Toast.makeText(checkout.this, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (trnID.equals("")) {
                    Toast.makeText(checkout.this, "Empty Transaction ID", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            submit();
        } catch (Exception e) {
        }
    }

    private void submit() {
        progressDialog = ProgressDialog.show(checkout.this, "", "Fetching", false, false);
        try {
            String first_nameS = "", phoneS = "", fulladdressS = "";
            session = homeViewModel.getSession(checkout.this);
            first_nameS = first_name.getText().toString();
            phoneS = Phone.getText().toString();
//            emailS = Email.getText().toString();
            fulladdressS = fulladdress.getText().toString();
            String thisDate = operation.getCreateDate();

            String sql = "INSERT INTO `delivery_infos` (`first_name`,`last_name`,`email`,`address`,`phone`," +
                    "`country`,`district_id`,`session_id`,`created_at`) VALUES ('" + first_nameS + "',''," +
                    "'"+email+"','" + fulladdressS + "','" + phoneS + "','BD','" + districtid + "','" + session + "','" + thisDate + "')";

            deliveryAdd(checkout.this, sql, progressDialog);
        } catch (Exception e) {
        }
    }

    public void close() {
        finish();
    }

    public void invoice_query(String response, String gid, Context context, ProgressDialog progressDialog) {
        session = homeViewModel.getSession(checkout.this);
        String sql1 = "INSERT INTO `invoices` (`invoice_id`,`delivery_id`,`guest_id`," +
                "`coupon_id`,`delivery_charge`,`payment_type`,`mobile_acc`,`trans_id`," +
                "`discount`,`sub_total`,`grand_total`,`session_id`,`status`" +
                ") VALUES ('";
        String sql2 = "','" + response + "','" + gid + "','" + id + "','" + del_ch + "','" + pay_type + "'," +
                "'" + mobileNO + "','" + trnID + "','" + disTK + "'," +
                "'" + subTK + "','" + totalTK + "'," +
                "'" + session + "','0')";

        operation.invoice_proccess(context, sql1, sql2, progressDialog, id, subTK, disTK, String.valueOf(del_ch), grnT.getText().toString(),pay_type);
    }

    public void deliveryAdd(Context context, String sql, ProgressDialog progressDialog) {

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.INSERT_DELIVERY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("failed") && !response.equals("problem")) {
//                                gid = homeViewModel.getGuestID(context);
//                                if(gid == null || gid.equals("")){
//                                    sign_in(context,response,progressDialog,"0");
////                                    online_gid(context,response);
//                                }else
                                invoice_query(response, gid, context, progressDialog);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
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

    private void online_gid(Context context, String delID, String username, String pass, ProgressDialog progressDialog, String check) {
        try {
            if (check.equals("1")) {
                progress = ProgressDialog.show(checkout.this, "", "Loading", false, false);
            }
//            String sql = "SELECT id FROM `guest` WHERE (email = '"+username+"' OR phone = '"+username+"') AND password = '"+pass+"'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.GET_LOGIN_INFO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (check.equals("1"))
                                progress.dismiss();
                            if (!response.equals("1") && !response.equals("") && !response.equals("{\"result\":[]}")) {
                                show_user_data(response, check, context, delID, progressDialog);

                            } else {
                                Toast.makeText(context, "Failed, Please try again", Toast.LENGTH_LONG).show();
                                sign_in(context, delID, progressDialog, check);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (check.equals("1"))
                        progress.dismiss();
                    else
                        progressDialog.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    sign_in(context, delID, progressDialog, check);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, username);
                    params.put(DoConfig.PRO_SIZE, pass);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    public void show_user_data(String response, String check, Context context, String delID, ProgressDialog progressDialog) {
        try {
            String firstname = "", lastname = "", phone = "", gid = "", Address = "";
            if (!check.equals("1")) {
                invoice_query(delID, response, context, progressDialog);
                homeViewModel.insertuser(context, firstname + " " + lastname, phone, "", pass, response);
            } else {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray(DoConfig.RESULT);
                for (int i = 0; i <= result.length(); i++) {
                    JSONObject collegeData = result.getJSONObject(i);
                    gid = collegeData.getString(DoConfig.CAT_ID);
                    firstname = collegeData.getString(DoConfig.PRO_NAME);
                    lastname = collegeData.getString(DoConfig.CAT_NAME);
//                email = collegeData.getString(config.EMAIL);
                    phone = collegeData.getString(DoConfig.PHONE);
                    Address = collegeData.getString(DoConfig.PRO_SALE_PRICE);

//                    invoice_query(delID, gid, context,progressDialog);
                    homeViewModel.insertuser(context, firstname + " " + lastname, phone, "", pass, gid);
                    first_name.setText(firstname);
                    Phone.setText(phone);
//                Email.setText(email);
                    fulladdress.setText(Address);

                }
            }
        } catch (Exception e) {
        }
    }

    private void sign_in(Context context, String response, ProgressDialog progressDialog, String check) {
        signin_view = LayoutInflater.from(checkout.this).inflate(R.layout.signin_dialog, null);
        phone_n = signin_view.findViewById(R.id.username_di);
        trn_n = signin_view.findViewById(R.id.password_di);
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
        dialogBuilder.setTitle("Sign In");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(signin_view);
        dialogBuilder.setNegativeButton("Sign Up", (dialog, which) -> {
            dialog.cancel();
            sign_up(context, response, progressDialog, check);
        });
        dialogBuilder.setPositiveButton("Sign In", (dialog, which) -> {
            username = phone_n.getText().toString();
            pass = trn_n.getText().toString();
            online_gid(context, response, username, pass, progressDialog, check);
        });
        dialogBuilder.setNeutralButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
            finish();
            if (!check.equals("1"))
                progressDialog.dismiss();
        });
        dialogBuilder.show();
    }

    public void sign_up(Context context, String response, ProgressDialog progressDialog, String check) {
        singup_view = LayoutInflater.from(checkout.this).inflate(R.layout.signup_dialog, null);
        firstname_ed = singup_view.findViewById(R.id.username_dis);
        phone_ed = singup_view.findViewById(R.id.phone_dis);
        email_ed = singup_view.findViewById(R.id.email_txt_dis);
        address_ed = singup_view.findViewById(R.id.address_ed);
        password_ed = singup_view.findViewById(R.id.vp_ed_dis);
        con_password_ed = singup_view.findViewById(R.id.vc_ed_dis);
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(checkout.this);
        dialogBuilder.setTitle("Sign Up");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setView(singup_view);
        dialogBuilder.setNegativeButton("Sign In", (dialog, which) -> {
            dialog.cancel();
            sign_in(context, response, progressDialog, check);
        });
        dialogBuilder.setPositiveButton("Sign Up", (dialog, which) -> {
            if (firstname_ed.getText().toString().trim().equals("")) {
                firstname_ed.setError("Empty First Name");
                return;
            }
            if (phone_ed.getText().toString().trim().equals("")) {
                phone_ed.setError("Empty Phone Number");
                return;
            }
            if (address_ed.getText().toString().trim().equals("")) {
                address_ed.setError("Empty Email");
                return;
            }
            if (password_ed.getText().toString().equals("")) {
                password_ed.setError("Empty Password");
                return;
            }
            if (password_ed.getText().toString().length() < 8) {
                password_ed.setError("Password must be >= 8 character");
                return;
            }
            if (!password_ed.getText().toString().equals(con_password_ed.getText().toString())) {
                con_password_ed.setError("Password didn't matched");
                return;
            }
            if (check.equals("1")) {
                progress = ProgressDialog.show(checkout.this, "", "Loading", false, false);
            }
            check_phone_email(context, firstname_ed.getText().toString(), "", phone_ed.getText().toString()
                    , email_ed.getText().toString(), password_ed.getText().toString(), address_ed.getText().toString(), response, check, progressDialog, progress);
            showSignGuest(firstname_ed.getText().toString(), "", phone_ed
                    .getText().toString(), email_ed.getText().toString(), address_ed.getText().toString());
        });
        dialogBuilder.setNeutralButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
            finish();
            if (!check.equals("1"))
                progressDialog.dismiss();
        });
        dialogBuilder.show();
    }

    public void getGuestData(String sql) {
        try {

            String query = "SELECT * FROM guest WHERE id = '" + sql + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.GUEST_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                showGuest(response);
                            } else
                                Toast.makeText(checkout.this, response, Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, query);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    private void showGuest(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(DoConfig.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                first_name.setText(collegeData.getString(DoConfig.PRO_NAME));
                Phone.setText(collegeData.getString(DoConfig.PRO_DISCOUNT_PRICE));
                email = collegeData.getString(DoConfig.EMAIL);
                fulladdress.setText(collegeData.getString(DoConfig.PRO_CURRENT_PRICE));
            }
        } catch (Exception e) {
        }
    }

    public void showSignGuest(String firstname, String lastname, String phone, String email, String address) {
        try {
            first_name.setText(firstname);
            Phone.setText(phone);
//                Email.setText(email);
            fulladdress.setText(address);
        } catch (Exception e) {
        }
    }

    public void getCharge(String sql, String shipping) {
        try {
//            String query = "SELECT SUM(`charge`) AS 'id' FROM `delivery_charges` WHERE `district_id` = '"+sql+"' AND `shipping_id` IN ("+shipping+")";
            String query = "SELECT SUM(delivery_charges.`charge`) AS 'id'" +
                    " FROM delivery_charges " +
                    "INNER JOIN zone_districts ON zone_districts.`zone_id` = delivery_charges.`zone_id` " +
                    "WHERE delivery_charges.`shipping_id` IN (" + shipping + ") AND zone_districts.`thana_id` = '" + sql + "'";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response != null && !response.equals("1")) {
                                try {
                                    if (response.equals("")) {
                                        deliT.setText("0");
                                        gr_t = Double.parseDouble(grnT.getText().toString());
                                        Total = gr_t - del_ch;
                                        del_ch = 0;
                                        grnT.setText(String.valueOf(Total));
                                        totalTK = grnT.getText().toString();
                                        subTK = subT.getText().toString();
                                    } else {
                                        deliT.setText(response);
                                        gr_t = Double.parseDouble(grnT.getText().toString());
                                        Total = gr_t - del_ch;
                                        del_ch = Double.parseDouble(response);
                                        Total = del_ch + Total;
                                        grnT.setText(String.valueOf(Total));
                                        totalTK = grnT.getText().toString();
                                        subTK = subT.getText().toString();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                deliT.setText("0");
                                gr_t = Double.parseDouble(grnT.getText().toString());
                                Total = gr_t - del_ch;
                                del_ch = 0;
                                grnT.setText(String.valueOf(Total));
                                totalTK = grnT.getText().toString();
                                subTK = subT.getText().toString();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(checkout.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.QUERY, query);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(checkout.this);
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
        return super.onSupportNavigateUp();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
//            cat_model cat_model = districtList.get(position);
//            gr_t = Double.parseDouble(grnT.getText().toString());
//            int item;
//            String inQuery = "";
            cat_model cat_model;
            int id_dis = getPosition(district.getText().toString());
            if (id_dis != -1) {
                districtid = String.valueOf(id_dis);
            } else
                districtid = "0";
//            gr_t = Double.parseDouble(grnT.getText().toString());
//            Total =  gr_t - del_ch;

//            Total = del_ch + Total;
//            deliT.setText(cat_model.getId());
//            grnT.setText(String.valueOf(Total));
            thana.setText("");
            thanaList.clear();
            thanamodel.clear();
            showThana(districtid);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(checkout.this,
                    R.layout.item_name, thanamodel);
            thana.setAdapter(dataAdapter);
            totalTK = grnT.getText().toString();
            subTK = subT.getText().toString();
        } catch (Exception e) {
        }
    }

    private int getPosition(String pro_model) {
        try {
            for (cat_model x : districtList) {
                if (x.getImage().equals(pro_model)) {
                    return Integer.parseInt(x.getId().trim());
                }
            }
        } catch (Exception e) {
        }
        return -1;
    }

    public void insertuserData(Context context, String firstname, String lastname, String phone, String email,
                               String password, String address, String reponse, String check, ProgressDialog progressDialog, ProgressDialog progress) {
        try {
            homeViewModel = new HomeViewModel();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.GET_GUEST_REG,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (check.equals("1"))
                                progress.dismiss();
                            if (!response.equals("Could not Registered in online") && !response.equals("")) {
                                if (check.equals("0")) {
                                    invoice_query(reponse, response, context, progressDialog);
                                }
//                                checkout.show_user_data(response,check,context,reponse,progressDialog);
                                homeViewModel.insertuser(context, firstname + " " + lastname, phone, email, password, response);
                            } else {
                                Toast.makeText(context, "Failed to Sign up", Toast.LENGTH_LONG).show();
                                sign_up(context, reponse, progressDialog, check);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (check.equals("1"))
                        progress.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    sign_up(context, reponse, progressDialog, check);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(DoConfig.FIRST_N, firstname + " " + lastname);
                    params.put(DoConfig.LAST_N, "");
                    params.put(DoConfig.EMAIL, email);
                    params.put(DoConfig.PHONE, phone);
                    params.put(DoConfig.ADDRESS, address);
                    params.put(DoConfig.PASS, password);
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

    public void check_phone_email(Context context, String firstname, String lastname, String phone, String email,
                                  String password, String address, String reponse, String check, ProgressDialog progressDialog, ProgressDialog progress) {
        try {
            String sql = "SELECT id FROM guest WHERE phone = '" + phone + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DoConfig.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.equals("1")) {
                                if (check.equals("0")) {
                                    Toast.makeText(context, "Phone already taken", Toast.LENGTH_LONG).show();
                                    sign_up(context, reponse, progressDialog, check);
                                } else {
                                    Toast.makeText(context, "Phone already taken", Toast.LENGTH_LONG).show();
                                    sign_up(context, reponse, progressDialog, check);
                                    progress.dismiss();
                                }
                            } else {
                                insertuserData(context, firstname, lastname, phone, email, password, address, reponse, check, progressDialog, progress);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (check.equals("1"))
                        progress.dismiss();
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    sign_up(context, reponse, progressDialog, check);
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