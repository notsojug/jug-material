package jug.retrofit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.base.Optional;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.RetrofitError.Kind;
import retrofit.client.ApacheClient;
import retrofit.http.GET;
import retrofit.http.Path;

public class RetrofitApacheClientTest {
	private static final int MY_DEAR_TIMEOUT = 500;

	public static interface MyClientInterface {
		@GET("/things/{key}")
		MyObject getMyObjectOfKey(@Path("key") String identifier);
	}

	public static class MyObject {
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
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	String serverUrl = "http://127.0.0.1:8080";

	private MyClientInterface createRetrofitClient(String url) {
		RequestConfig configWithTimeout = RequestConfig.custom()
				.setSocketTimeout(MY_DEAR_TIMEOUT)
				.build();
		HttpClient httpClient = HttpClientBuilder.create().
				setDefaultRequestConfig(configWithTimeout)
				.build();
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setClient(new ApacheClient(httpClient ))
				.setEndpoint(url)
				.build();
		// based on the rest adapter, create an instace of the client, using
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

		MyClientInterface client = createRetrofitClient(serverUrl);

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
						.withFixedDelay(MY_DEAR_TIMEOUT/2)
						.withStatus(200)
						.withBody("{\n  \"firstField\" = \"I love JSON\",\n  \"secondField\" = 3\n}")));

		MyClientInterface client = createRetrofitClient(serverUrl);

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
						.withFixedDelay(MY_DEAR_TIMEOUT*2)
						.withStatus(200)
						.withBody("{\n  \"firstField\" = \"I love JSON\",\n  \"secondField\" = 3\n}")));

		MyClientInterface client = createRetrofitClient(serverUrl);

		// invoke the method, get the bean
		RetrofitError error = null;
		try {
			client.getMyObjectOfKey("something");
		} catch (RetrofitError e) {
			error = e;
		}
		assertThat(error).isNotNull();
		assertThat(error.getKind()).isEqualTo(Kind.NETWORK);
		assertThat(error.getMessage()).containsIgnoringCase("time").containsIgnoringCase("out");

		// verify that the request was made correctly
		wireMockRule.verify(getRequestedFor(urlEqualTo("/things/something")));
	}
}
