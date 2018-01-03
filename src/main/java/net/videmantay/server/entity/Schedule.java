package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.*;

@Entity
public class Schedule implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1453954588792920511L;

	@Id
	public Long id;
	@Serialize
	public List<ScheduleItem> items = new ArrayList<>();
		
}
