package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class TimeSlot implements Serializable{

	public String start;
	public int orderNum;
	//should be a date but javascript likes strings
	public String slotDuration;
	
	/*
	 * rules for slots
	 * stations and student must be unique to each slot
	 * ie slot 1 and slot 2 cannot share any data.
	 */
	public List<Slot> slots;
	
	public static class Slot{
		public int stationNum;
		public Set<String> studentIds;
		
		/* if group id is set
		 * studentIds will be loaded from the group.
		 */
		public Long groupId;
	}
	
}
