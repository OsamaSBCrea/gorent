package com.example.gorent.repository.api;

import com.example.gorent.data.models.Tenant;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TenantRepository {

    @GET("tenant/{tenantId}")
    Observable<Tenant> getTenant(@Path("tenantId") long tenantId);

    @PUT("tenant/{tenantId}")
    Observable<Tenant> updateTenant(@Path("tenantId") long tenantId, @Body Tenant tenant);

}
