package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Rubric implements Serializable {

	
	@com.googlecode.objectify.annotation.Id
	public Long Id;
	
	@Index
	public String rosterId;
	public String title;
	public String description;
	public String subject;
	public List<Criterion> criteria =new ArrayList<Criterion>();
	
	
		public static class Criterion implements Serializable{
		
		public String value;
		public String definition;
		}
	
	
}
