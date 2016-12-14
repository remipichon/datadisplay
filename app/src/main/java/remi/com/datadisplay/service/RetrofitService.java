package remi.com.datadisplay.service;

import remi.com.datadisplay.model.ReviewsDTO;
import retrofit.Call;
import retrofit.http.GET;


public interface RetrofitService {

    @GET("apidemo.json")
    Call<ReviewsDTO> getReviews();

}
