package net.videmantay.server.schoology;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;

public class SchoologyAuthorize extends OAuthAuthorizeTemporaryTokenUrl {

	public SchoologyAuthorize() {
		super("https://api.schoologytest.com/v1/oauth/authorize");
		
		// TODO Auto-generated constructor stub
	}
	
	public void setToken(String token){
		this.temporaryToken = token;
	}

}
