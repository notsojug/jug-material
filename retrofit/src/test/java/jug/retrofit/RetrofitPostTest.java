package jug.retrofit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.google.gson.Gson;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

public class RetrofitPostTest {
	
	// Google's json converter (it's used internally in retrofit).
	Gson gson = new Gson();
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8081);
	String serverUrl = "http://127.0.0.1:8081";
	
	/**
	 * This is a very simple interface for a client.
	 */
	public static interface ClientInterface {
		
		/**
		 * As a retrofit limitation, we cannot have <code>void</code> method,
		 * but you can return a generic retrofit's {@link Response}, and ignore
		 * it at runtime.
		 * <p>
		 * Or maybe you can modify your API to return the resulting object after
		 * a POST, so you can return the object in the call.
		 * 
		 * @param toAdd
		 *            the object to add
		 * @return nothing useful.
		 */
		@POST("/things/")
		Response saveMyObjectToIdentifier(@Body MyObject toAdd);
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
		wireMockRule.stubFor(post(urlMatching("/things/"))
	            .willReturn(aResponse()
	                .withStatus(201)));

		// get the client
		ClientInterface client = createRetrofitClient(serverUrl);

		// create object to save
		MyObject objectToSave = MyObject.create("I love JSON", 3);
				
		// invoke the method
		client.saveMyObjectToIdentifier(objectToSave);

		// verify that the request was made correctly
		wireMockRule.verify(postRequestedFor(urlEqualTo("/things/")));
		List<LoggedRequest> requests = wireMockRule.findRequestsMatching(RequestPattern.everything()).getRequests();
		assertThat(requests).hasSize(1);
		final LoggedRequest lastRequest = requests.get(0);
		MyObject fromJson = convertFromJson(lastRequest.getBody(), MyObject.class);
		assertThat(fromJson.getFirstField()).contains("I love JSON");
		assertThat(fromJson.getSecondField()).contains(3);
	}

	private MyObject convertFromJson(final byte[] body, final Class<MyObject> clazz) {
		String jsonBody = new String(body);
		return gson.fromJson(jsonBody, clazz);
	}
}
