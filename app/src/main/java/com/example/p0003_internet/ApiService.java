package com.example.p0003_internet;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface ApiService {

    @GET("random_joke")
    Single<Joke> loadJoke();
}
