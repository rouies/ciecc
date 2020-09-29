package com.ciecc.common.network.http;

import java.io.InputStream;

public class DefaultHttpClient extends HttpClient<InputStream, InputStream>{

	@Override
	protected InputStream parseArgs(InputStream input) throws HttpException {
		return input;
	}

	@Override
	protected InputStream parseResult(InputStream input,
			HttpResponseHeaders headers)
			throws HttpException {
		return input;
	}


}
