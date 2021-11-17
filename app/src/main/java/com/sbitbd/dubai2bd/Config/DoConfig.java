package com.sbitbd.dubai2bd.Config;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sbitbd.dubai2bd.Adapter.order_adapter;
import com.sbitbd.dubai2bd.Adapter.order_model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DoConfig {

    public static final String CAT_DATA = "https://dubai2bd.com/java/getCategory.php";
    public static final String SIX_DMS = "https://dubai2bd.com/java/six_dms.php";
    public static final String FORGET = "https://dubai2bd.com/java/forget_password.php";
    public static final String GUEST_REG = "https://dubai2bd.com/java/guest_register.php";
    public static final String GET_GUEST_REG = "https://dubai2bd.com/java/getData_register.php";
//    public static final String UPDATE_GUEST = "http://salesman-bd.com/java/update_guest.php";
    public static final String UPDATE_GUEST = "https://dubai2bd.com/java/fileUpload.php?apicall=uploadpic";
//    public static final String UPDATE_GUEST = "http://192.168.0.103/salesman-bd/update_guest.php";
    public static final String GUEST_DATA = "https://dubai2bd.com/java/getGuestData.php";
//    public static final String GUEST_DATA = "http://192.168.0.116/salesman-bd/getGuestData.php";
    public static final String ORDER_DATA = "https://dubai2bd.com/java/getOrderData.php";
    public static final String COUPON = "https://dubai2bd.com/java/getCoupon.php";
    public static final String GET_ID = "https://dubai2bd.com/java/getID.php";
    public static final String RESET_PASS = "https://dubai2bd.com/java/reset_password.php";
    public static final String GET_LOGIN_INFO = "https://dubai2bd.com/java/getLoginInfo.php";
    public static final String LOGIN = "https://dubai2bd.com/java/getLoginUser.php";
    public static final String SELLER_LOGIN = "https://dubai2bd.com/java/getLoginSeller.php";
    public static final String PROFILE = "https://dubai2bd.com/java/getProfile.php";
    public static final String DISTRICT_DELIVERY = "https://dubai2bd.com/java/getDistrict_delivery.php";
    public static final String CART_DATA = "https://dubai2bd.com/java/getCart.php";
    public static final String INSERT = "https://dubai2bd.com/java/insert.php";
    public static final String INSERT_DELIVERY = "https://dubai2bd.com/java/insert_delivery.php";
    public static final String INSERT_INVOICE = "https://dubai2bd.com/java/invoice_insert.php";
//    public static final String INSERT_INVOICE = "http://192.168.0.108/salesman-bd/invoice_insert.php";
    public static final String PRO_DATA = "https://dubai2bd.com/java/getProduct.php";
//    public static final String PRO_DATA = "http://192.168.0.116/salesman-bd/getProduct.php";
    public static final String PRO_DES = "https://dubai2bd.com/java/getProDescription.php";
    public static final String SLIDER_IMG = "https://dubai2bd.com/java/getData.php";
    public static final String CHECK_PRO_DATA = "https://dubai2bd.com/java/getCheckData.php";
    public static final String CAT_IMG_URL = "https://dubai2bd.com/public/categoryImage/";
    public static final String SELLER_IMG_URL = "https://dubai2bd.com/public/seller/";
    public static final String ITEM_IMG_URL = "https://dubai2bd.com/public/itemImage/";
    public static final String PRO_IMG_URL = "https://dubai2bd.com/public/productImage/";
    public static final String SLIDER_IMG_URL = "https://dubai2bd.com/public/sliderImage/";
    public static final String GUEST_IMAGE = "https://dubai2bd.com/public/guestImage/";
    public static final String BANNER = "https://dubai2bd.com/public/Frontend/";
    public static final String CAT_IMG = "cat_img";
    public static final String CAT_NAME = "cat_name";
    public static final String SHIPPING = "shipping";
    public static final String CAT_ID = "id";
    public static final String RESULT = "result";
    public static final String QUERY = "query";
    public static final String PRO_NAME = "pro_name";
    public static final String PRO_SALE_PRICE = "sale_price";
    public static final String PRO_DISCOUNT_PRICE = "discount_price";
    public static final String PRO_CURRENT_PRICE = "current_price";
    public static final String PRO_IMAGE = "image";
    public static final String PRO_IMAGE_NAME = "image_name";
    public static final String PRO_SIZE = "size";
    public static final String EMAIL = "email";
    public static final String FIRST_N = "firstname";
    public static final String LAST_N = "lastname";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String PASS = "pass";
    public static final String ONE = "one";
    public static final String TWO = "two";
    public static final String THREE = "three";
    public static final String FOUR = "four";
    public static final String FIVE = "five";
    public static final String SIX = "six";
    public static final String MER_TXNID = "mer_txnid";
    public static final String CUS_NAME = "cus_name";
    public static final String cus_phone = "cus_phone";
    public static final String cus_email = "cus_email";
    public static final String processing_charge = "processing_charge";
    public static final String amount = "amount_bdt";
    public static final String payment_processor = "payment_processor";
    public static final String cardnumber = "cardnumber";
    public static final String currency_merchant = "currency_merchant";
    public static final String convertion_rate = "convertion_rate";
    public static final String ip = "ip";
    public static final String other_amount = "amount";
    public static final String pay_status = "pay_status";
    public static final String pg_txnid = "pg_txnid";
    public static final String currency = "currency";
    public static final String rec_amount = "rec_amount";
    public static final String date_processed = "date_processed";
    public static final String bank_trxid = "bank_trxid";
    public static final String payment_type = "payment_type";
    public static final String risk_level = "risk_level";
    public static final String error_code = "error_code";
    private List<order_model> order_modelsList = new ArrayList<>();

    public String encrypt(String value) {
        try {
            String key, initVector;
            key = "Jar12345Jar12345";
            initVector = "RandomInitVector";
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(1, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(encrypted);
            }else
                return new String(encrypted);
//            encodeBase64String(encrypted);getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
        }
        return null;
    }
    public void OrderData(Context context, String query, RecyclerView recyclerView, ConstraintLayout materialCardView){
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ORDER_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1") && !response.equals("")){
                                showOrderJson(response,recyclerView,context);
                            }else
                                materialCardView.setVisibility(View.VISIBLE);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    materialCardView.setVisibility(View.VISIBLE);
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(QUERY, query);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
    public void online_gid(Context context,String id,RecyclerView recyclerView,ConstraintLayout materialCardView){
        try {

            String sql1 = "SELECT `invoices`.`invoice_id`,`shopping_carts`.`product_id`,`product_productinfo`.`product_name`," +
                    "`invoices`.`created_at`,`invoices`.`status` FROM `invoices` INNER JOIN `shopping_carts` ON `invoices`.`session_id` = " +
                    "`shopping_carts`.`session_id` INNER JOIN `product_productinfo` ON `shopping_carts`.`product_id` = " +
                    "`product_productinfo`.`id` WHERE `invoices`.`guest_id` = '1' AND `invoices`.`session_id` LIKE '"+id+"%' ORDER BY `invoices`.`id` DESC";
            OrderData(context,sql1,recyclerView,materialCardView);

//            String sql = "SELECT id FROM `guest` WHERE email = 'info@salesman-bd.com'";
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_ID,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            if(!response.equals("1")){
//
//                            }else
//                                Toast.makeText(context,"Please Sign in",Toast.LENGTH_LONG).show();
//
//
//                        }
//                    }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put(QUERY, sql);
//                    return params;
//                }
//            };
//            RequestQueue requestQueue = Volley.newRequestQueue(context);
//            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
    private void showOrderJson(String response,RecyclerView recyclerView,Context context){
        String id = "";
        String name = "";
        String date = "";
        String status = "";
        order_model order_model;
        int position = -1;
        order_adapter order_adapter = new order_adapter(context);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(RESULT);
            for (int i = 0; i <= result.length(); i++) {
                JSONObject collegeData = result.getJSONObject(i);
                name = collegeData.getString(PRO_NAME);
                status = collegeData.getString(PRO_IMAGE);
                id = collegeData.getString(CAT_ID);
                date = collegeData.getString(PRO_SIZE);
                order_model = new order_model(id,name,date,status);

                position = order_adapter.getPosition(order_model);
                if (position == -1){
                    order_adapter.adduser(order_model);
                    order_modelsList.add(order_model);
                }else {
                    order_model or_model = order_modelsList.get(position);
                    order_model = new order_model(id,name+","+or_model.getPro_name(),date,status);
                    order_adapter.updateUser(order_model);
                    order_modelsList.set(position,order_model);
                }

                recyclerView.setAdapter(order_adapter);
            }
        } catch (Exception e) {
        }
    }
    public void Update(Context context, String query){
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ORDER_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.equals("1")){
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(QUERY, query);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        }catch (Exception e){
        }
    }
}
