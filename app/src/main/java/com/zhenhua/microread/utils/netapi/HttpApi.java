package com.zhenhua.microread.utils.netapi;


import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 存放所有的Api
 */

public interface HttpApi {

    @GET("user/getVerifyCode")
    Observable<ResponseBody> getVerifyCode(@Query("phoneNum") String phoneNum);

    @POST("user/login")
    @FormUrlEncoded
    Observable<ResponseBody> login(@FieldMap Map<String, Object> map);

    @GET("share/getShareList")
    Observable<ResponseBody> getShareList(@QueryMap Map<String, Object> map);


    @GET("getUsers")
    Observable<ResponseBody> getUsers();

    @GET("getUserSelectById")
    Observable<ResponseBody> getUserSelectById(@Query("id") String id);

    @GET("getUsersByPage")
    Observable<ResponseBody> getUsersByPage(@QueryMap Map<String, Object> map);

//    @POST("insert")
//    Observable<ResponseBody> insert(@Body User user);
//
//    @POST("insertUser")
//    Observable<ResponseBody> insertUser(@Body User user);

    @POST("insertUser1")
    @FormUrlEncoded
    Observable<ResponseBody> insertUser1(@Field("userId") String userId, @Field("userName") String userName);

    @POST("insertUser2")
    @FormUrlEncoded
    Observable<ResponseBody> insertUser2(@FieldMap Map<String, Object> map);

    @GET("getUsers")
    Observable<ResponseBody> getWeatherDataForQuery(@Query("version") String version, @Query("city") String city);

    @GET("api")
    Observable<ResponseBody> getWeatherDataForMap(@QueryMap Map<String, String> map);

    //天气预报接口测试  @GET 不支持@Body类型
//    @POST("api")
//    Observable<ResponseBody> getWeatherDataForBody(@Body BaseRequestBean<WeatherRequestBean> requestBean);

    /**
     * 文件下载
     */
    @GET()
    @Streaming//使用Streaming 方式 Retrofit 不会一次性将ResponseBody 读取进入内存，否则文件很多容易OOM
    Observable<ResponseBody> downloadFile(@Url String fileUrl);//返回值使用 ResponseBody 之后会对ResponseBody 进行读取

    @POST("share/upload")
    @Multipart
    Observable<ResponseBody> upload(@Part MultipartBody.Part part);

//    @POST("share/multiUpload")
//    @Multipart
//    Observable<ResponseBody> multiUpload(@Part("userId") RequestBody userId,
//                                         @Part("content") RequestBody content,
//                                         @Part("typeList") RequestBody typeList,
//                                         @Part MultipartBody.Part[] parts);

    @POST("share/multiUpload")
    @Multipart
    Observable<ResponseBody> multiUpload(@PartMap Map<String, RequestBody> map,
                                          @Part List<MultipartBody.Part> parts);

    @POST("share/test")
//    @Multipart
    Observable<ResponseBody> test(@Body List<Integer> typeList);

//    @POST("share/test")
//    @Multipart
//    Observable<ResponseBody> test(@Part("age") String age, @PartMap Map<String, RequestBody> map);
}
