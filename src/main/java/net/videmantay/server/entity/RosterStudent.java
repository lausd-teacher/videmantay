package net.videmantay.server.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.api.services.classroom.model.Student;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Serialize;

@Entity
public class RosterStudent {
	@Id
	//managed by schoology enrollment id;
	public String id;
	@Index
	public String rosterId;
	public String firstName;
	public String lastName;
	public String DOB;
	public String picUrl;
	public Boolean synced = true;
	@Serialize
	public ArrayList<Thumbnail> thumbnails = new ArrayList<>();
	public Boolean useThumbs = false;
	public Integer negPoints =0;
	public Integer posPoints = 0;
	public Boolean glasses = false;
	public String eldLevel="";
	public String readingLevel="";
	public String homeLang ="";
	public Set<String> modifications = new HashSet<>();
	public Boolean IEP = false;
	public String currentSummary ="";
	public String driveFolder;
	public boolean inactive =false;
	public ArrayList<StudentJob>jobs = new ArrayList<>();

}
