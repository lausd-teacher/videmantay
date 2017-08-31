package net.videmantay.server.entity;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
public class EducationalLink implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7202743509714253346L;
	
	
	@Id
	private Long id;
	private String descript;
	private String title;
	private String urlPic;
	private String url;
	private Date createOn;
	private Date lastUpdate;
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrlPic() {
		return urlPic;
	}
	public void setUrlPic(String urlPic) {
		this.urlPic = urlPic;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getCreateOn() {
		return createOn;
	}
	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public Long getId() {
		return id;
	}

}
