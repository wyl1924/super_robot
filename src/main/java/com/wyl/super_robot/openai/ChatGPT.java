package com.wyl.super_robot.openai;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.alibaba.fastjson.JSON;
import com.wyl.super_robot.enums.ResultEnums;
import com.wyl.super_robot.openai.api.Api;
import com.wyl.super_robot.openai.edits.Edit;
import com.wyl.super_robot.openai.edits.EditResponse;
import com.wyl.super_robot.openai.entity.BaseResponse;
import com.wyl.super_robot.openai.entity.billing.BillingUsage;
import com.wyl.super_robot.openai.entity.billing.CreditGrantsResponse;
import com.wyl.super_robot.openai.entity.billing.Subscription;
import com.wyl.super_robot.openai.entity.chat.ChatCompletion;
import com.wyl.super_robot.openai.entity.chat.ChatCompletionResponse;
import com.wyl.super_robot.openai.entity.chat.Message;
import com.wyl.super_robot.openai.entity.common.OpenAiResponse;
import com.wyl.super_robot.openai.entity.completions.CompletionResponse;
import com.wyl.super_robot.openai.entity.embeddings.Embedding;
import com.wyl.super_robot.openai.entity.embeddings.EmbeddingResponse;
import com.wyl.super_robot.openai.entity.files.DeleteResponse;
import com.wyl.super_robot.openai.entity.files.File;
import com.wyl.super_robot.openai.entity.files.UploadFileResponse;
import com.wyl.super_robot.openai.entity.fineTune.Event;
import com.wyl.super_robot.openai.entity.fineTune.FineTune;
import com.wyl.super_robot.openai.entity.fineTune.FineTuneDeleteResponse;
import com.wyl.super_robot.openai.entity.fineTune.FineTuneResponse;
import com.wyl.super_robot.openai.entity.images.*;
import com.wyl.super_robot.openai.entity.models.ListModels;
import com.wyl.super_robot.openai.entity.models.Model;
import com.wyl.super_robot.openai.entity.moderations.Moderation;
import com.wyl.super_robot.openai.entity.moderations.ModerationResponse;
import com.wyl.super_robot.openai.entity.transcriptions.Transcriptions;
import com.wyl.super_robot.openai.entity.transcriptions.Translations;
import com.wyl.super_robot.openai.entity.transcriptions.WhisperResponse;
import com.wyl.super_robot.openai.enums.MessageRole;
import com.wyl.super_robot.openai.exception.ChatException;
import io.reactivex.Single;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.processing.Completion;
import java.math.BigDecimal;
import java.net.Proxy;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * open ai 客户端
 *
 * @author wyl
 */

