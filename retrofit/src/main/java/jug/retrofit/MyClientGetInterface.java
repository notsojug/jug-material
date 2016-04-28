package jug.retrofit;

import retrofit.http.GET;
import retrofit.http.Path;


public interface MyClientGetInterface {
	@GET("/things/{key}")
	MyObject getMyObjectOfKey(@Path("key") String identifier);
}