package jug.retrofit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.assertj.guava.api.Assertions.assertThat;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.RetrofitError.Kind;
import retrofit.client.ApacheClient;

public class RetrofitApacheClientIT {
	private static final int MY_DEAR_TIMEOUT = 5000;

	// the fake server
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	String serverUrl = "http://127.0.0.1:8083";

	private MyClientGetInterface createRetrofitClient(String url) {
		// prepare apache client config with timeout
		RequestConfig configWithTimeout = RequestConfig.custom()
				.setSocketTimeout(MY_DEAR_TIMEOUT)
				.build();
		
		// build apache client with timeout 
		HttpClient httpClient = HttpClientBuilder.create().
				setDefaultRequestConfig(configWithTimeout)
				.build();
		
		// feed retrofit with the newly created rest client
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setClient(new ApacheClient(httpClient ))
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
