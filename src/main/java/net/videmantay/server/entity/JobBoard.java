package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
public class JobBoard implements Serializable{

	@Id
	public Long id;
	@Index
	public String startDate;
	@Index
	public String endDate;
	@Ignore
	public List<StudentJob> jobs;

	public List<Key<StudentJob>> jobKeys;
}
