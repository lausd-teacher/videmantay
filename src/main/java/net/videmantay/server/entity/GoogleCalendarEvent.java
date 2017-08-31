package net.videmantay.server.entity;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class GoogleCalendarEvent implements Serializable {

   @Id
   Long id;
   
   
   String eventId;
   
   @Index
   Long gradedWorkId;

public GoogleCalendarEvent(Long id, String eventId, Long gradedWorkId) {
	
	this.id = id;
	this.eventId = eventId;
	this.gradedWorkId = gradedWorkId;
}

public GoogleCalendarEvent() {

}

public Long getId() {
	return this.id;
}

public String getEventId() {
	return this.eventId;
}

public Long getGradedWorkId() {
	return this.gradedWorkId;
}

public void setId(Long id) {
	this.id = id;
}

public void setEventId(String eventId) {
	this.eventId = eventId;
}

public void setGradedWorkId(Long gradedWorkId) {
	this.gradedWorkId = gradedWorkId;
}

}
