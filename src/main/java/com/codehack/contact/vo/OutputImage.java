package com.codehack.contact.vo;

public class OutputImage {
	 private String content_type;
	 private float revpos;
	 private String digest;
	 private float length;
	 private boolean stub;


	 // Getter Methods 

	 public String getContent_type() {
	  return content_type;
	 }

	 public float getRevpos() {
	  return revpos;
	 }

	 public String getDigest() {
	  return digest;
	 }

	 public float getLength() {
	  return length;
	 }

	 public boolean getStub() {
	  return stub;
	 }

	 // Setter Methods 

	 public void setContent_type(String content_type) {
	  this.content_type = content_type;
	 }

	 public void setRevpos(float revpos) {
	  this.revpos = revpos;
	 }

	 public void setDigest(String digest) {
	  this.digest = digest;
	 }

	 public void setLength(float length) {
	  this.length = length;
	 }

	 public void setStub(boolean stub) {
	  this.stub = stub;
	 }
	}