package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
@Cache
public class StudentRoll implements Serializable {
	
	@Id
	Long id;
	
	Date date;
	
	@Index
	Long studentId;
	
	boolean isPresent;
	boolean isTardy;
	public String arrivalTime;

	public StudentRoll() {
	
	}

	public Long getId() {
		return this.id;
	}

	public Date getDate() {
		return this.date;
	}

	public Long getStudentId() {
		return this.studentId;
	}

	public boolean isPresent() {
		return this.isPresent;
	}
	
	public boolean isTardy(){
		return isTardy;
	}
	
	public String getArrivalTime(){
		return this.arrivalTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public void setPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}
	
	public void setTardy(boolean isTardy){
		this.isTardy = isTardy;
	}
	
	public void setArrivalTime(String time){
		this.arrivalTime = time;
	}
	
	
}
