package net.videmantay.server.entity;

import java.io.Serializable;

public class StudentSeat implements Serializable{
	
	
	/**
	 * 
	 */
	private static transient final long serialVersionUID = 4912335951117569448L;
	
	
	public Integer seatNum;
	public String rosterStudent;
	public Long studentGroup;
	public String color;
	public String url;
	public boolean isEmpty;
	
	
	public boolean isEmpty() {
		if(rosterStudent == null ||rosterStudent.isEmpty()){
			this.isEmpty = true;
		}else{
			this.isEmpty = false;
		}
		
		return isEmpty;
	}
	
	public Integer getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(Integer seatNum) {
		this.seatNum = seatNum;
	}
	public String getRosterStudent() {
		return rosterStudent;
	}
	public void setRosterStudent(String rosterStudent) {
		this.rosterStudent = rosterStudent;
	}
	public Long getStudentGroup() {
		return studentGroup;
	}
	public void setStudentGroup(Long studentGroup) {
		this.studentGroup = studentGroup;
	}

}
