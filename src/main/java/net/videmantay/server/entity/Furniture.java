package net.videmantay.server.entity;

import java.io.Serializable;

public class Furniture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8772140337750474129L;
	
	public String top;
	public String left;
	public Double rotate;
	public String type;
	public String kind;
	public Integer zIndex;
	public String transform;
	public String iconUrl;
	public String backgroundColor;
	public StudentSeat[] seats;
	public Double trueTop;
	public Double trueLeft;
	
	
	
	public StudentSeat[] getSeats() {
		return seats;
	}
	public void setSeats(StudentSeat[] seats) {
		this.seats = seats;
	}
	public String getTop() {
		return top;
	}
	public void setTop(String top) {
		this.top = top;
	}
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public Double getRotate() {
		return rotate;
	}
	public void setRotate(Double rotate) {
		this.rotate = rotate;
	}
	
	public Integer getZIndex(){
		return zIndex;
	}
	
	public void setZIndex(Integer zIndex){
		this.zIndex = zIndex;
	}
	
	public void setTransform(String transform){
		this.transform = transform;
	}
	
	public String getTransform(){
		return this.transform;
	}
}
