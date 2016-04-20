package jug.retrofit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.squareup.okhttp.internal.http.Response;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

public class RetrofitBaseTest {
	
	/**
	 * This is a very simple interface for a client.
	 * <p>
	 * Retrofit annotations are used by the retrofit library to infer the
	 * correct http method, headers, and parameters of the http request.
	 * <p>
	 * The method return type is used to either return a deserialized object (in
	 * json, for this example), or to return a generalized retrofit
	 * {@link Response} type.
	 * <p>
	 * If you strip away the retrofit annotations, you obtain a plain old java
	 * interface {@link MyClientInterface_withoutAnnotations}: imagine
	 * unit-testing a class using this client...it would be very easy to mock
	 * away this!
	 */
	public static interface MyClientInterface {
		@GET("/things/{key}")
		MyObject getMyObjectOfKey(@Path("key") String identifier);
	}

	
	/**
	 * A simple bean to be serialized. 
	 */
	public static class MyObject{
		private String firstField;
		private Integer secondField;
		
		public Optional<String> getFirstField() {
			return Optional.fromNullable(firstField);
		}
		
		public Optional<Integer> getSecondField() {
			return Optional.fromNullable(secondField);
		}
	}
	
	// the fake server
	MockWebServer mockServer;
	String serverUrl;

	@Before
	public void setUp() throws Exception{
		mockServer = new MockWebServer();
		// star the server
		mockServer.play();
		// get the server url
		serverUrl = mockServer.getUrl("/").toString();
	}
	
	/**
	 * Creates the client for the given url.
	 * 
	 * @param url
	 *            the server url.
	 * @return a client with {@link MyClientInterface} interface.
	 */
	private MyClientInterface createRetrofitClient(String url){
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(url)
				.build();
		// based on the rest adapter, create an instace of the client, using
		// MyClientInterface as a reference
		return restAdapter.create(MyClientInterface.class);
	}
	
	@Test
	public void shouldGetTheCorrectValueForGoodResponse() throws Exception {
		// prepare the server with canned response
		MockResponse mockResponse = new MockResponse();
		mockResponse.setBody("{\n  \"firstField\" = \"I love JSON\",\n  \"secondField\" = 3\n}");
		/*
		 * the above string is 
		 * {
         *   "firstField" = "I love JSON",
         *   "secondField" = 3
         * }
		 */
		mockResponse.setResponseCode(200);
		mockServer.enqueue(mockResponse);

		
		MyClientInterface client = createRetrofitClient(serverUrl);
		
		// invoke the method, get the bean
		MyObject myObjectOfKey = client.getMyObjectOfKey("something");

		// verify the bean
		assertThat(myObjectOfKey.getFirstField()).contains("I love JSON");
		assertThat(myObjectOfKey.getSecondField()).contains(3);

		// verify that the request was made correctly
		RecordedRequest request = mockServer.takeRequest();
		assertThat(request.getMethod()).isEqualTo("GET");
		assertThat(request.getPath()).isEqualTo("/things/something");
	}
	
	
	
	/**
	 * This is the same interface as {@link MyClientInterface}, but without annotations. 
	 */
	public static interface MyClientInterface_withoutAnnotations {
		MyObject getMyObjectOfKey(String identifier);
	}
	
}
