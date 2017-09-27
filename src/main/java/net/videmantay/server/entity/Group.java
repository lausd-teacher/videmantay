package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.HashSet;

import net.videmantay.server.constant.GroupingType;

public class Group implements Serializable{
	
	public int num;
	public String title;
	
	public String objective;
	
	public GroupingType type;
	
	public String backGroundColor;
	
	public String textColor;
	
	public String borderColor;
	
	public HashSet<String> students = new HashSet<>();

}
