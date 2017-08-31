package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.Set;

import net.videmantay.server.constant.GroupingType;



public class StudentGroup implements Serializable{
	
	
	public Long id;
	
	public String title;
	
	public String objective;
	
	public GroupingType type;
	
	public String backGroundColor;
	
	public String textColor;
	
	public String borderColor;
	
	public Set<String> studentIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public GroupingType getType() {
		return type;
	}

	public void setType(GroupingType type) {
		this.type = type;
	}

	public String getBackGroundColor() {
		return backGroundColor;
	}

	public void setBackGroundColor(String backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public Set<String> getStudents() {
		return studentIds;
	}

	public void setStudents(Set<String> students) {
		this.studentIds = students;
	}
	
}
