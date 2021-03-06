package com.emmu.webserviceretrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMain extends AppCompatActivity {
    private Button btnSubmit;
    String responseTxt;
    Activity activity;
    ArrayList<Country> countries = new ArrayList<Country>();
    private ProgressDialog progressDialog;
    ListView listView;

    //base url
    public static final String BASE_URL = "https://cdn.rawgit.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_main);

        activity = this;
        btnSubmit = (Button) findViewById(R.id.btnsubmit);
        listView = (ListView) findViewById(R.id.androidlist);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countries.clear();
                progressDialog = new ProgressDialog(RetrofitMain.this);
                progressDialog.setMessage("Fetching country data");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getWebServiceResponseData();
            }
        });
    }

    protected void getWebServiceResponseData() {
        //creating retrofit adapter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating an object of our api interface
        CountryArrayAPI api = retrofit.create(CountryArrayAPI.class);
        Call<List<Country>> call = api.getCountries();

        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                try {
                    countries = (ArrayList) response.body();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                    CustomCountryList customCountryList = new CustomCountryList(activity, countries);
                    listView.setAdapter(customCountryList);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Toast.makeText(getApplicationContext(), " you selected " + countries.get(position).getCountryName() + " as country ",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.d("Failure", t.toString());

            }
        });
        return ;
    }
}
