package com.wyl.super_robot.openai.api;

import com.wyl.super_robot.openai.edits.Edit;
import com.wyl.super_robot.openai.edits.EditResponse;
import com.wyl.super_robot.openai.entity.billing.BillingUsage;
import com.wyl.super_robot.openai.entity.billing.CreditGrantsResponse;
import com.wyl.super_robot.openai.entity.billing.Subscription;
import com.wyl.super_robot.openai.entity.chat.ChatCompletion;
import com.wyl.super_robot.openai.entity.chat.ChatCompletionResponse;
import com.wyl.super_robot.openai.entity.common.OpenAiResponse;
import com.wyl.super_robot.openai.entity.completions.CompletionResponse;
import com.wyl.super_robot.openai.entity.embeddings.Embedding;
import com.wyl.super_robot.openai.entity.embeddings.EmbeddingResponse;
import com.wyl.super_robot.openai.entity.files.DeleteResponse;
import com.wyl.super_robot.openai.entity.files.File;
import com.wyl.super_robot.openai.entity.files.UploadFileResponse;
import com.wyl.super_robot.openai.entity.images.Image;
import com.wyl.super_robot.openai.entity.images.ImageResponse;
import com.wyl.super_robot.openai.entity.models.ListModels;
import com.wyl.super_robot.openai.entity.models.Model;
import com.wyl.super_robot.openai.entity.transcriptions.WhisperResponse;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.*;

import javax.annotation.processing.Completion;
import java.time.LocalDate;
import java.util.Map;


/**
 *
 */
public interface Api {

    String DEFAULT_API_HOST = "https://api.openai.com/";

    /**
     * 模型列表
     */
    @GET("v1/models")
    Single<ListModels> models();

    /**
     * 获取指定模型
     */
    @GET("v1/models/{id}")
    Single<Model> model(@Path("id") String id);

    @POST("v1/completions")
    Single<CompletionResponse> completions(@Body Completion completion);

    /**
     * chat
     */
    @POST("v1/chat/completions")
    Single<ChatCompletionResponse> chatCompletion(@Body ChatCompletion chatCompletion);

    @POST("v1/edits")
    Single<EditResponse> edits(@Body Edit edit);
    /**
     * Creates an image given a prompt.
     * 根据描述生成图片
     */
    @POST("v1/images/generations")
    Single<ImageResponse> genImages(@Body Image image);

    /**
     * Creates an edited or extended image given an original image and a prompt.
     * 根据描述修改图片
     *
     * @param image
     * @param mask
     * @param requestBodyMap
     * @return Single ImageResponse
     */
    @Multipart
    @POST("v1/images/edits")
    Single<ImageResponse> editImages(@Part() MultipartBody.Part image,
                                     @Part() MultipartBody.Part mask,
                                     @PartMap() Map<String, RequestBody> requestBodyMap
    );

    /**
     * Creates a variation of a given image.
     *
     * @param image
     * @param requestBodyMap
     * @return Single ImageResponse
     */
    @Multipart
    @POST("v1/images/variations")
    Single<ImageResponse> variationsImages(@Part() MultipartBody.Part image,
                                           @PartMap() Map<String, RequestBody> requestBodyMap
    );

    /**
     * 文本向量计算
     *
     * @param embedding
     * @return Single EmbeddingResponse
     */
    @POST("v1/embeddings")
    Single<EmbeddingResponse> embeddings(@Body Embedding embedding);


    /**
     * Returns a list of files that belong to the user's organization.
     *
     * @return Single OpenAiResponse File
     */
    @GET("/v1/files")
    Single<OpenAiResponse<File>> files();

    /**
     * 删除文件
     *
     * @param fileId
     * @return Single DeleteResponse
     */
    @DELETE("v1/files/{file_id}")
    Single<DeleteResponse> deleteFile(@Path("file_id") String fileId);

    /**
     * 上传文件
     *
     * @param purpose
     * @param file
     * @return  Single UploadFileResponse
     */
    @Multipart
    @POST("v1/files")
    Single<UploadFileResponse> uploadFile(@Part MultipartBody.Part file,
                                          @Part("purpose") RequestBody purpose);


    /**
     * 检索文件
     */
    @GET("v1/files/{file_id}")
    Single<File> retrieveFile(@Path("file_id") String fileId);

    /**
     * 检索文件内容
     * ###不对免费用户开放###
     * ###不对免费用户开放###
     * ###不对免费用户开放###
     *
     * @param fileId
     * @return Single ResponseBody
     */
    @Streaming
    @GET("v1/files/{file_id}/content")
    Single<ResponseBody> retrieveFileContent(@Path("file_id") String fileId);

    /**
     * 语音转文字
     *
     * @param file              语音文件
     * @param requestBodyMap    参数
     * @return 文本
     */
    @Multipart
    @POST("v1/audio/transcriptions")
    Single<WhisperResponse> speechToTextTranscriptions(@Part MultipartBody.Part file,
                                                       @PartMap() Map<String, RequestBody> requestBodyMap);

    /**
     * 语音翻译：目前仅支持翻译为英文
     *
     * @param file              语音文件
     * @param requestBodyMap    参数
     * @return 文本
     */
    @Multipart
    @POST("v1/audio/translations")
    Single<WhisperResponse> speechToTextTranslations(@Part MultipartBody.Part file,
                                                     @PartMap() Map<String, RequestBody> requestBodyMap);

    /**
     * 余额查询
     * 官方禁止访问此接口
     *
     * @return 余额结果
     */
    @GET("dashboard/billing/credit_grants")
    @Deprecated
    Single<CreditGrantsResponse> creditGrants();

    /**
     * 账户信息查询：里面包含总金额（美元）等信息
     *
     * @return
     */
    @GET("v1/dashboard/billing/subscription")
    Single<Subscription> subscription();

    /**
     * 账户调用接口消耗金额信息查询
     * totalUsage = 账户总使用金额（美分）
     *
     * @return
     * @param starDate
     * @param endDate
     */
    @GET("v1/dashboard/billing/usage")
    Single<BillingUsage> billingUsage(@Query("start_date") LocalDate starDate, @Query("end_date") LocalDate endDate);


}
