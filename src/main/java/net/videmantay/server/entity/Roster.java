package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	
	public String sid;//schoologyId
	
	
	public String taskListId;
	public String calendarId;
	
	public String teacherFolderId;
	
	@Serialize
	public Schedule schedule;

	@Serialize
	public HashMap<String,String>folders = new HashMap<>();
	
	@Ignore
	public Set<Routine> routines = new HashSet<>();

	@Ignore
	public Set<Incident>incidents= new HashSet<>();
	
	@Ignore
	public RoutineConfig defaultRoutine;
	
	@Ignore
	public Attendance attendance;
	@Ignore
	public List<RosterStudent> students ;
	
	
	@Ignore
	public RosterInfo rosterInfo;
	@Ignore
	public TaskList tasks;

	
	@SafeHtml
	public Roster(){
	
	}

}
