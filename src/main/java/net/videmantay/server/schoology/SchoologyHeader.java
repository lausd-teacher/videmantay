package net.videmantay.server.schoology;

import java.util.UUID;
import java.util.logging.Logger;

public class SchoologyHeader {

	private Logger log = Logger.getAnonymousLogger();
	public String oauth_token="";
	public String oauth_token_secret = "";
	
	private StringBuilder header = new StringBuilder();
	public SchoologyHeader(){
		
		setHeader();
	}
	
	public SchoologyHeader(String oauth_token){
		this.oauth_token = oauth_token;
		setHeader();
	}
	
	public SchoologyHeader(String oauth_token, String oauth_token_secret){
		this.oauth_token_secret = oauth_token_secret;
		log.info("here is token secret in header " + this.oauth_token_secret);
		this.oauth_token = oauth_token;
		this.oauth_token_secret = oauth_token_secret.split("=")[1];
		log.info("token secret after split " + this.oauth_token_secret);
		setHeader();
	}
	private void setHeader(){
		header.append("OAuth oauth_consumer_key=\"2c38b6df3cb6d7bea0573a649170b6d005a1c6515\",");
		header.append(oauth_token+"\",");
		header.append("oauth_nonce=\""+ UUID.randomUUID().toString()+"\",");
		header.append("oauth_timestamp=\""+ System.currentTimeMillis()/1000l +"\",");
		header.append("oauth_signature_method=\"PLAINTEXT\",");
		header.append("oauth_version=\"1.0\",");
		header.append("oauth_signature=\"468fd6a0405c9b7f33593864ea62256f%26"+ oauth_token_secret +"\",");
		header.append("oauth_verifier=\"\"");
		
		
	}
	
	@Override
	public String toString(){
		return header.toString();
	}
	
}
