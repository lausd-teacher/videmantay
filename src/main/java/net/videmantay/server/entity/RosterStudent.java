package net.videmantay.server.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.api.services.classroom.model.Student;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Serialize;

@Entity
public class RosterStudent {
	@Id
	public String id;//for right now simple id (rosterId + acct). later switch this to indexed compositeKey;
	public String firstName;
	public String lastName;
	public String DOB;
	public String picUrl;
	public String acct;
	@Serialize
	public ArrayList<Thumbnail> thumbnails = new ArrayList<>();
	
	public Long rosterId;

	
	
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
	public boolean inactive;
	public ArrayList<StudentJob>jobs = new ArrayList<>();

}
