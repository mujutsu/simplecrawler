package com.mujutsu.simplecrawler;

import java.time.ZonedDateTime;
import java.util.Set;

public class ApartmentEntry {
	private Long id;
	private String link;
	private String type;
	private Double priceInRon;
	private ZonedDateTime timeOfPosting;
	private Set<String> imagesCollection;

	public ApartmentEntry() {

	}

	public ApartmentEntry(Long id, String link, String type, Double priceInRon, ZonedDateTime timeOfPosting,
			Set<String> imagesCollection) {
		super();
		this.id = id;
		this.link = link;
		this.type = type;
		this.priceInRon = priceInRon;
		this.timeOfPosting = timeOfPosting;
		this.imagesCollection = imagesCollection;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getPriceInRon() {
		return priceInRon;
	}

	public void setPriceInRon(Double priceInRon) {
		this.priceInRon = priceInRon;
	}

	public ZonedDateTime getTimeOfPosting() {
		return timeOfPosting;
	}

	public void setTimeOfPosting(ZonedDateTime timeOfPosting) {
		this.timeOfPosting = timeOfPosting;
	}

	public Set<String> getImagesCollection() {
		return imagesCollection;
	}

	public void setImagesCollection(Set<String> imagesCollection) {
		this.imagesCollection = imagesCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imagesCollection == null) ? 0 : imagesCollection.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((priceInRon == null) ? 0 : priceInRon.hashCode());
		result = prime * result + ((timeOfPosting == null) ? 0 : timeOfPosting.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ApartmentEntry other = (ApartmentEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imagesCollection == null) {
			if (other.imagesCollection != null)
				return false;
		} else if (!imagesCollection.equals(other.imagesCollection))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (priceInRon == null) {
			if (other.priceInRon != null)
				return false;
		} else if (!priceInRon.equals(other.priceInRon))
			return false;
		if (timeOfPosting == null) {
			if (other.timeOfPosting != null)
				return false;
		} else if (!timeOfPosting.equals(other.timeOfPosting))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ApartmentEntry [id=" + id + ", link=" + link + ", type=" + type + ", priceInRon=" + priceInRon
				+ ", timeOfPosting=" + timeOfPosting + ", imagesCollection=" + imagesCollection + "]";
	}
}
