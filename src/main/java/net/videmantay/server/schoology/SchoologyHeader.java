package net.videmantay.server.schoology;

import java.util.Random;

public class SchoologyHeader {

	public String token="";
	
	private StringBuilder header = new StringBuilder();
	public SchoologyHeader(){
		
		setHeader();
	}
	
	public SchoologyHeader(String token){
		this.token = token;
		setHeader();
	}
	private void setHeader(){
		header.append("OAuth oauth_consumer_key=\"2c38b6df3cb6d7bea0573a649170b6d005a1c6515\",");
		header.append("oauth_nonce=\""+new Random().nextLong()+"\",");
		header.append("oauth_signature=\"468fd6a0405c9b7f33593864ea62256f%26"+ token +"\",");
		header.append("oauth_token=\""+token+"\",");
		header.append("oauth_signature_method=\"PLAINTEXT\",");
		header.append("oauth_timestamp=\""+ System.currentTimeMillis()/1000l +"\",");
		header.append("oauth_version=\"1.0\"");
	}
	
	@Override
	public String toString(){
		return header.toString();
	}
	
}
