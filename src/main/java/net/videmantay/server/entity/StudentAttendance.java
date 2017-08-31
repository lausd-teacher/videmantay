package net.videmantay.server.entity;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class StudentAttendance implements Serializable{

@Id
public String id;

@Index
public String studentId;


@Index
public String date;

@Index
public AttendanceStatus status;

public String arrival;


enum AttendanceStatus{PRESENT, ABSENT, TARDY};
}
