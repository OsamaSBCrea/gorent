package com.example.gorent.repository.api;

import com.example.gorent.data.models.RentApplication;
import com.example.gorent.data.models.RentHistory;
import com.example.gorent.util.shared.pagination.Page;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RentRepository {

    @GET("rent/applications")
    Observable<Page<RentApplication>> getRentApplications(@Query("page") int page,
                                                          @Query("pageSize") int pageSize);

    @GET("rent/history")
    Observable<List<RentHistory>> getRentHistory();

    @GET("rent/history/{tenantId}")
    Observable<List<RentHistory>> getTenantHistory(@Path("tenantId") long tenantId);

    @POST("rent/approve/{rentId}")
    Observable<Response<Void>> approveRent(@Path("rentId") long rentId);

    @POST("rent/reject/{rentId}")
    Observable<Response<Void>> rejectRent(@Path("rentId") long rentId);

}
