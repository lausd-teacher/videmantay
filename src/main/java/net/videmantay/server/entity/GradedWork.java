package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import net.videmantay.server.constant.GradedWorkType;

@Entity
public class GradedWork implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1028483424931330106L;
	
	@Id
	public Long id; 
	@Index
	public String rosterId;
	@Index
	public GradedWorkType type = GradedWorkType.HOMEWORK;
	public String assignedDate;
	public Double pointsPossilbe;
	public Boolean finishedGrading = false;
	@Index
	public String dueDate;
	
	public Set<String> attatchments = new HashSet<String>();
	
	public Long rubricId;
	
	@Index
	public ArrayList<String> assignedTo= new ArrayList<String>();
	
	@Ignore
	public Rubric rubric;
	
	
	
}
