package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import net.videmantay.server.constant.GoalType;

/*
 * Goal is either academic or behavior with
 * a badge attributed for those that meet it.
 * 
 * ex: turn in hw for a week = you earn hw badge(goal url);
 * this is for studentGoal;
 */
@Entity
public class Goal implements Serializable{

	@Id
	public Long id;
	public  String title;
	public String objective;
	public  String description;
	public  String iconUrl;
	//incase you want to attribute some value system
	public Double value;
	public Double completePercentage;
	public ArrayList<String> taskList = new ArrayList<>();
	
	@Index
	public GoalType type;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return iconUrl;
	}
	public void setUrl(String url) {
		this.iconUrl = url;
	}
	public GoalType getType() {
		return type;
	}
	public void setType(GoalType type) {
		this.type = type;
	}
}
