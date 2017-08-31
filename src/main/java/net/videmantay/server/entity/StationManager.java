package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;

public class StationManager implements Serializable{
	public String stationDuration;
	public String transitionTime;
	public List<TimeSlot> timeSlots = new ArrayList<>();
	public List<Station> stations;
	
}
