package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import net.videmantay.server.constant.GradedWorkType;
import net.videmantay.server.constant.SubjectType;

@Entity
public class StudentWork implements Serializable {

	// same id as gradedwork////
	@Id
	public Long id;
	@Index 
	public Long gradedWorkId;
	@Index
	public String studentId;
	public String letterGrade;
	public String message;
	public GradedWorkType type;
	public SubjectType subject;
	public Set<String> attatchments = new HashSet<String>();
	// List of standard to review with accomany links;
	public List<Standard> standardReviews;

	public StudentWork() {

	}

	
}
