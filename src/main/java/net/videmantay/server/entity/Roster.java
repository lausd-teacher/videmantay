package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.SafeHtml;

import com.google.api.services.tasks.model.TaskList;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;

@Cache
@Entity
public class Roster implements Serializable{
	
	@Id
	public Long id;
	
	@Index
	public String ownerId;
	
	public String sid;//schoologyId
	
	
	public String taskListId;
	public String calendarId;
	
	public String teacherFolderId;

	@Serialize
	public HashMap<String,String>folders = new HashMap<>();
	
	public Set<Routine> routines = new HashSet<>();

	public Set<Incident>incidents= new HashSet<>();
	
	public RoutineConfig defaultRoutine;
	
	@Ignore
	public Attendance attendance;
	
	
	
	@Ignore
	public RosterInfo rosterInfo;
	@Ignore
	public TaskList tasks;
	
	
	
	public void load(){
		//limit to 50 all entities
		
	}
	
	

	@SafeHtml
	public Roster(){
	
	}

}
