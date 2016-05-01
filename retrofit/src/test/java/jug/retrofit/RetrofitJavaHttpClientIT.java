package jug.retrofit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.assertj.guava.api.Assertions.assertThat;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.RetrofitError.Kind;
import retrofit.client.Request;
import retrofit.client.UrlConnectionClient;

public class RetrofitJavaHttpClientIT {
	private static final int MY_DEAR_TIMEOUT = 5000;

	// the fake server
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	String serverUrl = "http://127.0.0.1:8080";
	
	/**
	 * An extension of {@link UrlConnectionClient} with a set timeout.
	 */
	class UrlConnectionClientWithTimeout extends UrlConnectionClient{
		
		final int myTimeout;
		
		private UrlConnectionClientWithTimeout(int myTimeout) {
			super();
			this.myTimeout = myTimeout;
		}

		/**
		 * Override default behavior of {@link #openConnection(Request)},
		 * forcing the timeout on read.
		 */
		@Override
		protected HttpURLConnection openConnection(Request request) throws IOException {
			final HttpURLConnection openConnection = super.openConnection(request);
			openConnection.setReadTimeout(myTimeout);
			return openConnection;
		}
	}

	private MyClientGetInterface createRetrofitClient(String url) {
		RestAdapter restAdapter = new RestAdapter.Builder()
				// pass the new client with set timeout
				.setClient(new UrlConnectionClientWithTimeout(MY_DEAR_TIMEOUT))
				.setEndpoint(url)
				.build();
		return restAdapter.create(MyClientGetInterface.class);
	}

	@Test
	public void shouldGetTheCorrectValueForGoodResponse() throws Exception {
		// prepare the server with canned response
		wireMockRule.stubFor(get(urlMatching("/things/something"))
				.willReturn(aResponse()
						.withStatus(200)
						.withBody("{\n  \"firstField\" = \"I love JSON\",\n  \"secondField\" = 3\n}")));

		MyClientGetInterface client = createRetrofitClient(serverUrl);

		// invoke the method, get the bean
		MyObject myObjectOfKey = client.getMyObjectOfKey("something");

		// verify the bean
		assertThat(myObjectOfKey.getFirstField()).contains("I love JSON");
		assertThat(myObjectOfKey.getSecondField()).contains(3);

		// verify that the request was made correctly
		wireMockRule.verify(getRequestedFor(urlEqualTo("/things/something")));
	}
	
	@Test
	public void shouldGetTheCorrectValueForGoodResponse_withDelayWithinTimeoutLimit() throws Exception {
		// prepare the server with canned response
		wireMockRule.stubFor(get(urlMatching("/things/something"))
				.willReturn(aResponse()
						// less delay than max timeout
						.withFixedDelay(MY_DEAR_TIMEOUT/2)
						.withStatus(200)
						.withBody("{\n  \"firstField\" = \"I love JSON\",\n  \"secondField\" = 3\n}")));

		MyClientGetInterface client = createRetrofitClient(serverUrl);

		// invoke the method, get the bean
		MyObject myObjectOfKey = client.getMyObjectOfKey("something");

		// verify the bean
		assertThat(myObjectOfKey.getFirstField()).contains("I love JSON");
		assertThat(myObjectOfKey.getSecondField()).contains(3);

		// verify that the request was made correctly
		wireMockRule.verify(getRequestedFor(urlEqualTo("/things/something")));
	}
	
	@Test
	public void shouldTimeout_withDelayOverTimeoutLimit() throws Exception {
		// prepare the server with canned response
		wireMockRule.stubFor(get(urlMatching("/things/something"))
				.willReturn(aResponse()
						// more delay than timeout
						.withFixedDelay(MY_DEAR_TIMEOUT*2)
						.withStatus(200)
						.withBody("{\n  \"firstField\" = \"I love JSON\",\n  \"secondField\" = 3\n}")));

		MyClientGetInterface client = createRetrofitClient(serverUrl);

		// we are sure we will get an error
		RetrofitError error = null;
		try {
			client.getMyObjectOfKey("something");
			failBecauseExceptionWasNotThrown(RetrofitError.class);
		} catch (RetrofitError e) {
			error = e;
		}
		
		// the Retrofit error holds info regarding the type of error
		assertThat(error).isNotNull();
		assertThat(error.getKind()).isEqualTo(Kind.NETWORK);
		assertThat(error.getMessage()).containsIgnoringCase("time").containsIgnoringCase("out");

		// verify that the request was made correctly
		wireMockRule.verify(getRequestedFor(urlEqualTo("/things/something")));
	}
}
