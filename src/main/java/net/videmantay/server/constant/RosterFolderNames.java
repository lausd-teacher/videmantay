package net.videmantay.server.constant;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class RosterFolderNames {

	public static final String STUDENTS = "Students";
	public static final String SHARED = "Shared";
	public static final String SHARED_ASSIGNMENTS= "Assignements";
	public static final String SHARED_SHOWCASE = "Showcase";
	public static final String FORMS = "Forms";
	public static final String PUBLIC = "Public";
	
	public static List<String> getNames(){
		return ImmutableList.of(STUDENTS, SHARED, SHARED_ASSIGNMENTS,SHARED_SHOWCASE,FORMS,PUBLIC);
	}
	
	private RosterFolderNames(){}
	
}
