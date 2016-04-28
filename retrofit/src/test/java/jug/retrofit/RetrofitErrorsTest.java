package jug.retrofit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.assertj.guava.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.base.Optional;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

@SuppressWarnings("serial")
public class RetrofitErrorsTest {
	// the fake server
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	String serverUrl = "http://127.0.0.1:8080";

	private static MyClientGetInterface createRetrofitClient(String url) {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(url)
				.setErrorHandler(new MyErrorHandler())
				.build();
		return restAdapter.create(MyClientGetInterface.class);
	}

	static class MyErrorHandler implements ErrorHandler {
		@Override
		public Throwable handleError(RetrofitError cause) {
			switch (cause.getKind()) {
			case CONVERSION:
				return new JsonSerializationException();
			case HTTP: {
				switch (cause.getResponse().getStatus()) {
				case 404:
					return new ItemNotFoundException();
				case 400:
					return new BadRequestException();
				default:
					return new UnexpectedErrorException();
				}
			}
			case NETWORK:
				return new NetworkProblemsException();
			case UNEXPECTED:
				return new UnexpectedErrorException();
			default:
				return new IllegalArgumentException("unexpected kind " + cause.getKind());
			}
		}
	}
	
	static class ItemNotFoundException extends RuntimeException{};
	static class UnexpectedErrorException extends RuntimeException{};
	static class JsonSerializationException extends RuntimeException{};
	static class NetworkProblemsException extends RuntimeException{};
	static class BadRequestException extends RuntimeException{};
	
	class MyErrorHandlingClient {
		private MyClientGetInterface innerClient;
		
		public MyErrorHandlingClient(String uri){
			innerClient = createRetrofitClient(uri);
		}
		
		public Optional<MyObject> getMyObjectOfKey(String identifier){
			try {
				return Optional.of(innerClient.getMyObjectOfKey(identifier));
			} catch (ItemNotFoundException e) {
				return Optional.<MyObject> absent();
			}
		}
	}
	
	@Test
	public void shouldGetTheCorrectValueForGoodResponse() throws Exception {
		// prepare the server with canned response
		wireMockRule.stubFor(get(urlMatching("/things/something")).willReturn(aResponse().withStatus(200)
				.withBody("{\n  \"firstField\" = \"I love JSON\",\n  \"secondField\" = 3\n}")));

		MyErrorHandlingClient client = new MyErrorHandlingClient(serverUrl);

		// invoke the method, get the bean
		Optional<MyObject> maybeMyObject = client.getMyObjectOfKey("something");

		// verify the bean
		assertThat(maybeMyObject).isPresent();
		MyObject myObjectOfKey = maybeMyObject.get();
		assertThat(myObjectOfKey.getFirstField()).contains("I love JSON");
		assertThat(myObjectOfKey.getSecondField()).contains(3);

		// verify that the request was made correctly
		wireMockRule.verify(getRequestedFor(urlEqualTo("/things/something")));
	}
	
	@Test
	public void shouldGetEmptyValueFor404Response() throws Exception {
		// prepare the server with canned response
		wireMockRule.stubFor(get(urlMatching("/things/something")).willReturn(aResponse().withStatus(404)));

		MyErrorHandlingClient client = new MyErrorHandlingClient(serverUrl);

		// invoke the method, get the bean
		Optional<MyObject> maybeMyObject = client.getMyObjectOfKey("something");

		// verify the bean
		assertThat(maybeMyObject).isAbsent();

		// verify that the request was made correctly
		wireMockRule.verify(getRequestedFor(urlEqualTo("/things/something")));
	}
	
	@Test
	public void shouldGetGetJsonSerializationExceptionForGarbageResponse() throws Exception {
		// prepare the server with canned response
		wireMockRule.stubFor(get(urlMatching("/things/something")).willReturn(aResponse().withStatus(200)
				.withBody("{\n  \"yabbadabbadoo")));

		MyErrorHandlingClient client = new MyErrorHandlingClient(serverUrl);

		try {
			client.getMyObjectOfKey("something");
			failBecauseExceptionWasNotThrown(JsonSerializationException.class);
		} catch (JsonSerializationException e) {
			// expected
		}


		// verify that the request was made correctly
		wireMockRule.verify(getRequestedFor(urlEqualTo("/things/something")));
	}
	
	@Test
	public void shouldGetUnexpectedErrorExceptionForGarbageResponse() throws Exception {
		// prepare the server with canned response
		wireMockRule.stubFor(get(urlMatching("/things/something")).willReturn(aResponse().withStatus(500)));

		MyErrorHandlingClient client = new MyErrorHandlingClient(serverUrl);

		try {
			client.getMyObjectOfKey("something");
			failBecauseExceptionWasNotThrown(UnexpectedErrorException.class);
		} catch (UnexpectedErrorException e) {
			// expected
		}

		// verify that the request was made correctly
		wireMockRule.verify(getRequestedFor(urlEqualTo("/things/something")));
	}
}
