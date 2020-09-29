package com.ciecc.common.network.ssl;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class DefaultSSLConfiguration implements SSLConfiguration{

	@Override
	public KeyManager[] getKeyManagers() throws SSLException {
		return null;
	}

	@Override
	public TrustManager[] getTrustManagers() throws SSLException {
		return new TrustManager[]{
			new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
				}
				
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
				}
			}
		};
	}

	@Override
	public SSLSocketConfiguration getSocketConfiguration() {
		return null;
	}

	@Override
	public String getProtocol() {
		return null;
	}

	@Override
	public String getProvider() {
		return null;
	}

}
