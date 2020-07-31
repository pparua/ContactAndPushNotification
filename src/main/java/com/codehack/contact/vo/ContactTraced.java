package com.codehack.contact.vo;

import java.util.ArrayList;

public class ContactTraced {
	 public ArrayList<TracedData> getTracedData() {
		return tracedData;
	}

	public void setTracedData(ArrayList<TracedData> tracedData) {
		this.tracedData = tracedData;
	}

	private String _id;
	 private String _rev;
	 private String type;
	 ArrayList < TracedData > tracedData = new ArrayList < TracedData > ();


	 // Getter Methods 

	 public String get_id() {
	  return _id;
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