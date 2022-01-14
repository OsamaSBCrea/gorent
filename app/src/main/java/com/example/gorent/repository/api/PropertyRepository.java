package com.example.gorent.repository.api;

import com.example.gorent.data.dto.PropertyDTO;
import com.example.gorent.data.models.Property;
import com.example.gorent.data.models.PropertySearch;
import com.example.gorent.data.models.Rent;
import com.example.gorent.util.shared.pagination.Page;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PropertyRepository {

    @GET("property")
    Observable<Page<Property>> getProperties(@Query("page") int page, @Query("pageSize") int pageSize);

    @GET("property/latest")
    Observable<List<Property>> getLatestProperties();

    @GET("property/{propertyId}")
    Observable<Property> getPropertyById(@Path("propertyId") long propertyId);

    @POST("property")
    Observable<Property> newProperty(@Body PropertyDTO property);

    @PUT("property/{propertyId}")
    Observable<Response<Void>> updateProperty(@Path("propertyId") long propertyId, @Body PropertyDTO propertyDTO);

    @POST("property/search")
    Observable<Page<Property>> searchProperties(@Query("page") int page,
                                                @Query("pageSize") int pageSize,
                                                @Body PropertySearch propertySearch);

    @DELETE("property/{propertyId}")
    Observable<Response<Void>> deleteProperty(@Path("propertyId") long propertyId);

    @POST("property/{propertyId}/rent")
    Observable<Rent> rentProperty(@Path("propertyId") long propertyId);
}
