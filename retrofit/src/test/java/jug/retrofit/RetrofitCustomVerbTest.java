package jug.retrofit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.client.RequestPatternBuilder;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import retrofit.RestAdapter;
import retrofit.client.ApacheClient;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.RestMethod;

/**
 * You can define your own verbs...
 * <p>
 * Don't try this at home.
 * <p>
 * And don't ever think about putting this in production. 
 *
 */
public class RetrofitCustomVerbTest {
	@Target(METHOD)
	@Retention(RUNTIME)
	@RestMethod(value = "SIGN", hasBody = true)
	public static @interface SIGN {
		String value();
	}
	
	public static interface MyClientInterface {
		@SIGN("/documents/")
		Response signThisThing(@Body MyObject theObject);
	}
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	String serverUrl = "http://127.0.0.1:8080";
	
	@Test
	public void shouldGetGoodResponse() throws Exception {
		wireMockRule.stubFor(any(urlMatching("/documents/"))
	            .willReturn(aResponse()
	                .withStatus(201)));
		
		MyClientInterface wrong = createRetrofitClient(MyClientInterface.class);

		final MyObject theObject = MyObject.create("yo", 15);

		Response response = wrong.signThisThing(theObject);

		assertThat(response.getStatus()).isEqualTo(201);

		wireMockRule.verify(new RequestPatternBuilder(new RequestMethod("SIGN"), urlEqualTo("/documents/"))
				.withRequestBody((containing("{\"firstField\":\"yo\",\"secondField\":15}"))));
	}
	
	
	
	private <T> T createRetrofitClient(Class<T> iface){
		RestAdapter restAdapter = new RestAdapter.Builder()
				// java's url connection does not support custom verbs...
				.setClient(new ApacheClient())
				.setEndpoint(serverUrl)
				.build();
		return restAdapter.create(iface);
	}
}
