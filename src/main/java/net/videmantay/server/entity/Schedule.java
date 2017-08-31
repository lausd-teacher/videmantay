package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;

@Entity
public class Schedule implements Serializable{
	
	@Id
	public Long id;
	
	public String msg;
	
	@Index
	public String name;
	
	@Serialize
	public Map<String,ScheduleItem> items = new HashMap<>();
	

	
}
