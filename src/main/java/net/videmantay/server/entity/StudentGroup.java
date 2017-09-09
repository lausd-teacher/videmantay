package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.Set;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import net.videmantay.server.constant.GroupingType;


@Entity
public class StudentGroup implements Serializable{
	
	@Id
	public Long id;
	
	@Index
	public String groupId;
	
	public String title;
	
	public String objective;
	
	public GroupingType type;
	
	public String backGroundColor;
	
	@Index
	public String studentId;

	
}
