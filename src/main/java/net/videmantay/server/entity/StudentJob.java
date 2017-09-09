package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class StudentJob implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8098272788361413932L;
	@Id
	public Long id;
	public String url;
	public String title;
	public String jobDescription;
	public String studentId;
	@Index
	public boolean current;
	@Index
	public String startDate;
	public String endDate;

}
