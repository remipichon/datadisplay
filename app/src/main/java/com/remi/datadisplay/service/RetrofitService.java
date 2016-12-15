package com.remi.datadisplay.service;

import com.remi.datadisplay.model.ReviewsDTO;

import retrofit.Call;
import retrofit.http.GET;


public interface RetrofitService {

    @GET("apidemo.json")
    Call<ReviewsDTO> getReviews();

}
