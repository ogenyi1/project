package ng.optisoft.rosabon.generators;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Component
public class MyBankStatementGenerator {


	private static String CREDIT_BASE_URL;

	private static Retrofit.Builder builder;

	private static Retrofit retrofit;

	private static OkHttpClient.Builder httpClient;

	private static HttpLoggingInterceptor logging;


	private static void init() {
		
		
		builder = new Retrofit.Builder().baseUrl(CREDIT_BASE_URL)
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
				Request.Builder builder1 = original.newBuilder()
						.header("Authorization", "bearer " + token);
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
		// create Base64 encoded string
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
