package net.videmantay.server.schoology;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;


import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;

public class SchoologyRequestToken{
	
	private final Logger log = Logger.getLogger(SchoologyRequestToken.class);
	private static String url = "https://api.schoologytest.com/v1/oauth/request_token";
	
	public SchoologyRequestToken(HttpServletResponse resp) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAuthorization(new SchoologyHeader().toString());
		HttpRequestFactory requestFac = new NetHttpTransport().createRequestFactory();
		HttpRequest request = requestFac.buildGetRequest(new GenericUrl(url));
		request.setHeaders(headers);
		HttpResponse response = request.execute();
		String res = response.parseAsString();
		String[] resParts = res.split("&");
		//so the authorization page must be as followed not the api calls thought
		String redirect = "https://lausd.schoologytest.com/oauth/authorize?"+ URLEncoder.encode(resParts[0]
				+ "&oauth_callback=https://drsammyleemag.appspot.com/schoologycallback");
		log.info("redirect is: " + redirect);
		resp.sendRedirect(redirect);
	}

}
