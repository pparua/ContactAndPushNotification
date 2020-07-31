package com.codehack.contact.vo;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GridViolations{
 private String _id;
 private String _rev;
 private String clientId;
 private float locId;
 private String locDesc;
 private String type;
 private float vId;
 private String vDesc;
 ArrayList <Coordinates> vCoord = new ArrayList <Coordinates> ();
 public float getvId() {
	return vId;
}

public void setvId(float vId) {
	this.vId = vId;
}

public String getvDesc() {
	return vDesc;
}

public void setvDesc(String vDesc) {
	this.vDesc = vDesc;
}

public ArrayList<Coordinates> getvCoord() {
	return vCoord;
}

public void setvCoord(ArrayList<Coordinates> vCoord) {
	this.vCoord = vCoord;
}

public float getvTime() {
	return vTime;
}

public void setvTime(float vTime) {
	this.vTime = vTime;
}

private float vTime;
 private String imgName;
 @JsonProperty("_attachments")
 Attachments attachments;


 // Getter Methods 

 public String get_id() {
  return _id;
 }

 public String get_rev() {
  return _rev;
 }

 public String getClientId() {
  return clientId;
 }

 public float getLocId() {
  return locId;
 }

 public String getLocDesc() {
  return locDesc;
 }

 public String getType() {
  return type;
 }

 public float getVTime() {
  return vTime;
 }

 public String getImgName() {
  return imgName;
 }

 public Attachments getAttachments() {
  return attachments;
 }

 // Setter Methods 

 public void set_id(String _id) {
  this._id = _id;
 }

 public void set_rev(String _rev) {
  this._rev = _rev;
 }

 public void setClientId(String clientId) {
  this.clientId = clientId;
 }

 public void setLocId(float locId) {
  this.locId = locId;
 }

 public void setLocDesc(String locDesc) {
  this.locDesc = locDesc;
 }

 public void setType(String type) {
  this.type = type;
 }


 public void setVTime(float vTime) {
  this.vTime = vTime;
 }

 public void setImgName(String imgName) {
  this.imgName = imgName;
 }

 public void setAttachments(Attachments attachments) {
  this.attachments = attachments;
 }
}

