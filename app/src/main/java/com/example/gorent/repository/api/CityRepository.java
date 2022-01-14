package com.example.gorent.repository.api;

import com.example.gorent.data.models.locale.City;
import com.example.gorent.util.shared.pagination.Page;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CityRepository {

    @GET("city")
    Observable<Page<City>> getCities(@Query("page") int page, @Query("pageSize") int pageSize,
                                     @Query("search") String search, @Query("countryId") long countryId);

    @GET("city/{cityId}")
    Observable<City> getCityById(@Path("cityId") long cityId);

}
