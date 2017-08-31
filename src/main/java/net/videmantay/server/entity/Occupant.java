package net.videmantay.server.entity;

import java.io.Serializable;

public class Occupant implements Serializable {
	
	public String firstName;
	public String lastName;
	public String pic;
	public String id;
	public String seatId;
	public OccupantType type;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSeatId() {
		return seatId;
	}

	public void setSeatId(String seatId) {
		this.seatId = seatId;
	}

	public OccupantType getType() {
		return type;
	}

	public void setType(OccupantType type) {
		this.type = type;
	}

	enum OccupantType{STUDENT, FACULTY};

}
