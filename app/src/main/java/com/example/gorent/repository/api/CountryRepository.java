package com.example.gorent.repository.api;

import com.example.gorent.data.models.locale.Country;
import com.example.gorent.util.shared.pagination.Page;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CountryRepository {
    @GET("country")
    Observable<Page<Country>> getCountries(@Query("page") int page, @Query("pageSize") int pageSize, @Query("search") String search);

    @GET("country/{countryId}")
    Observable<Country> getCountryById(@Path("countryId") long countryId);

}
