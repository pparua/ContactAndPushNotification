package com.codehack.contact.vo;

public class Zone {
	 private float sid;
	 private String Country;
	 private String State;
	 private String City;
	 private String ContainmentZone;
	 private float Severity;


	 // Getter Methods 

	 public float getSid() {
	  return sid;
	 }

	 public String getCountry() {
	  return Country;
	 }

	 public String getState() {
	  return State;
	 }

	 public String getCity() {
	  return City;
	 }

	 public String getContainmentZone() {
	  return ContainmentZone;
	 }

	 public float getSeverity() {
	  return Severity;
	 }

	 // Setter Methods 

	 public void setSid(float sid) {
	  this.sid = sid;
	 }

	 public void setCountry(String Country) {
	  this.Country = Country;
	 }

	 public void setState(String State) {
	  this.State = State;
	 }

	 public void setCity(String City) {
	  this.City = City;
	 }

	 public void setContainmentZone(String ContainmentZone) {
	  this.ContainmentZone = ContainmentZone;
	 }

	 public void setSeverity(float Severity) {
	  this.Severity = Severity;
	 }
	}