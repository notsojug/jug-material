package jug.retrofit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

public class RetrofitPostTest {
	
	// Google's json converter (it's used internally in retrofit).
	Gson gson = new Gson();
	
	/**
	 * This is a very simple interface for a client.
	 */
	public static interface ClientInterface {
		
		/**
		 * As a retrofit limitation, we cannot have <code>void</code> method,
		 * but you can return a generic retrofit's {@link Response}, and ignore
		 * it at runtime.
		 * 
		 * @param toAdd the object to add
		 * @return nothing useful.
		 */
		@POST("/things/")
		Response saveMyObjectToIdentifier(@Body MyObject toAdd);
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
		
		public static MyObject create(String firstField, Integer secondField) {
			MyObject toreturn = new MyObject();
			toreturn.firstField = firstField;
			toreturn.secondField = secondField;
			return toreturn;
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
	 * @return a client with {@link ClientInterface} interface.
	 */
	private ClientInterface createRetrofitClient(String url){
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(url)
				.build();
		return restAdapter.create(ClientInterface.class);
	}
	
	@Test
	public void shouldGetTheCorrectValueForGoodResponse() throws Exception {
		// prepare the server with canned response
		MockResponse mockResponse = new MockResponse();
		mockResponse.setResponseCode(201);
		mockServer.enqueue(mockResponse);

		// get the client
		ClientInterface client = createRetrofitClient(serverUrl);

		// create object to save
		MyObject objectToSave = MyObject.create("I love JSON", 3);
				
		// invoke the method
		client.saveMyObjectToIdentifier(objectToSave);

		// verify that the request was made correctly
		RecordedRequest request = mockServer.takeRequest();
		assertThat(request.getMethod()).isEqualTo("POST");
		assertThat(request.getPath()).isEqualTo("/things/");
		MyObject fromJson = convertFromJson(request.getBody(), MyObject.class);
		assertThat(fromJson.getFirstField()).contains("I love JSON");
		assertThat(fromJson.getSecondField()).contains(3);
	}

	private MyObject convertFromJson(final byte[] body, final Class<MyObject> clazz) {
		String jsonBody = new String(body);
		return gson.fromJson(jsonBody, clazz);
	}
}
