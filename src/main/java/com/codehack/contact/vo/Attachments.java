package com.codehack.contact.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Attachments {
	@JsonProperty("output.jpg")
	OutputImage outputImage;


 // Getter Methods 

 public OutputImage getOutputImage() {
  return outputImage;
 }

 // Setter Methods 

 public void setOutputImage(OutputImage outputImage) {
  this.outputImage = outputImage;
 }
}