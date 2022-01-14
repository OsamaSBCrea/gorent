package com.example.gorent.repository.api;

import com.example.gorent.data.models.auth.AgencyRegisterModel;
import com.example.gorent.data.models.auth.JWTToken;
import com.example.gorent.data.models.auth.LoginModel;
import com.example.gorent.data.models.auth.TenantRegisterModel;
import com.example.gorent.data.models.auth.UserModel;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthenticationRepository {

    @POST("agency/register")
    Observable<Response<Void>> registerAgency(@Body AgencyRegisterModel agencyRegisterModel);

    @POST("tenant/register")
    Observable<Response<Void>> registerTenant(@Body TenantRegisterModel tenantRegisterModel);

    @POST("authenticate")
    Observable<JWTToken> authenticate(@Body LoginModel loginModel);

    @GET("authenticate")
    Observable<Void> isAuthenticated();

    @GET("account")
    Observable<UserModel> getCurrentUser();

    @POST("fcm-token")
    Observable<Response<Void>> refreshFCMToken(@Query("token") String token);

}
