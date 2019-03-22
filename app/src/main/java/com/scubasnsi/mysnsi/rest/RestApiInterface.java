package com.scubasnsi.mysnsi.rest;


import com.scubasnsi.mysnsi.model.data_models.Logbook;
import com.scubasnsi.mysnsi.model.dto.C_CardsListDto;
import com.scubasnsi.mysnsi.model.dto.C_CardsPDFDto;
import com.scubasnsi.mysnsi.model.dto.CoursesDto;
import com.scubasnsi.mysnsi.model.dto.DiverLoginDto;
import com.scubasnsi.mysnsi.model.dto.LogCountDto;
import com.scubasnsi.mysnsi.model.dto.LogbookDeleteDto;
import com.scubasnsi.mysnsi.model.dto.LogbookDto;
import com.scubasnsi.mysnsi.model.dto.LoginDto;
import com.scubasnsi.mysnsi.model.dto.ProfileDto;
import com.scubasnsi.mysnsi.model.dto.SignupDto;
import com.scubasnsi.mysnsi.rest.dto.ResponseDto;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;


/**
 * Author -
 * Date -  13-10-2016.
 */

public interface RestApiInterface {
    String BASE_URL = "http://mysnsi.scubasnsi.com/";
    String AD_URL = "http://scubasnsi.goscubasnsi.com";
    String IMAGE_URL = BASE_URL+"wp-content/uploads/ccards/";


    @POST("App_API.php")
    Call<ResponseDto> login(@Body LoginDto loginDto);

    @POST("App_API.php")
    Call<ResponseDto> getProfile(@Body ProfileDto loginDto);

    @POST("App_API.php")
    Call<ResponseDto> signup(@Body SignupDto signupDto);

    @POST("App_API.php")
    Call<ResponseDto> loginDiver(@Body DiverLoginDto diverLoginDto);

    @POST("App_API.php")
    Call<ResponseDto> getcources(@Body CoursesDto coursesDto);

    @POST("App_API.php")
    Call<ResponseDto> getCCardList(@Body C_CardsListDto c_cardsListDto);

    @POST("App_API.php")
    Call<ResponseDto> getLogCounts(@Body LogCountDto logCountDto);

    @Multipart
    @POST("App_API.php")
    Call<ResponseDto> addDive(@Part("logs")Logbook logbook, @Part MultipartBody.Part file);

    @Multipart
    @POST("App_API.php")
    Call<ResponseDto> uploadPassport(@Part("logs") Logbook logbook, @Part MultipartBody.Part file);

    @POST("App_API.php")
    Call<ResponseDto> getLogbooks(@Body LogbookDto logbookDto);

    @POST("App_API.php")
    Call<ResponseDto> deleteLog(@Body LogbookDeleteDto logbookDeleteDto);

    @POST("App_API.php")
    Call<ResponseDto> getPDFUrl(@Body C_CardsPDFDto c_cardsPDFDto);

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);


}
