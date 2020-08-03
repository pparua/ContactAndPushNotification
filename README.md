# ContactAndPushNotification
Middle layer spring boot service to provide service end points to interact with database(IBM Cloud Cloudant DB), plot the floor lay out image, and send notification.

This is deployed in IBM Cloud Foundry server. 

Below are the summary of the endpoints:
1. /NotifyToAdminContact: 
This service end point is used to send the notification to admin poc afterdetecting the breach, for a respective location. This service find the admin contact for a location and then notified with the message type selected and the vendor type preferred. Notification of type mentioned will be send by respective vendor mentioned in input parameter. To send notification need to set it 'Y'.

2. /TraceVulnarableCarrier:
	 This service end point identify the presence of vulnerable carrier in a location and send the notification to admin poc after detecting such person in that location. 
   1.Get contact numbers traced in a location, from CotactTraced DB store, which will be populated by ETL job. Based on assumption that ETL job will capture this information from different sources from government and vendors, as per future agreement. 
	 2. Get risk zone having severe affect of Covid 19, from Sevirezone DB, graded between 1 to 5 based on severity. This table also will be populated by ETL job, which will analyze the data from different sources li Kaggle etc. 
	 3.Check any such contact identified, who visited recently to severity zones. This service find the admin contact for a location and then notified with the message type selected and the vendor type preferred. Notification of type mentioned will be send by respective vendor mentioned in input parameter. To send notification need to set it 'Y'.
   
3. /NotifyForSafeRoute:
  This service end point is used to send the notification SMS to the given contact number, who is getting into the severity zone or containment zone. This service find all the possible routes around this containment zone, so that the individual can decide its safe route for destination. Notification of type mentioned will be send by respective vendor. To send notification need to set it 'Y'.
  
4. /GetPlottedLayout:
This service end point is used to plot the floor layout of a particular location with the breached incidents for a date. This service is being called from the UI application.

5./getPlottedLayoutSnippets:

This service end point pick up the name of those breach screen shot files from DB, for a particular location and date, and return as list of string. This service is being called from the UI application.

Need to put the applicable properties file entries in the application.properties file:
environmentVars.cloudantDbUrl: <<CLOUDANT_DB_ENDPOINTURL>>
environmentVars.cloudantDbKey: <<CLOUDANT_DB_KEY>>

environmentVars.cMProductToken:<<CM_NETWORK_PRODUCT_TOKEN>>
environmentVars.cMApiUrl:https://gw-hk.cmtelecom.com/v1.0/message

environmentVars.objectStoreUrl:<<IBM_CLOUD_OBJECT_STORE_URL>>

environmentVars.apiKeyTextLocalSms:<<KEY_FOR_TEXT_LOCAL>>
environmentVars.urlTextLocalSms:https://api.textlocal.in/send/?

environmentVars.alternateRouteDocType:SuggestSafeRoute
environmentVars.adminContactDocType:AdminContact
environmentVars.floorLayoutDocType:Floor Layout
environmentVars.gridViolationDocType:Grid Violations

environmentVars.ContactTracedDocId:300
environmentVars.SevireZoneDocId:200

environmentVars.socialDistanceViolationId:11

