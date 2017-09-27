package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Stations implements Serializable{
	public String stationDuration ="";
	public String transitionTime = "";
	public int rotationNum = 0;
	public List<Slot> slots = new ArrayList<>();
	public List<StationArea> areas = new ArrayList<>();
	
	
	public static class Slot implements Serializable{
		public List<Integer> stationNums = new ArrayList<>();
		public Set<String> studentIds;
		
		/* if group id is set
		 * studentIds will be loaded from the group.
		 */
		public Long groupId;
	}
	
	public static class StationArea implements Serializable{
		public String title;
		public String color;
		public String description;
		public String x;
		public String y ;
		public String width;
		public String height;
		public String rotate; 
		public String image;
		public int num;
		public String zIndex;
		public StationType type;
		enum StationType{CARPET_L, CARPET_S, KIDNEY,SQUARE,CIRCLE}
	}
	
}
