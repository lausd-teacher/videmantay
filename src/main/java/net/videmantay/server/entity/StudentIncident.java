package net.videmantay.server.entity;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class StudentIncident implements Serializable {

	private static final long serialVersionUID = -8576611663190914147L;
	
	@Id
	public Long id;

	
	@Index
	public String studentId;
	
	@Index
	public Long incidentId;
	
	@Index
	public String date;

	public String imageUrl;
	
	public String name;

	public int points;
	
	public String eventId;
	public String mediaUrl;
	public String summary;
	public Boolean parentsContacted;
	public String parentContactDate;
	public String parentContactSummary;
	
	
}
