package net.videmantay.server.schoology;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SchoologyUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -943989993415967381L;
	public String uid;
	public String school_id;
	public String building_id;
	public String primary_email;
	public String name_first;
	public String name_last;
	public String picture_url;
}
