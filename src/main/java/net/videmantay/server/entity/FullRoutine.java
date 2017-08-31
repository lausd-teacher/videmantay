package net.videmantay.server.entity;

import java.io.Serializable;

public class FullRoutine implements Serializable {

	public Routine routine;
	public RoutineConfig routineConfig;
	
	public FullRoutine(Routine routine, RoutineConfig config){
		this.routine = routine;
		this.routineConfig = config;
	}
	
	public FullRoutine(){}
}