@Slf4j
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPT {
    /**
     * key
     */
    private String apiKey;
    /**
     * keys
     */
    private List<String> apiKeyList;
    /**
     * 自定义api host使用builder的方式构造client
     */
    @Builder.Default
    private String apiHost = Api.DEFAULT_API_HOST;
    private Api apiClient;
    private OkHttpClient okHttpClient;
    /**
     * 超时 默认300
     */
    @Builder.Default
    private long timeout = 300;
    /**
     * okhttp 代理
     */
    @Builder.Default
    private Proxy proxy = Proxy.NO_PROXY;


    /**
     * 初始化
     */
    public ChatGPT init() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(chain -> {
            Request original = chain.request();
            String key = apiKey;
            if (apiKeyList != null && !apiKeyList.isEmpty()) {
                key = RandomUtil.randomEle(apiKeyList);
            }

            Request request = original.newBuilder()
                    .header(Header.AUTHORIZATION.getValue(), "Bearer " + key)
                    .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        }).addInterceptor(chain -> {
            Request original = chain.request();
            Response response = chain.proceed(original);
            if (!response.isSuccessful()) {
                String errorMsg = response.body().string();

                log.error("请求异常：{}", errorMsg);
                BaseResponse baseResponse = JSON.parseObject(errorMsg, BaseResponse.class);
                if (Objects.nonNull(baseResponse.getError())) {
                    log.error(baseResponse.getError().getMessage());
                    throw new ChatException(baseResponse.getError().getMessage());
                }
                throw new ChatException("error");
            }
            return response;
        });

        client.connectTimeout(timeout, TimeUnit.SECONDS);
        client.writeTimeout(timeout, TimeUnit.SECONDS);
        client.readTimeout(timeout, TimeUnit.SECONDS);
        OkHttpClient httpClient = client.build();
        this.okHttpClient = httpClient;


        this.apiClient = new Retrofit.Builder()
                .baseUrl(this.apiHost)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(Api.class);

        return this;
    }

    /**
     * 模型列表
     */
    public Single<ListModels> listModels() {
        return this.apiClient.models();
    }

    /**
     * 获取指定模型
     *
     * @param id
     * @return
     */
    public Single<Model> getModel(String id) {
        return this.apiClient.model(id);
    }

    /**
     * chat completion 更加贴近官方网站的问答模型
     *
     * @param chatCompletion 问答参数
     * @return 答案
     */
    public ChatCompletionResponse chatCompletion(ChatCompletion chatCompletion) {
        Single<ChatCompletionResponse> chatCompletionResponse =
                this.apiClient.chatCompletion(chatCompletion);
        return chatCompletionResponse.blockingGet();
    }

    /**
     * 包含上下文的问答
     *
     * @param messages 问答参数
     */
    public ChatCompletionResponse chatCompletion(List<Message> messages) {
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(messages).build();
        return this.chatCompletion(chatCompletion);
    }

    /**
     * 问答接口
     *
     * @param completion
     * @return CompletionResponse
     */
    public CompletionResponse completions(Completion completion) {
        Single<CompletionResponse> completions = this.apiClient.completions(completion);
        return completions.blockingGet();
    }

    public String chat(List<String> message) {
        List<Message> messages = new ArrayList<>();
        message.forEach(m -> messages.add(Message.of(m)));
        var response = chatCompletion(messages);
        log.info("---------------------------------------------------");
        log.info("Open AI 官方计算的总的tokens数{}", response.getUsage().getTotalTokens());
        log.info("Open AI 官方计算的请求的tokens数{}", response.getUsage().getPromptTokens());
        log.info("Open AI 官方计算的返回的tokens数{}", response.getUsage().getCompletionTokens());
        return response.getChoices().get(0).getMessage().getContent();
    }

    /**
     * 简单问答（不包含上下文）
     */
    public String chat(String message) {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(Arrays.asList(Message.of(message)))
                .build();
        ChatCompletionResponse response = this.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage().getContent();
    }

    /**
     * 文本修改
     *
     * @param edit
     * @return
     */
    public EditResponse edit(Edit edit) {
        Single<EditResponse> editResponse = this.apiClient.edits(edit);
        return editResponse.blockingGet();
    }

    /**
     * 根据描述生成图片
     *
     * @param prompt
     * @return ImageResponse
     */
    public ImageResponse genImages(String prompt) {
        Image image = Image.builder().prompt(prompt).build();
        return this.genImages(image);
    }

    /**
     * 根据描述生成图片
     *
     * @param image
     * @return ImageResponse
     */
    public ImageResponse genImages(Image image) {
        Single<ImageResponse> edits = this.apiClient.genImages(image);
        return edits.blockingGet();
    }

    public List<Item> editImages(java.io.File image, String prompt) {
        ImageEdit imageEdit = ImageEdit.builder().prompt(prompt).build();
        return this.editImages(image, null, imageEdit);
    }

    public ImageResponse variationsImages(java.io.File image, ImageVariations imageVariations) {
        checkImage(image);
        checkImageFormat(image);
        checkImageSize(image);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("image", image.getName(), imageBody);
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("n", RequestBody.create(MediaType.parse("multipart/form-data"), imageVariations.getN().toString()));
        requestBodyMap.put("size", RequestBody.create(MediaType.parse("multipart/form-data"), imageVariations.getSize()));
        requestBodyMap.put("response_format", RequestBody.create(MediaType.parse("multipart/form-data"), imageVariations.getResponseFormat()));
        if (!(Objects.isNull(imageVariations.getUser()) || "".equals(imageVariations.getUser()))) {
            requestBodyMap.put("user", RequestBody.create(MediaType.parse("multipart/form-data"), imageVariations.getUser()));
        }
        Single<ImageResponse> variationsImages = this.apiClient.variationsImages(
                multipartBody,
                requestBodyMap
        );
        return variationsImages.blockingGet();
    }

    public ImageResponse variationsImages(java.io.File image) {
        checkImage(image);
        checkImageFormat(image);
        checkImageSize(image);
        ImageVariations imageVariations = ImageVariations.builder().build();
        return this.variationsImages(image, imageVariations);
    }


    /**
     * 向量计算：单文本
     *
     * @param input
     * @return EmbeddingResponse
     */
    public EmbeddingResponse embeddings(String input) {
        List<String> inputs = new ArrayList<>(1);
        inputs.add(input);
        Embedding embedding = Embedding.builder().input(inputs).build();
        return this.embeddings(embedding);
    }

    /**
     * 向量计算：集合文本
     *
     * @param input
     * @return EmbeddingResponse
     */
    public EmbeddingResponse embeddings(List<String> input) {
        Embedding embedding = Embedding.builder().input(input).build();
        return this.embeddings(embedding);
    }

    /**
     * Creates an embedding vector representing the input text.
     *
     * @param embedding
     * @return EmbeddingResponse
     */
    public EmbeddingResponse embeddings(Embedding embedding) {
        Single<EmbeddingResponse> embeddings = this.apiClient.embeddings(embedding);
        return embeddings.blockingGet();
    }

    /**
     * 获取文件列表
     *
     * @return File  list
     */
    public List<File> files() {
        Single<OpenAiResponse<File>> files = this.apiClient.files();
        return files.blockingGet().getData();
    }

    /**
     * 删除文件
     *
     * @param fileId
     * @return DeleteResponse
     */
    public DeleteResponse deleteFile(String fileId) {
        Single<DeleteResponse> deleteFile = this.apiClient.deleteFile(fileId);
        return deleteFile.blockingGet();
    }

    /**
     * 上传文件
     *
     * @param purpose
     * @param file
     * @return UploadFileResponse
     */
    public UploadFileResponse uploadFile(String purpose, java.io.File file) {
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);

        RequestBody purposeBody = RequestBody.create(MediaType.parse("multipart/form-data"), purpose);
        Single<UploadFileResponse> uploadFileResponse = this.apiClient.uploadFile(multipartBody, purposeBody);
        return uploadFileResponse.blockingGet();
    }

    /**
     * 文本审核
     *
     * @param input 待检测数据集合
     * @return ModerationResponse
     */
    public ModerationResponse moderations(List<String> input) {
        Moderation moderation = Moderation.builder().input(input).build();
        Single<ModerationResponse> responseSingle = this.apiClient.moderations(moderation);
        return responseSingle.blockingGet();
    }

    /**
     * 上传文件
     *
     * @param file
     * @return UploadFileResponse
     */
    public UploadFileResponse uploadFile(java.io.File file) {
        //purpose 官网示例默认是：fine-tune
        return this.uploadFile("fine-tune", file);
    }

    /**
     * 检索文件
     *
     * @param fileId
     * @return File
     */
    public File retrieveFile(String fileId) {
        Single<File> fileContent = this.apiClient.retrieveFile(fileId);
        return fileContent.blockingGet();
    }

    /**
     * 语音转文字
     *
     * @param transcriptions 参数
     * @param file           语音文件 最大支持25MB mp3, mp4, mpeg, mpga, m4a, wav, webm
     * @return 语音文本
     */
    public WhisperResponse speechToTextTranscriptions(java.io.File file, Transcriptions transcriptions) {
        //文件
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        //自定义参数
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        if (StrUtil.isNotBlank(transcriptions.getLanguage())) {
            requestBodyMap.put(Transcriptions.Fields.language, RequestBody.create(MediaType.parse("multipart/form-data"), transcriptions.getLanguage()));
        }
        if (StrUtil.isNotBlank(transcriptions.getModel())) {
            requestBodyMap.put(Transcriptions.Fields.model, RequestBody.create(MediaType.parse("multipart/form-data"), transcriptions.getModel()));
        }
        if (StrUtil.isNotBlank(transcriptions.getPrompt())) {
            requestBodyMap.put(Transcriptions.Fields.prompt, RequestBody.create(MediaType.parse("multipart/form-data"), transcriptions.getPrompt()));
        }
        if (StrUtil.isNotBlank(transcriptions.getResponseFormat())) {
            requestBodyMap.put(Transcriptions.Fields.responseFormat, RequestBody.create(MediaType.parse("multipart/form-data"), transcriptions.getResponseFormat()));
        }
        if (Objects.nonNull(transcriptions.getTemperature())) {
            requestBodyMap.put(Transcriptions.Fields.temperature, RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(transcriptions.getTemperature())));
        }
        Single<WhisperResponse> whisperResponse = this.apiClient.speechToTextTranscriptions(multipartBody, requestBodyMap);
        return whisperResponse.blockingGet();
    }

    /**
     * 简易版 语音转文字
     *
     * @param file 语音文件 最大支持25MB mp3, mp4, mpeg, mpga, m4a, wav, webm
     * @return 语音文本
     */
    public WhisperResponse speechToTextTranscriptions(java.io.File file) {
        Transcriptions transcriptions = Transcriptions.builder().build();
        return this.speechToTextTranscriptions(file, transcriptions);

    }


    /**
     * 语音翻译：目前仅支持翻译为英文
     *
     * @param translations 参数
     * @param file         语音文件 最大支持25MB mp3, mp4, mpeg, mpga, m4a, wav, webm
     * @return 翻译后文本
     */
    public WhisperResponse speechToTextTranslations(java.io.File file, Translations translations) {
        //文件
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        //自定义参数
        Map<String, RequestBody> requestBodyMap = new HashMap<>();

        if (StrUtil.isNotBlank(translations.getModel())) {
            requestBodyMap.put(Translations.Fields.model, RequestBody.create(MediaType.parse("multipart/form-data"), translations.getModel()));
        }
        if (StrUtil.isNotBlank(translations.getPrompt())) {
            requestBodyMap.put(Translations.Fields.prompt, RequestBody.create(MediaType.parse("multipart/form-data"), translations.getPrompt()));
        }
        if (StrUtil.isNotBlank(translations.getResponseFormat())) {
            requestBodyMap.put(Translations.Fields.responseFormat, RequestBody.create(MediaType.parse("multipart/form-data"), translations.getResponseFormat()));
        }
        if (Objects.nonNull(translations.getTemperature())) {
            requestBodyMap.put(Translations.Fields.temperature, RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(translations.getTemperature())));
        }
        Single<WhisperResponse> whisperResponse = this.apiClient.speechToTextTranslations(multipartBody, requestBodyMap);
        return whisperResponse.blockingGet();
    }

    /**
     * 简易版 语音翻译：目前仅支持翻译为英文
     *
     * @param file 语音文件 最大支持25MB mp3, mp4, mpeg, mpga, m4a, wav, webm
     * @return 翻译后文本
     */
    public WhisperResponse speechToTextTranslations(java.io.File file) {
        Translations translations = Translations.builder().build();
        return this.speechToTextTranslations(file, translations);
    }

    /**
     * 校验语音文件大小给出提示，目前官方限制25MB，后续可能会改动所以不报错只做提示
     *
     * @param file
     */
    private void checkSpeechFileSize(java.io.File file) {
        if (file.length() > 25 * 1204 * 1024) {
            log.warn("2023-03-02官方文档提示：文件不能超出25MB");
        }
    }

    /**
     * 创建微调模型
     *
     * @param fineTune
     * @return FineTuneResponse
     */
    public FineTuneResponse fineTune(FineTune fineTune) {
        Single<FineTuneResponse> fineTuneResponse = this.apiClient.fineTune(fineTune);
        return fineTuneResponse.blockingGet();
    }

    /**
     * 创建微调模型
     *
     * @param trainingFileId 文件id，文件上传返回的id
     * @return FineTuneResponse
     */
    public FineTuneResponse fineTune(String trainingFileId) {
        FineTune fineTune = FineTune.builder().trainingFile(trainingFileId).build();
        return this.fineTune(fineTune);
    }

    /**
     * 微调模型列表
     *
     * @return FineTuneResponse list
     */
    public List<FineTuneResponse> fineTunes() {
        Single<OpenAiResponse<FineTuneResponse>> fineTunes = this.apiClient.fineTunes();
        return fineTunes.blockingGet().getData();
    }

    /**
     * 检索微调作业
     *
     * @param fineTuneId
     * @return FineTuneResponse
     */
    public FineTuneResponse retrieveFineTune(String fineTuneId) {
        Single<FineTuneResponse> fineTune = this.apiClient.retrieveFineTune(fineTuneId);
        return fineTune.blockingGet();
    }

    /**
     * 取消微调作业
     *
     * @param fineTuneId
     * @return FineTuneResponse
     */
    public FineTuneResponse cancelFineTune(String fineTuneId) {
        Single<FineTuneResponse> fineTune = this.apiClient.cancelFineTune(fineTuneId);
        return fineTune.blockingGet();
    }

    /**
     * 微调作业事件列表
     *
     * @param fineTuneId
     * @return Event List
     */
    public List<Event> fineTuneEvents(String fineTuneId) {
        Single<OpenAiResponse<Event>> events = this.apiClient.fineTuneEvents(fineTuneId);
        return events.blockingGet().getData();
    }

    /**
     * 删除微调作业模型
     * Delete a fine-tuned model. You must have the Owner role in your organization.
     *
     * @param model
     * @return FineTuneDeleteResponse
     */
    public FineTuneDeleteResponse deleteFineTuneModel(String model) {
        Single<FineTuneDeleteResponse> delete = this.apiClient.deleteFineTuneModel(model);
        return delete.blockingGet();
    }

    /**
     * 余额查询
     */
    public CreditGrantsResponse creditGrants() {
        Single<CreditGrantsResponse> creditGrants = this.apiClient.creditGrants();
        return creditGrants.blockingGet();
    }


    /**
     * 余额查询
     */
    public BigDecimal balance() {
        Single<CreditGrantsResponse> creditGrants = apiClient.creditGrants();
        CreditGrantsResponse response = creditGrants.blockingGet();

        return response.getTotalAvailable();
    }

    /**
     * 账户信息查询：里面包含总金额等信息
     *
     * @return
     */
    public Subscription subscription() {
        Single<Subscription> subscription = this.apiClient.subscription();
        return subscription.blockingGet();
    }

    /**
     * 余额查询
     */
    public BigDecimal balance(String key) {
        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey(key)
                .build()
                .init();

        return chatGPT.balance();
    }

    /**
     * 账户调用接口消耗金额信息查询
     * 最多查询100天
     *
     * @param starDate 开始时间
     * @param endDate  结束时间
     * @return
     */
    public BillingUsage billingUsage(@NotNull LocalDate starDate, @NotNull LocalDate endDate) {
        Single<BillingUsage> billingUsage = this.apiClient.billingUsage(starDate, endDate);
        return billingUsage.blockingGet();
    }


    private List<Item> editImages(java.io.File image, java.io.File mask, ImageEdit imageEdit) {
        checkImage(image);
        checkImageFormat(image);
        checkImageSize(image);
        if (Objects.nonNull(mask)) {
            checkImageFormat(image);
            checkImageSize(image);
        }
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part imageMultipartBody = MultipartBody.Part.createFormData("image", image.getName(), imageBody);
        MultipartBody.Part maskMultipartBody = null;
        if (Objects.nonNull(mask)) {
            RequestBody maskBody = RequestBody.create(MediaType.parse("multipart/form-data"), mask);
            maskMultipartBody = MultipartBody.Part.createFormData("mask", image.getName(), maskBody);
        }
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("prompt", RequestBody.create(MediaType.parse("multipart/form-data"), imageEdit.getPrompt()));
        requestBodyMap.put("n", RequestBody.create(MediaType.parse("multipart/form-data"), imageEdit.getN().toString()));
        requestBodyMap.put("size", RequestBody.create(MediaType.parse("multipart/form-data"), imageEdit.getSize()));
        requestBodyMap.put("response_format", RequestBody.create(MediaType.parse("multipart/form-data"), imageEdit.getResponseFormat()));
        if (!(Objects.isNull(imageEdit.getUser()) || "".equals(imageEdit.getUser()))) {
            requestBodyMap.put("user", RequestBody.create(MediaType.parse("multipart/form-data"), imageEdit.getUser()));
        }
        Single<ImageResponse> imageResponse = this.apiClient.editImages(
                imageMultipartBody,
                maskMultipartBody,
                requestBodyMap
        );
        return imageResponse.blockingGet().getData();
    }

    /**
     * 校验图片不能为空
     *
     * @param image
     */
    private void checkImage(java.io.File image) {
        if (Objects.isNull(image)) {
            log.error("image不能为空");
            throw new ChatException(ResultEnums.PARAM_Format.getMsg());
        }
    }

    /**
     * 校验图片格式
     *
     * @param image
     */
    private void checkImageFormat(java.io.File image) {
        if (!(image.getName().endsWith("png") || image.getName().endsWith("PNG"))) {
            log.error("image格式错误");
            throw new ChatException(ResultEnums.PARAM_Format.getMsg());
        }
    }

    /**
     * 校验图片大小
     *
     * @param image
     */
    private void checkImageSize(java.io.File image) {
        if (image.length() > 4 * 1024 * 1024) {
            log.error("image最大支持4MB");
            throw new ChatException(ResultEnums.PARAM_Format.getMsg());
        }
    }

}
