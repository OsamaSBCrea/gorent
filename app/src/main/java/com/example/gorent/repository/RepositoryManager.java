package com.example.gorent.repository;

import android.content.Context;

import com.example.gorent.data.sqlite.SQLiteDatabaseManager;
import com.example.gorent.repository.api.AgencyRepository;
import com.example.gorent.repository.api.AuthenticationRepository;
import com.example.gorent.repository.api.CityRepository;
import com.example.gorent.repository.api.CountryRepository;
import com.example.gorent.repository.api.PropertyRepository;
import com.example.gorent.repository.api.RentRepository;
import com.example.gorent.repository.api.TenantRepository;
import com.example.gorent.repository.sqlite.SQLiteCountryRepository;
import com.example.gorent.util.network.AuthInterceptor;
import com.example.gorent.util.shared.SharedPreferencesManager;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.Instant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryManager {

    private final String API_BASE_URL = "http://192.168.0.3:8080/api/";

    private static RepositoryManager repositoryManager;

    private final SQLiteDatabaseManager sqLiteDatabaseManager;

    private Retrofit retrofit;

    private SQLiteCountryRepository sqLiteCountryRepository;

    private CountryRepository countryRepository;

    private CityRepository cityRepository;

    private PropertyRepository propertyRepository;

    private AuthenticationRepository authenticationRepository;

    private RentRepository rentRepository;

    private TenantRepository tenantRepository;

    private AgencyRepository agencyRepository;

    private SharedPreferencesManager sharedPreferencesManager;

    private RepositoryManager(Context context) {
        sharedPreferencesManager = SharedPreferencesManager.getInstance(context);
        this.sqLiteDatabaseManager = SQLiteDatabaseManager.getInstance(context);
        this.initRepositories();
    }

    public static RepositoryManager getInstance(Context context) {
        if (repositoryManager == null) {
            repositoryManager = new RepositoryManager(context);
        }

        return repositoryManager;
    }

    private void initRepositories() {
        AuthInterceptor authInterceptor = new AuthInterceptor(sharedPreferencesManager);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(authInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();



        retrofit = new Retrofit.Builder().baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder().registerTypeAdapter(
                                Instant.class,
                                (JsonDeserializer<Instant>) (json, type, jsonDeserializationContext) ->
                                        Instant.parse(json.getAsString())).create()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.countryRepository = retrofit.create(CountryRepository.class);
        this.cityRepository = retrofit.create(CityRepository.class);
        this.propertyRepository = retrofit.create(PropertyRepository.class);
        this.authenticationRepository = retrofit.create(AuthenticationRepository.class);
        this.rentRepository = retrofit.create(RentRepository.class);
        this.tenantRepository = retrofit.create(TenantRepository.class);
        this.agencyRepository = retrofit.create(AgencyRepository.class);

        this.sqLiteCountryRepository = SQLiteCountryRepository.getInstance(sqLiteDatabaseManager);
    }

    public CountryRepository getCountryRepository() {
        return countryRepository;
    }

    public CityRepository getCityRepository() {
        return cityRepository;
    }

    public PropertyRepository getPropertyRepository() {
        return propertyRepository;
    }

    public AuthenticationRepository getAuthenticationRepository() {
        return this.authenticationRepository;
    }

    public RentRepository getRentRepository() {
        return rentRepository;
    }

    public TenantRepository getTenantRepository() {
        return tenantRepository;
    }

    public AgencyRepository getAgencyRepository() {
        return agencyRepository;
    }

    public SQLiteCountryRepository getSqLiteCountryRepository() {
        return sqLiteCountryRepository;
    }
}
