package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Routine implements Serializable{
	
	private static final long serialVersionUID = -2845019435811983254L;
	
	
	@Id
	public Long id;
	
	@Index
	public Long rosterId;
	
	public String title;
	public String descript;
	public String color;
	
	
	
	
	public boolean isDefault;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public Long getRosterId() {
		return this.rosterId;
	}
	
	public void setRosterId(Long rosterId) {
		this.rosterId = rosterId;
	}
	
	public boolean isDefault() {
		return this.isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	} 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Routine other = (Routine) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	

}
