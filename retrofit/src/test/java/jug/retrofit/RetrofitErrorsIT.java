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

/**
 * In this test we demonstrate the use of custom error handlers in retrofit. 
 *
 */
@SuppressWarnings("serial")
public class RetrofitErrorsIT {
	// the fake server
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(9084);
	String serverUrl = "http://127.0.0.1:9084";

	private static MyClientGetInterface createRetrofitClient(String url) {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(url)
				.setErrorHandler(new MyErrorHandler())
				.build();
		return restAdapter.create(MyClientGetInterface.class);
	}

	/**
	 * An example error handler, with custom errors per HTTP status
	 */
	static class MyErrorHandler implements ErrorHandler {
		@Override
		public Throwable handleError(RetrofitError cause) {
			switch (cause.getKind()) {
			case CONVERSION:
				// conversion errors are due to incorrect response-to-object conversion.
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

	/*
	 * Some custom exception, imagine these being your business exception, or
	 * client-related exceptions.
	 */
	static class ItemNotFoundException extends RuntimeException{};
	static class UnexpectedErrorException extends RuntimeException{};
	static class JsonSerializationException extends RuntimeException{};
	static class NetworkProblemsException extends RuntimeException{};
	static class BadRequestException extends RuntimeException{};
	
	
	/**
	 * A client that not only throws some business exception, but also returns
	 * {@link Optional}s instead of nulls.
	 */
	class MyErrorHandlingClient {
		private MyClientGetInterface innerClient;
		
		public MyErrorHandlingClient(String uri){
			innerClient = createRetrofitClient(uri);
		}

		/**
		 * Returns a an {@link Optional} of {@link MyObject}, exploiting the
		 * {@link ItemNotFoundException} from the {@link MyErrorHandler}.
		 * 
		 * @param identifier
		 *            the identifier of the requested object.
		 * @return an {@link Optional} of {@link MyObject}, or
		 *         {@link Optional#absent()}, if it is not found.
		 */
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
		wireMockRule.stubFor(get(urlMatching("/things/something"))
					.willReturn(aResponse()
							// 404 should result in an ItemNotFoundException
							// and an absent object
							.withStatus(404)));

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
				// this obviously does not represent any valid JSON response
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
	public void shouldGetUnexpectedErrorExceptionFor500Response() throws Exception {
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
