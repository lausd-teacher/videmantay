package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import net.videmantay.server.constant.GradedWorkType;
import net.videmantay.server.constant.StudentWorkStatus;
import net.videmantay.server.constant.SubjectType;

@Entity
public class StudentWork implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5494828880181012173L;
	// same id as gradedwork////
	@Id
	public Long id;
	@Index 
	public Long gradedWorkId;
	@Ignore
	GradedWork gradedWork = null;
	@Index
	public String studentId;
	public Double percentage;
	public Double points;
	public String letterGrade;
	public String message;
	public GradedWorkType type;
	public SubjectType subject;
	public StudentWorkStatus status;
	public String dateTaken;
	public Set<String> attachments = new HashSet<String>();
	// List of standard to review with accomany links;
	public StudentWork() {

	}

	
}
