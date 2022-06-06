package com.emmu.webserviceretrofit;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CountryArrayAPI {
    //@get url is after base url

    @GET("/arpitmandliya/AndroidRestJSONExample/master/countries.json")
    public Call<List<Country>>getCountries();
}
