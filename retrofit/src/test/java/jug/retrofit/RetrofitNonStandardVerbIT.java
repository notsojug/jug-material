package jug.retrofit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.RetrofitError.Kind;
import retrofit.client.ApacheClient;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.RestMethod;

/**
 * We want to test a DELETE http request, but with a form in it.
 *
 */
public class RetrofitNonStandardVerbIT {
	
	
	/**
	 * This interface demonstrates that DELETE method does not allow body
	 * parameters.
	 * 
	 * @see RetrofitNonStandardVerbIT#shouldBreakWithInvalidAnnotation()
	 */
	public static interface MyClientInterface_wrong {
		@DELETE("/things/")
		Response deleteThisThing(@Body MyObject theObject);
	}
	
	/**
	 * A customized DELETE method, which allows a body parameter
	 *
	 */
	@Target(METHOD)
	@Retention(RUNTIME)
	@RestMethod(value = "DELETE", hasBody = true)
	public static @interface DELETE_WITH_BODY {
		String value();
	}
	
	/**
	 * An interface using the customized DELETE method.
	 *
	 */
	public static interface MyClientInterface {
		@DELETE_WITH_BODY("/things/")
		Response deleteThisThing(@Body MyObject theObject);
	}
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8085);
	String serverUrl = "http://127.0.0.1:8085";
	
	@Test
	public void shouldBreakWithInvalidAnnotation() throws Exception {
		MyClientInterface_wrong wrong = createRetrofitClient(MyClientInterface_wrong.class);
		
		try {
			wrong.deleteThisThing(new MyObject());
			// an invalid interface should break retrofit
			failBecauseExceptionWasNotThrown(RetrofitError.class);
		} catch (RetrofitError e) {
			assertThat(e.getKind()).isEqualTo(Kind.UNEXPECTED);
			assertThat(e.getCause())
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Non-body HTTP method cannot contain @Body or @TypedOutput");
		}
	}
	
	@Test
	public void shouldGetGoodResponse() throws Exception {
		// expect a delete, return 201
		wireMockRule.stubFor(delete(urlMatching("/things/"))
	            .willReturn(aResponse()
	                .withStatus(201)));
		
		MyClientInterface good = createRetrofitClient(MyClientInterface.class);

		final MyObject theObject = MyObject.create("yo", 15);

		Response response = good.deleteThisThing(theObject);

		assertThat(response.getStatus()).isEqualTo(201);

		// assert that the DELETE had a body with a JSON
		wireMockRule.verify(deleteRequestedFor(urlEqualTo("/things/"))
				.withRequestBody(containing("{\"firstField\":\"yo\",\"secondField\":15}")));
	}
	
	
	
	private <T> T createRetrofitClient(Class<T> iface){
		RestAdapter restAdapter = new RestAdapter.Builder()
				// java's url connection does not support non standard verbs. 
				// using apache client instead...
				.setClient(new ApacheClient())
				.setEndpoint(serverUrl)
				.build();
		return restAdapter.create(iface);
	}
}
