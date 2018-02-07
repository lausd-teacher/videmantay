package net.videmantay.server.entity;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
public class SchoologyInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2441439449361560144L;
	@Id
	public String acctId;
	public String uId;
	public String schoolId;
	public String buildingId;
	public SchoologyToken requestToken = new SchoologyToken();
	public SchoologyToken accessToken = new SchoologyToken();
	
	
}
