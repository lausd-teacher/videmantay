package net.videmantay.server.entity;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class GroupNotes implements Serializable{

	@Id
	public Long id;
	@Index
	public Long groupId;
	@Index
	public String dateTime;
	public String description;
	public String activity;
	public Boolean metObjetive;
	public Double duration;
	
}
