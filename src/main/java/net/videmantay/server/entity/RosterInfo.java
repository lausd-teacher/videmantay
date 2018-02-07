package net.videmantay.server.entity;

import java.io.Serializable;
import java.time.LocalDate;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.Serialize;
import com.googlecode.objectify.annotation.Cache;

@Entity
@Cache
public class RosterInfo implements Serializable{
	@Id
	public String id;
	public String name;
	public String description ="";
	public String start = "";
	public String end= "";
	public String roomNum ="";
	public String color = "red darken-2";
	public String sid;
	public Boolean synced = true;
	
	@Index
	public transient String timestamp;
	@Index
	public   String ownerId;
	@Serialize
	public TeacherInfo teacherInfo = new TeacherInfo();
	
	@OnLoad
	public void setTimestamp(){
		timestamp = LocalDate.now().toString();
	}
	
}