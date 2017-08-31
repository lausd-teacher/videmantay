package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class StudentJob implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8098272788361413932L;
	@Id
	public Long id;
	public String iconUrl;
	public String title;
	public String jobDescription;
	public List<Long> assignedStudents;
	public String category;
	
	
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Long getId() {
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	public List<Long> getAssignedStudents() {
		return assignedStudents;
	}
	public void setAssignedStudents(List<Long> assignedStudents) {
		this.assignedStudents = assignedStudents;
	}

	

}
