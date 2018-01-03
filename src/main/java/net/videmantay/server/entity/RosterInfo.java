package net.videmantay.server.entity;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;
import com.googlecode.objectify.annotation.Cache;

@Entity
@Cache
public class RosterInfo implements Serializable{
	@Id
	public Long id;
	public String name;
	public String description;
	public String start;
	public String end;
	public String roomNum;
	public String color;
	@Index
	public   String ownerId;
	@Serialize
	public TeacherInfo teacherInfo = new TeacherInfo();
	
}