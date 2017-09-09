package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;

@Entity
public class JobBoard implements Serializable{

	@Id
	public Long id;
	@Index
	public String startDate;
	@Index
	public String endDate;
	
	@Serialize
	public List<Job> jobs;
	
	
	public class Job implements Serializable{
		public String url;
		//Title must be unique
		public String title;
		public String description;
		public String requirements;
		public int orderNum;
		public HashSet<String> studentIds = new HashSet<>();
	}
}
