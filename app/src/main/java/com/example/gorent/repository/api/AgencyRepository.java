package com.example.gorent.repository.api;

import com.example.gorent.data.models.Agency;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AgencyRepository {

    @GET("agency/{agencyId}")
    Observable<Agency> getAgencyById(@Path("agencyId") long agencyId);

    @PUT("agency/{agencyId}")
    Observable<Agency> updateAgency(@Path("agencyId") long agencyId, @Body Agency agency);

}
