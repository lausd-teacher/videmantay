package net.videmantay.server.entity;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Incident implements Serializable{
	
	@Id
    public Long id;
	@Index
	public  transient String rosterId;
		
	public String name;
	public String imageUrl;
	public int points;
	
	public Incident() {

	}



	public Incident(Long id, String  rosId, String name, String imageUrl, int points) {
		this.id = id;
		this.rosterId =  rosId;
		this.name = name;
		this.imageUrl = imageUrl;
		this.points = points;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}
	
	public String getRosterId(){
		return this.rosterId;
	}

	public void setRosterId(String rosId){
		this.rosterId = rosId;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
	public int getPoints() {
		return this.points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + points;
		result = prime * result + ((rosterId == null) ? 0 : rosterId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Incident other = (Incident) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (points != other.points)
			return false;
		if (rosterId == null) {
			if (other.rosterId != null)
				return false;
		} else if (!rosterId.equals(other.rosterId))
			return false;
		return true;
	}



}
