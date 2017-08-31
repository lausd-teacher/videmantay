package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Serialize;

@Entity
public class RoutineConfig implements Serializable {
	
	/* the id is the same as its routine counter part for easy access */
	@Id
	public Long id;
	
	@Serialize
	public Set<StudentGroup> groups = new HashSet<>();
	
	@Serialize
	public Set<Procedure> procedures = new HashSet<>();
	
	@Serialize
	public StationManager stations;
	
}
