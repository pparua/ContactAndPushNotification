package com.codehack.contact.vo;

import java.util.ArrayList;
import java.util.List;

public class SafeRoute {
	 private String _id;
	 private String _rev;
	 private String type;
	// @JsonProperty("SafeRouteList")
	 List<Route> safeRouteList = new ArrayList<Route>();


	 // Getter Methods 

	 public String get_id() {
	  return _id;
	 }

	 public List<Route> getSafeRouteList() {
		return safeRouteList;
	}

	public void setSafeRouteList(List<Route> safeRouteList) {
		this.safeRouteList = safeRouteList;
	}

	public String get_rev() {
	  return _rev;
	 }

	 public String getType() {
	  return type;
	 }

	 // Setter Methods 

	 public void set_id(String _id) {
	  this._id = _id;
	 }

	 public void set_rev(String _rev) {
	  this._rev = _rev;
	 }

	 public void setType(String type) {
	  this.type = type;
	 }
	}