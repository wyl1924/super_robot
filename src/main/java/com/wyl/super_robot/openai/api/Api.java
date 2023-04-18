package com.wyl.super_robot.openai.api;

import com.wyl.super_robot.openai.entity.AuthenticationBean;
import com.wyl.super_robot.openai.entity.billing.CreditGrantsResponse;
import com.wyl.super_robot.openai.entity.chat.ChatCompletion;
import com.wyl.super_robot.openai.entity.chat.ChatCompletionResponse;
import com.wyl.super_robot.openai.entity.images.Image;
import com.wyl.super_robot.openai.entity.images.ImageResponse;
import com.wyl.super_robot.utils.ResponseData;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.Map;


/**
 *
 */
public interface Api {

    String DEFAULT_API_HOST = "https://api.openai.com/";


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

/**
 * 根据描述生成图片
 */
    @POST("v1/images/generations")
    Single<ImageResponse> genImages(@Body Image image);
    @POST("bp/log/login")
   ResponseData<Map<String, Object>> login(@Body AuthenticationBean authenticationBean);
}
