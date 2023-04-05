package com.rova.transactionService.http.implementaion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rova.transactionService.exception.GeneralException;
import com.rova.transactionService.exception.RemoteServiceException;
import com.rova.transactionService.http.HttpClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HttpClientImplementation implements HttpClient {
	private final OkHttpClient okHttpClient;
	private final Gson gson;
	private final ObjectMapper objectMapper;

	@Override
	public Response post(Map<String, String> headerList, String jsonPayload, String url) throws IOException {
		log.info("Making POST request with header {}, jsonPayload {} and url {}", headerList, jsonPayload, url);

		Request request = new Request.Builder().post(
			RequestBody.create(jsonPayload, MediaType.parse("application/json"))
		).headers(Headers.of(headerList)).url(url).build();

		Call call = okHttpClient.newCall(request);
		return call.execute();
	}

    @Override
	@SneakyThrows
	public <T> T post(Map<String, String> headerList, String jsonPayload, String url, Class<T> t) {
		return
			responseToObject(post(headerList, jsonPayload, url), t);
	}

	private <T> T responseToObject(Response r, Class<T> t) {
		log.info("--> Converting response:: {} to object :: {}", r, t);

		try (r) {
			return toPojo(r.body().string(), t);
		} catch (Exception e) {
			log.info("--> Error converting response to object :: {}", e);
			throw new RemoteServiceException(e.getMessage());
		}
	}

	@Override
	public Response get(Map<String, String> headerList, Map<String, Object> params, String url) throws IOException {
		log.info("Making GET request with header {}, params {} and url {}", headerList, params, url);
		URL httpUrl = UriComponentsBuilder.fromHttpUrl(url).build(params).toURL();

		Request request = new Request.Builder().get().headers(Headers.of(headerList)).url(httpUrl).build();

		Call call = okHttpClient.newCall(request);
		return call.execute();
	}

    @Override
	@SneakyThrows
	public <T> T get(Map<String, String> headerList, Map<String, Object> params, String url, Class<T> t) {
		return responseToObject(get(headerList, params, url), t);
	}

	@Override
	public <T> T toPojo(final String  o, Class<T> type) {
		log.info("--> conversion request for object {} to type {}", o, type);
		try {
			return objectMapper.readValue(o, type);

		}catch (Exception e){
			log.error("--> conversion of json  to object error {} ", e.getMessage());
			throw new GeneralException("Error while parsing response");
		}
	}

	@Override
	public String toJson(Object src) {
		log.info("--> converting to json string", src);
		try{
			return objectMapper.writeValueAsString(src);
		}catch (Exception e){
			log.error("conversion to json string error :: {}", e);
			return "{}";
		}
	}
}
