package jug.retrofit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * A very simple sample of retrofit's features.
 *
 */
public class RetrofitGetTest {
	
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
	
	// the fake server
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8080);
	String serverUrl = "http://127.0.0.1:8080";
	
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
		// based on the rest adapter, create an instance of the client, using
		// MyClientInterface as a reference
		return restAdapter.create(MyClientInterface.class);
	}
	
	@Test
	public void shouldGetTheCorrectValueForGoodResponse() throws Exception {
		// prepare the server with canned response
		wireMockRule.stubFor(get(urlMatching("/things/something"))
	            .willReturn(aResponse()
	                .withStatus(200)
	                .withBody("{\n  \"firstField\" = \"I love JSON\",\n  \"secondField\" = 3\n}")));
		/*
		 * the above string is 
		 * {
         *   "firstField" = "I love JSON",
         *   "secondField" = 3
         * }
		 */
		
		MyClientInterface client = createRetrofitClient(serverUrl);
		
		// invoke the method, get the bean
		MyObject myObjectOfKey = client.getMyObjectOfKey("something");

		// verify the bean
		assertThat(myObjectOfKey.getFirstField()).contains("I love JSON");
		assertThat(myObjectOfKey.getSecondField()).contains(3);

		// verify that the request was made correctly
		wireMockRule.verify(getRequestedFor(urlEqualTo("/things/something")));
	}
	
	
	
	/**
	 * This is the same interface as {@link MyClientInterface}, but without annotations. 
	 */
	public static interface MyClientInterface_withoutAnnotations {
		MyObject getMyObjectOfKey(String identifier);
	}
	
}
