package com.sbitbd.dubai2bd.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.dubai2bd.Adapter.pro_model;
import com.sbitbd.dubai2bd.Adapter.product_adapter;
import com.sbitbd.dubai2bd.Config.DoConfig;
import com.sbitbd.dubai2bd.Config.sqliteDB;
import com.sbitbd.dubai2bd.ImageSlider.productAdapter;
import com.sbitbd.dubai2bd.ImageSlider.sliderModel;
import com.sbitbd.dubai2bd.R;
import com.sbitbd.dubai2bd.ui.cart_operation.operation;
import com.sbitbd.dubai2bd.ui.checkout.checkout;
import com.sbitbd.dubai2bd.ui.home.HomeViewModel;
import com.sbitbd.dubai2bd.ui.sh_cart.sh_cart;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.nikartm.support.BadgePosition;
import ru.nikartm.support.ImageBadgeView;

public class product_descripton extends AppCompatActivity {

    private productAdapter sliderAdapter;
    private SliderView sliderView;
    List<sliderModel> modelList = new ArrayList<>();
    String name, price, id, disprice;
    String image;
    private TextView pro_name, pro_price, pro_dis, min_quant, stock_status, menu_txt, quantity;
    private WebView pro_discrip;
    private Button pro_view_btn, pro_add_btn, pro_wish_btn, back_btn, decre, incre;
    private ImageBadgeView cart_btn;
    private operation operation;
    private DoConfig config;
    private pro_model cart_model;
    private static String quant;
    private RecyclerView recyclerView;
    private product_adapter product_adapter;
    private HomeViewModel homeViewModel = new HomeViewModel();
    private Toolbar toolbar;
    private sliderModel sliderModel;
    private List<String> size = new ArrayList<>();
    private List<String> color = new ArrayList<>();
    private AutoCompleteTextView color_a, size_a;
    private int min, Max;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_descripton);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        toolbar = findViewById(R.id.des_topAppBar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        config = new DoConfig();
        operation = new operation();
        find_component();
    }

    private void find_component() {
        pro_name = findViewById(R.id.pro_name);
        pro_price = findViewById(R.id.pro_price);
        sliderView = findViewById(R.id.pro_imageSlider);
        pro_discrip = findViewById(R.id.des_webview);
        WebSettings webSettings = pro_discrip.getSettings();
        webSettings.setJavaScriptEnabled(true);
        pro_view_btn = findViewById(R.id.pro_view_btn);
        pro_add_btn = findViewById(R.id.pro_add_btn);
        pro_dis = findViewById(R.id.pro_dis_price);
        min_quant = findViewById(R.id.min_quant);
        stock_status = findViewById(R.id.stock_status);
        recyclerView = findViewById(R.id.releted_proz_rec);
        back_btn = findViewById(R.id.pro_des_back);
        cart_btn = findViewById(R.id.pro_des_menu);
        menu_txt = findViewById(R.id.pro_des_text);
        quantity = findViewById(R.id.quant_num);
        incre = findViewById(R.id.pro_quant_incre);
        decre = findViewById(R.id.pro_quant_decre);
        color_a = findViewById(R.id.color_t);
        size_a = findViewById(R.id.size_t);

        recyclerView.setNestedScrollingEnabled(false);
        GridLayoutManager manager = new GridLayoutManager(product_descripton.this, 2);
        recyclerView.setLayoutManager(manager);
        product_adapter = new product_adapter(product_descripton.this);
        pro_dis.setPaintFlags(pro_dis.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        setcomponent();
        count();
        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(product_descripton.this, sh_cart.class));
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pro_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation.cartInsert(product_descripton.this, id, quant,String.valueOf(min), v, color_a.getText().toString(), size_a.getText().toString());
                startActivity(new Intent(product_descripton.this, checkout.class));

                count();
                if (Max >= Integer.parseInt(quant)) {
                    Max = Max - Integer.parseInt(quant);
                    setbtn(min,Max);
                }
            }
        });
        pro_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operation.cartInsert(product_descripton.this, id, quant,String.valueOf(min), v, color_a.getText().toString(), size_a.getText().toString());
                count();
                if (Max >= Integer.parseInt(quant)) {
                    Max = Max - Integer.parseInt(quant);

                    setbtn(min,Max);
                }
            }
        });
        color_a.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getid(color_a.getText().toString(), size_a.getText().toString(), min);
            }
        });
        size_a.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getid(color_a.getText().toString(), size_a.getText().toString(), min);
            }
        });
    }

    private void count(){
        sqliteDB sqlite_db = new sqliteDB(product_descripton.this);
        String session = "";
        try {
            homeViewModel = new HomeViewModel();
            session = homeViewModel.getSession(product_descripton.this);
            Cursor cursor = sqlite_db.getUerData("SELECT * FROM shopping_carts WHERE session_id = '" + session + "'");
            if (cursor.getCount() > 0) {
                cart_btn.setBadgeValue(cursor.getCount())
                        .setBadgeOvalAfterFirst(true)
                        .setMaxBadgeValue(99)
                        .setBadgeBackground(getResources().getDrawable(R.drawable.round_corner))
                        .setBadgePosition(BadgePosition.TOP_LEFT)
                        .setBadgeTextStyle(Typeface.NORMAL)
                        .setShowCounter(true)
                        .setBadgePadding(2);
            }else {
                cart_btn.setMaxBadgeValue(99)
                        .setBadgePosition(BadgePosition.TOP_LEFT)
                        .setBadgeTextStyle(Typeface.NORMAL)
                        .setShowCounter(false)
                        .setBadgePadding(2);
            }
        }catch (Exception e){
        }finally {
            try {
                sqlite_db.close();
            }catch (Exception e){
            }
        }
    }
    private void setcomponent() {
        try {
            name = getIntent().getStringExtra("name");
            price = getIntent().getStringExtra("price");
            image = getIntent().getStringExtra("image");
            id = getIntent().getStringExtra("id");
            disprice = getIntent().getStringExtra("dis_price");

            if (disprice.equals("") || disprice.equals("0.00") || disprice.equals("0") || Double.parseDouble(disprice)
                    == Double.parseDouble(price) || Double.parseDouble(disprice) < Double.parseDouble(price)) {
                pro_dis.setText("");
            } else {
                pro_dis.setText("৳ " + disprice);
            }
            getData();
//            desQuery();
            if (name != null) {
                pro_name.setText(name);
                menu_txt.setText(name);
            }
            if (price != null)
                pro_price.setText("৳ " + price);
            if (image != null) {
//                modelList.add(new sliderModel(image));
//                sliderAdapter = new productAdapter(product_descripton.this,modelList);
//                sliderView.setSliderAdapter(sliderAdapter);
            }
            showImage();

        } catch (Exception e) {
        }
    }

    private void getData() {
        try {
            String sql = "SELECT `product_size`.`size` AS 'id',`product_color`.`color` AS 'district_id' FROM `product_productinfo` INNER JOIN `product_size` ON " +
                    "`product_productinfo`.`id` = `product_size`.`product_id` INNER JOIN `product_color` ON `product_productinfo`.`id` = `product_color`.`product_id` " +
                    "WHERE `product_productinfo`.`id` = '" + id + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.DISTRICT_DELIVERY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1"))
                                showColor_size(response);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(product_descripton.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(product_descripton.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {

        }
    }

    private void getid(String color, String size, int min) {
        try {
            progressDialog = ProgressDialog.show(product_descripton.this, "", "Loading", false, false);
            String sql = "SELECT productstocks.quentity-sum(shopping_carts.quantity) as 'id' FROM " +
                    "`shopping_carts` inner join productstocks on shopping_carts.product_id = productstocks." +
                    "product_id where shopping_carts.product_id = '"+id+"' and shopping_carts.size = '"+size+"' " +
                    "and shopping_carts.color = '"+color+"' and productstocks.size = shopping_carts." +
                    "size and productstocks.color = shopping_carts.color";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GET_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            try {
                                if (response != null && !response.trim().equals("0")) {
                                    stock_status.setText(R.string.in_stock);
                                    stock_status.setTextColor(getResources().getColor(R.color.stock_green));
                                    pro_add_btn.setEnabled(true);
                                    pro_view_btn.setEnabled(true);
                                    Max = Integer.parseInt(response.trim());
                                    setbtn(min, Integer.parseInt(response.trim()));
                                } else if (response != null && Integer.parseInt(response.trim()) <= 0) {
                                    stock_out();
                                } else {
                                    stock_out();
                                }
                            } catch (Exception e) {
                                stock_out();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(product_descripton.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(product_descripton.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {

        }
    }

    private void stock_out() {
        stock_status.setText(R.string.out_stock);
        stock_status.setTextColor(getResources().getColor(R.color.main_color));
        pro_add_btn.setEnabled(false);
        pro_view_btn.setEnabled(false);
    }

    private void desQuery(String size, String color) {

        try {
            String sql = "SELECT `product_measurement`.`measurement_type` AS 'address',`product_productinfo`.`min_qunt` AS 'first_name',`product_productinfo`.`product_details` AS 'last_name'," +
                    "  `product_productinfo`.`stock_status` AS 'email',`product_productinfo`.`category_id` AS 'phone'" +
                    "   FROM `product_productinfo` INNER JOIN product_measurement ON product_productinfo.measurement_type = product_measurement.id " +
                    "WHERE `product_productinfo`.id = '" + id + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.GUEST_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (!response.equals("1") && !response.equals("")) {
                                cart_model = operation.showProDesJSON(response);
                                try {
                                    if (cart_model.getProName() == null || Integer.parseInt(cart_model.getProName()) <= 1) {
                                        min_quant.setText("1 " + cart_model.getDis_val());
                                        quant = "1";
                                        min = 1;
                                    } else {
                                        min_quant.setText(cart_model.getProName() + " " + cart_model.getDis_val());
                                        quant = cart_model.getProName();
                                        min = Integer.parseInt(cart_model.getProName());
                                    }
                                    if (cart_model.getDis_price() != null && cart_model.getDis_price().equals("1")) {
                                        getid(color, size, min);
                                        stock_status.setText(R.string.in_stock);
                                        stock_status.setTextColor(getResources().getColor(R.color.stock_green));
                                        pro_add_btn.setEnabled(true);
                                        pro_view_btn.setEnabled(true);
//                                        Max = Integer.parseInt(response);
//                                        setbtn(min, 1000);
//                                        stock_status.setText("In Stock");
//                                        stock_status.setTextColor(getResources().getColor(R.color.stock_green));
//                                        pro_add_btn.setEnabled(true);
//                                        pro_view_btn.setEnabled(true);
                                    } else {
                                        stock_status.setText(R.string.out_stock);
                                        stock_status.setTextColor(getResources().getColor(R.color.main_color));
                                        pro_add_btn.setEnabled(false);
                                        pro_view_btn.setEnabled(false);
                                    }
                                } catch (Exception e) {
                                    min_quant.setText("1 " + cart_model.getDis_val());
                                    quant = "1";
                                    min = 1;
                                    getid(color, size, min);
                                    stock_status.setText(R.string.in_stock);
                                    stock_status.setTextColor(getResources().getColor(R.color.stock_green));
                                    pro_add_btn.setEnabled(true);
                                    pro_view_btn.setEnabled(true);
//                                    Max = Integer.parseInt(response);
//                                    setbtn(min, 1000);
                                }
                                try {
                                    if (cart_model.getPrice() != null && !cart_model.getPrice().equals("")) {
                                        show_product(cart_model.getPrice());
                                    }
                                } catch (Exception e) {
                                }
                                String encodedHtml = Base64.encodeToString(cart_model.getSize().getBytes(),
                                        Base64.NO_PADDING);
                                pro_discrip.loadData(encodedHtml, "text/html", "base64");
//                                pro_discrip.loadData("<html><body>" + cart_model.getSize() + "</body></html>",
//                                        "text/html; charset=utf-8", "UTF-8");
                            } else {
                                stock_status.setText(R.string.out_stock);
                                stock_status.setTextColor(getResources().getColor(R.color.main_color));
                                pro_add_btn.setEnabled(false);
                                pro_view_btn.setEnabled(false);
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(product_descripton.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(product_descripton.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
    }

    public int getPositionC(String color1){
        try {
            for (String x:color){
                if(x.equals(color1)){
                    return color.indexOf(x);
                }
            }
        }catch (Exception e){
        }
        return -1;
    }
    public int getPositionS(String size1){
        try {
            for (String x:size){
                if(x.equals(size1)){
                    return size.indexOf(x);
                }
            }
        }catch (Exception e){
        }
        return -1;
    }

    private void showColor_size(String response) {
        try {
            color.clear();
            size.clear();
            String colors = "", sizes = "";
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            int position_c = -1,position_s = -1;

            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                if (i == 0) {
                    color_a.setText(collegeData.getString(config.PRO_NAME));
                    size_a.setText(collegeData.getString(config.CAT_ID));
                    colors = collegeData.getString(config.PRO_NAME);
                    sizes = collegeData.getString(config.CAT_ID);
                    desQuery(sizes, colors);
                }
                position_c = getPositionC(collegeData.getString(config.PRO_NAME));
                position_s = getPositionS(collegeData.getString(config.CAT_ID));
                if (position_c == -1)
                    color.add(collegeData.getString(config.PRO_NAME));
                if (position_s == -1)
                    size.add(collegeData.getString(config.CAT_ID));
            }
            setAdap();
        } catch (Exception e) {
            setAdap();
        }
    }

    private void setAdap() {
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(product_descripton.this,
                R.layout.item_name, color);
        color_a.setAdapter(dataAdapter1);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(product_descripton.this,
                R.layout.item_name, size);
        size_a.setAdapter(dataAdapter2);
    }

    private void setbtn(int min, int max) {
        try {
            quantity.setText(String.valueOf(min));
            incre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(quantity.getText().toString()) < max) {
                        int oldquant = Integer.parseInt(quantity.getText().toString());
                        oldquant++;
                        quantity.setText(String.valueOf(oldquant));
                        quant = String.valueOf(oldquant);
                    }
                }
            });
            decre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(quantity.getText().toString()) > min) {
                        int oldquant = Integer.parseInt(quantity.getText().toString());
                        oldquant--;
                        quantity.setText(String.valueOf(oldquant));
                        quant = String.valueOf(oldquant);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    private void showImage() {
        try {

            String sql = "SELECT image FROM `product_images` WHERE `product_id` = '" + id + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.SLIDER_IMG,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            modelList = showimgJson(response);
                            sliderAdapter = new productAdapter(product_descripton.this, modelList);
                            sliderView.setSliderAdapter(sliderAdapter);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(product_descripton.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(product_descripton.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINDEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }


    public List<sliderModel> showimgJson(String response) {
        List<sliderModel> modelList = new ArrayList<>();
        String img = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(config.RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                img = collegeData.getString(config.PRO_IMAGE);
                sliderModel = new sliderModel(config.PRO_IMG_URL + img);
                modelList.add(sliderModel);
            }
        } catch (Exception e) {
        }
        return modelList;
    }

    private void show_product(String id) {
        try {
            String sql = "SELECT `product_productinfo`.`id`,`product_productinfo`.`product_name`," +
                    "`product_productinfo`.`sale_price`,`product_productinfo`.`discount_price`," +
                    "`product_productinfo`.`current_price`,`product_productinfo`.`image` " +
                    "FROM `product_productinfo` WHERE product_productinfo.category_id = '" + id + "'";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, config.PRO_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")) {
                                homeViewModel.showProJSON(response, cart_model, product_adapter, product_descripton.this);
                            } else {
                                Toast.makeText(product_descripton.this, "Not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(product_descripton.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.QUERY, sql);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(product_descripton.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
        }

        recyclerView.setAdapter(product_adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        count();
    }
}