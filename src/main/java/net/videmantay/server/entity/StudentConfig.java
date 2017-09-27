package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.List;

public class StudentConfig implements Serializable {

	public List<StudentIncident>incidents;
	public List<StudentWork> assignments;
	public List<StudentAttendance>attendance;
	public List<StudentGoal>goals;
	
}
