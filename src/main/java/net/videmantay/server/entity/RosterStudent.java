package net.videmantay.server.entity;

import java.util.HashSet;
import java.util.Set;

import com.google.api.services.classroom.model.Student;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;

@Entity
public class RosterStudent {
	
	public String firstName;
	public String lastName;
	public String dob;
	public String picUrl;
	@Id
	public String acct;
	public String imageUrl;
	
	public Long rosterId;
	public String courseId;
	@Ignore
	public Student student = null;

	
	
	public Integer negPoints =0;
	public Integer posPoints = 0;
	public String eDate;
	public Boolean glasses;
	public String eldLevel;
	public String readingLevel;
	public String homeLang;
	public Set<String> modifications = new HashSet<>();
	public Boolean IEP;
	public String currentSummary;
	public String driveFolder;

}
