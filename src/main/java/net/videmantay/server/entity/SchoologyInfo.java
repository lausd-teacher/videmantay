package net.videmantay.server.entity;

import java.io.Serializable;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
public class SchoologyInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2441439449361560144L;
	@Id
	public String acctId;
	

	public String sId;
	public OAuth1RequestToken requestToken;
	public OAuth1AccessToken accessToken;
	
}
