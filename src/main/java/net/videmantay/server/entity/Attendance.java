package net.videmantay.server.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;


@Entity
public class Attendance implements Serializable{

	
	private static final long serialVersionUID = -3843376810950877476L;

//String id will match today's date
	//there will be no collision and ensure only one attendance per today's date
	//here roster will have to be parent
@Id
public String id;

public Boolean completed = false;

public Set<String> studentPresent = new HashSet<String>();
public Set<String> studentAbsent =new HashSet<String>();
//tardy student will still fall under present category
public Set<String> allStudents  =new HashSet<String>();
public Set<String> studentTardy  =new HashSet<String>();
public List<Key<StudentAttendance>>attendanceKeys = new ArrayList<Key<StudentAttendance>>();

@Ignore
public Set<StudentAttendance> studentAttendance = new HashSet<StudentAttendance>();

public Attendance(){
	id = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
}





}
