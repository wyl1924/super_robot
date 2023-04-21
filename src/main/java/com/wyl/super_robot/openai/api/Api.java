package com.wyl.super_robot.openai.api;

import com.wyl.super_robot.openai.entity.AuthenticationBean;
import com.wyl.super_robot.openai.entity.billing.CreditGrantsResponse;
import com.wyl.super_robot.openai.entity.chat.ChatCompletion;
import com.wyl.super_robot.openai.entity.chat.ChatCompletionResponse;
import com.wyl.super_robot.openai.entity.images.Image;
import com.wyl.super_robot.openai.entity.images.ImageResponse;
import com.wyl.super_robot.openai.entity.models.ListModels;
import com.wyl.super_robot.utils.ResponseData;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;


/**
 *
 */
public interface Api {

    String DEFAULT_API_HOST = "https://api.openai.com/";


    @GET("v1/models")
    Single<ListModels> models();

    @GET("v1/models/{id}")
    Single<Model> model(@Path("id") String id);




    /**
     * chat
     */
    @POST("v1/chat/completions")
    Single<ChatCompletionResponse> chatCompletion(@Body ChatCompletion chatCompletion);


    /**
     * 余额查询
     */
    @GET("dashboard/billing/credit_grants")
    Single<CreditGrantsResponse> creditGrants();


}
