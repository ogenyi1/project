package ng.optisoft.rosabon.generators;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Base64;

@Component
public class PaystackServiceGenerator {
	
	@Value("${paystack.base.url}")
	private String paystackBaseURL;

	private static String PAYSTACK_BASE_URL;

	private static Retrofit.Builder builder;

	private static Retrofit retrofit;

	private static OkHttpClient.Builder httpClient;

	private static HttpLoggingInterceptor logging;

	@Value("${paystack.base.url}")
	public void setBaseUrl(String m36BaseURL) {
		PaystackServiceGenerator.PAYSTACK_BASE_URL = m36BaseURL;
	}

	private static void init() {
		
		
		builder = new Retrofit.Builder().baseUrl(PAYSTACK_BASE_URL)
				.addConverterFactory(GsonConverterFactory.create());

		retrofit = builder.build();

		httpClient = new OkHttpClient.Builder();

		logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);

	}

	public static <S> S createService(Class<S> serviceClass) {

		if (builder == null)
			init();

		if (!httpClient.interceptors().contains(logging)) {
			httpClient.addInterceptor(logging);
			builder.client(httpClient.build());
			retrofit = builder.build();
		}
		return retrofit.create(serviceClass);
	}

	public static <S> S createService(Class<S> serviceClass, final String token) {

		if (builder == null)
			init();

		if (token != null) {
			httpClient.interceptors().clear();
			httpClient.addInterceptor(logging);
			httpClient.addInterceptor(chain -> {
				Request original = chain.request();
				Request.Builder builder1 = original.newBuilder().header("Authorization", token);
				Request request = builder1.build();
				return chain.proceed(request);
			});
			builder.client(httpClient.build());
			retrofit = builder.build();
		}
		return retrofit.create(serviceClass);
	}

	public static <S> S createService(Class<S> serviceClass, String clientId, String secret) {

		if (builder == null)
			init();

		httpClient.interceptors().clear();
		httpClient.addInterceptor(logging);

		String credentials = clientId + ":" + secret;
		// create Base64 encodet string
		final String basic = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

		httpClient.addInterceptor(chain -> {
			Request original = chain.request();
			Request.Builder builder1 = original.newBuilder().header("Authorization", basic);
			Request request = builder1.build();
			return chain.proceed(request);
		});
		builder.client(httpClient.build());
		retrofit = builder.build();

		return retrofit.create(serviceClass);
	}
}
