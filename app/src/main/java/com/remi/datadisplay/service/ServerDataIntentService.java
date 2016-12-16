package com.remi.datadisplay.service;

import android.app.IntentService;
import android.content.Intent;

import com.remi.datadisplay.DummyStorage;
import com.remi.datadisplay.event.DataUpdated;
import com.remi.datadisplay.model.Review;
import com.remi.datadisplay.model.ReviewsDTO;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class ServerDataIntentService extends IntentService {

    public ServerDataIntentService() {
        super("ServerDataIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        //do synchronous HTTP request
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://cache.usabilla.com/example/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<ReviewsDTO> call = service.getReviews();
        Response<ReviewsDTO> execute = null;
        try {
            execute = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ReviewsDTO reviewsDTO = execute.body();

        System.out.println("reviewsDTO size " + reviewsDTO.getReviews().size());

        //convert DTO data to model
        ArrayList<Review> reviews = DTOConverterService.fromDTO(reviewsDTO.getReviews());

        //hand data back to Activity to display
        //TODO what to do whi data ?
        System.out.println("reviews" + reviews);
        DummyStorage.reviews = reviews;
        EventBus.getDefault().post(new DataUpdated());

    }
}
