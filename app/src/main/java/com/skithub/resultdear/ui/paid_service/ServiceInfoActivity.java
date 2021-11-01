package com.skithub.resultdear.ui.paid_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.skithub.resultdear.R;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.JsonElement;
import com.skithub.resultdear.database.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceInfoActivity extends AppCompatActivity {


    private ApiInterface apiInterface;
    TextView freePlanName,freePlanPrice,paidPlaneName,paidPlanePrice,proPlanName,proPlanPrice;
    TextView licenseNumber,startedDate,expireDate,currentPlane;
    TextView prolicenseNumber,prostartedDate,proexpireDate,procurrentPlane;
    private LinearLayout viplicenseLayout,prolicenseLayout,noLicenseLayout;
    private SpinKitView spinKitView;
    private ImageView whatsAppBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_info);

        getSupportActionBar().setTitle(getString(R.string.paid_activity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noLicenseLayout = findViewById(R.id.noLicenseLayout);
        spinKitView = findViewById(R.id.spin_kit);
        whatsAppBtn = findViewById(R.id.whatsAppBtn);
        apiInterface= RetrofitClient.getApiClient().create(ApiInterface.class);
        currentPlane = findViewById(R.id.currentPlane);
        freePlanName = findViewById(R.id.freePlanName);
        freePlanPrice = findViewById(R.id.freePriceTxt);
        paidPlaneName = findViewById(R.id.paidPlaneName);
        paidPlanePrice = findViewById(R.id.paidPriceTxt);
        procurrentPlane = findViewById(R.id.ProcurrentPlane);
        proPlanName = findViewById(R.id.proPlaneName);
        proPlanPrice = findViewById(R.id.proPriceTxt);

        viplicenseLayout = findViewById(R.id.viplicenseLayout);
        prolicenseLayout = findViewById(R.id.prolicenseLayout);

        licenseNumber = findViewById(R.id.licenseNumberTxt);
        startedDate = findViewById(R.id.licenseStartedDate);
        expireDate = findViewById(R.id.licenseExpireDate);

        prolicenseNumber = findViewById(R.id.ProlicenseNumberTxt);
        prostartedDate = findViewById(R.id.ProlicenseStartedDate);
        proexpireDate = findViewById(R.id.ProlicenseExpireDate);

        getPostListByPagination(getIntent().getStringExtra("userID"));

        whatsAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=918100316072";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }


    private void getPostListByPagination(String key) {
        Call<JsonElement> call=apiInterface.getPostSearch(key);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                if (response.isSuccessful() && response.code()==200) {

                    try {
                        JSONArray rootArray=new JSONArray(response.body().toString());

                        for (int i=0; i<rootArray.length(); i++) {

                            spinKitView.setVisibility(View.GONE);
                            String license_number = rootArray.getJSONObject(i).getString("license_number");
                            String license_number_pro = rootArray.getJSONObject(i).getString("license_number_pro");
                            String freename = rootArray.getJSONObject(i).getString("free_name");
                            String freeprice = rootArray.getJSONObject(i).getString("free_price");
                            String paidprice = rootArray.getJSONObject(i).getString("paid_price");
                            String paidname = rootArray.getJSONObject(i).getString("paid_name");
                            String proprice = rootArray.getJSONObject(i).getString("pro_price");
                            String proname = rootArray.getJSONObject(i).getString("pro_name");
                            freePlanName.setText(freename);
                            freePlanPrice.setText(freeprice+" ₹");
                            paidPlaneName.setText(paidname);
                            paidPlanePrice.setText(paidprice+" ₹");

                            proPlanName.setText(proname);
                            proPlanPrice.setText(proprice+" ₹");

                            if (license_number.length() > 5){
                                viplicenseLayout.setVisibility(View.VISIBLE);
                                String starteddate = rootArray.getJSONObject(i).getString("started_date");
                                String expiredate = rootArray.getJSONObject(i).getString("expire_date");

                                licenseNumber.setText(license_number);
                                startedDate.setText(starteddate);
                                expireDate.setText(expiredate);

                                currentPlane.setText(paidname);
                            }
                            if (license_number_pro.length() > 5){
                                prolicenseLayout.setVisibility(View.VISIBLE);
                                String starteddate = rootArray.getJSONObject(i).getString("started_date_pro");
                                String expiredate = rootArray.getJSONObject(i).getString("expire_date_pro");

                                prolicenseNumber.setText(license_number_pro);
                                prostartedDate.setText(starteddate);
                                proexpireDate.setText(expiredate);

                                procurrentPlane.setText(proname);
                            }
                            if(license_number_pro.length() < 5 && license_number.length() < 5){
                                noLicenseLayout.setVisibility(View.VISIBLE);
                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        spinKitView.setVisibility(View.GONE);
                        Toast.makeText(ServiceInfoActivity.this, "error found:- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    spinKitView.setVisibility(View.GONE);
                    Toast.makeText(ServiceInfoActivity.this, "Unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                spinKitView.setVisibility(View.GONE);
                Toast.makeText(ServiceInfoActivity.this, "error:- "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.option_moreApps:
                String url = "https://play.google.com/store/apps/developer?id=Sikder+International+iT+Hub";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            case R.id.option_share:
                String url2 = "https://play.google.com/store/apps/details?id=com.skithub.resultdear";
                Intent i2 = new Intent(Intent.ACTION_VIEW);
                i2.setData(Uri.parse(url2));
                startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}