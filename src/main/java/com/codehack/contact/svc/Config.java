package com.codehack.contact.svc;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Config implements EnvironmentAware{
private String cloudantDbUrl;
private String cloudantDbKey;
private String cMProductToken;
private String cMApiUrl;
private String objectStoreUrl;
private String apiKeyTextLocalSms;
private String urlTextLocalSms;
private String alternateRouteDocType;
private String adminContactDocType;
private String contactTracedDocId;
private String sevireZoneDocId;
private String floorLayoutDocType;
private String gridViolationDocType;
private String socialDistanceViolationId;

public String getSocialDistanceViolationId() {
	return socialDistanceViolationId;
}

public void setSocialDistanceViolationId(String socialDistanceViolationId) {
	this.socialDistanceViolationId = socialDistanceViolationId;
}

public String getGridViolationDocType() {
	return gridViolationDocType;
}

public void setGridViolationDocType(String gridViolationDocType) {
	this.gridViolationDocType = gridViolationDocType;
}

public String getFloorLayoutDocType() {
	return floorLayoutDocType;
}

public void setFloorLayoutDocType(String floorLayoutDocType) {
	this.floorLayoutDocType = floorLayoutDocType;
}

public String getContactTracedDocId() {
	return contactTracedDocId;
}

public void setContactTracedDocId(String contactTracedDocId) {
	this.contactTracedDocId = contactTracedDocId;
}

public String getSevireZoneDocId() {
	return sevireZoneDocId;
}

public void setSevireZoneDocId(String sevireZoneDocId) {
	this.sevireZoneDocId = sevireZoneDocId;
}

public String getAdminContactDocType() {
	return adminContactDocType;
}

public void setAdminContactDocType(String adminContactDocType) {
	this.adminContactDocType = adminContactDocType;
}

public String getAlternateRouteDocType() {
	return alternateRouteDocType;
}

public void setAlternateRouteDocType(String alternateRouteDocType) {
	this.alternateRouteDocType = alternateRouteDocType;
}

public String getUrlTextLocalSms() {
	return urlTextLocalSms;
}

public void setUrlTextLocalSms(String urlTextLocalSms) {
	this.urlTextLocalSms = urlTextLocalSms;
}

public String getApiKeyTextLocalSms() {
	return apiKeyTextLocalSms;
}

public void setApiKeyTextLocalSms(String apiKeyTextLocalSms) {
	this.apiKeyTextLocalSms = apiKeyTextLocalSms;
}

public String getObjectStoreUrl() {
	return objectStoreUrl;
}

public void setObjectStoreUrl(String objectStoreUrl) {
	this.objectStoreUrl = objectStoreUrl;
}

public String getcMApiUrl() {
	return cMApiUrl;
}

public void setcMApiUrl(String cMApiUrl) {
	this.cMApiUrl = cMApiUrl;
}

public String getcMProductToken() {
	return cMProductToken;
}

public void setcMProductToken(String cMProductToken) {
	this.cMProductToken = cMProductToken;
}

public String getCloudantDbKey() {
	return cloudantDbKey;
}

public void setCloudantDbKey(String cloudantDbKey) {
	this.cloudantDbKey = cloudantDbKey;
}

public String getCloudantDbUrl() {
	return cloudantDbUrl;
}

public void setCloudantDbUrl(String cloudantDbUrl) {
	this.cloudantDbUrl = cloudantDbUrl;
}

@Override
public void setEnvironment(Environment environment) {
this.cloudantDbUrl = environment.getProperty("environmentVars.cloudantDbUrl");	
this.cloudantDbKey = environment.getProperty("environmentVars.cloudantDbKey");
this.cMProductToken = environment.getProperty("environmentVars.cMProductToken");
this.cMApiUrl = environment.getProperty("environmentVars.cMApiUrl");
this.objectStoreUrl = environment.getProperty("environmentVars.objectStoreUrl");
this.apiKeyTextLocalSms = environment.getProperty("environmentVars.apiKeyTextLocalSms");
this.urlTextLocalSms = environment.getProperty("environmentVars.urlTextLocalSms");
this.alternateRouteDocType = environment.getProperty("environmentVars.alternateRouteDocType");
this.adminContactDocType = environment.getProperty("environmentVars.adminContactDocType");
this.contactTracedDocId = environment.getProperty("environmentVars.ContactTracedDocId");
this.sevireZoneDocId = environment.getProperty("environmentVars.SevireZoneDocId");
this.floorLayoutDocType = environment.getProperty("environmentVars.floorLayoutDocType");
this.gridViolationDocType = environment.getProperty("environmentVars.gridViolationDocType");
this.socialDistanceViolationId = environment.getProperty("environmentVars.socialDistanceViolationId");

}

}
