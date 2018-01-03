package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ScheduleItem implements Serializable{


	/**
	 * 
	 */
	private static final transient long serialVersionUID = 612397185298477835L;


	public String id;
	

	public String title;
	
	//verify date string
	

	public String start;
	
	//verify date string
	

	public String end;

	public String img;
	

	public String constraint;
	

	public ArrayList<String> className = new ArrayList<>();

	public String color;
	

	public String textColor;
	

	public String backgroundColor;
	

	public String borderColor;
	
	public ArrayList<Integer> dow = new ArrayList<>();
	
}
