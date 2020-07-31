package com.codehack.contact.vo;

public class TracedData {
	 private float ctId;
	 private float ContactId;
	 private String VisitedLocation;
	 private String Date;
	 private float sid;


	 // Getter Methods 

	 public float getCtId() {
	  return ctId;
	 }

	 public float getContactId() {
	  return ContactId;
	 }

	 public String getVisitedLocation() {
	  return VisitedLocation;
	 }

	 public String getDate() {
	  return Date;
	 }

	 public float getSid() {
	  return sid;
	 }

	 // Setter Methods 

	 public void setCtId(float ctId) {
	  this.ctId = ctId;
	 }

	 public void setContactId(float ContactId) {
	  this.ContactId = ContactId;
	 }

	 public void setVisitedLocation(String VisitedLocation) {
	  this.VisitedLocation = VisitedLocation;
	 }

	 public void setDate(String Date) {
	  this.Date = Date;
	 }

	 public void setSid(float sid) {
	  this.sid = sid;
	 }
	}