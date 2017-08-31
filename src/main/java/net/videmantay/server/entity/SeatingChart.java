package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.ArrayList;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Serialize;


@Entity
public class SeatingChart implements Serializable {
	

	private static final long serialVersionUID = 2656470746976405698L;
	
	@Id
	public Long id;
	

	
	@Serialize
	public ArrayList<Furniture> furniture = new ArrayList<Furniture>();

	public Long getId() {
		return id;
	}
	
	
	
	

}
