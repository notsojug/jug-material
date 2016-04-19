package jug.retrofit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

public class RetrofitBaseTest {
	
	private RestAdapter restAdapter;

	MockWebServer mockServer;

	@Before
	public void setUp() throws Exception{
		mockServer = new MockWebServer();
		mockServer.play();
		restAdapter = new RestAdapter.Builder()
				.setEndpoint(mockServer.getUrl("/").toString())
				.build();
	}
	
	@Test
	public void shouldGetTheCorrectValueForGoodResponse() throws Exception {
		// prepare the server with canned response
		MockResponse mockResponse = new MockResponse();
		mockResponse.setBody("{ \"firstField\"=\"yo\",\n\"secondField\"=3\n }");
		mockResponse.setResponseCode(200);
		mockServer.enqueue(mockResponse);

		// get the client (this can be cached or used as a client per-se)
		MyClientInterface client = restAdapter.create(MyClientInterface.class);
		// invoke the method, get the bean
		MyObject myObjectOfKey = client.getMyObjectOfKey("boh");

		// verify the bean
		assertThat(myObjectOfKey.getFirstField()).contains("yo");
		assertThat(myObjectOfKey.getSecondField()).contains(3);

		// verify that the request was made correctly
		RecordedRequest request = mockServer.takeRequest();
		assertThat(request.getMethod()).isEqualTo("GET");
		assertThat(request.getPath()).isEqualTo("/things/boh");
	}
	
	public static interface MyClientInterface {
		
		@GET("/things/{key}")
		MyObject getMyObjectOfKey(@Path("key") String identifier);
	}
	
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
	
	
}
