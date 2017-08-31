package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
public class Standard implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9179699746226642563L;
	@Id
	public Long id;
	public String standardCode;
	public String definition;
	public String gradeLevel;
	public String subject;
	public String subSubject;
	public List<String> keyWords;
	
	public String getStandardCode() {
		return standardCode;
	}
	public void setStandardCode(String standardCode) {
		this.standardCode = standardCode;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getGradeLevel() {
		return gradeLevel;
	}
	public void setGradeLevel(String gradeLevel) {
		this.gradeLevel = gradeLevel;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubSubject() {
		return subSubject;
	}
	public void setSubSubject(String subSubject) {
		this.subSubject = subSubject;
	}
	public List<String> getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
	}
	public Long getId() {
		return id;
	}
	

}
