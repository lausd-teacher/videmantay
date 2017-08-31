package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;

@Entity
public class RosterInfo implements Serializable{
	@Id
	public Long id;
	public String name;
	public String description;
	public String start;
	public String end;
	public String roomNum;
	@Serialize
	public TeacherInfo teacherInfo;
	
	public static class TeacherInfo implements Serializable{
		public String name;
		public String picUrl;
		public String grade;
		public TeacherInfo(){}
	}

}
