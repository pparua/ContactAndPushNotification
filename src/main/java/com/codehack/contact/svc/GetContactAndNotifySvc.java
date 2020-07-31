package com.codehack.contact.svc;

import static com.cloudant.client.api.query.Expression.eq;
import static com.cloudant.client.api.query.Expression.gt;
import static com.cloudant.client.api.query.Expression.regex;
import static com.cloudant.client.api.query.Operation.and;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Document;
import com.cloudant.client.api.query.QueryBuilder;
import com.cloudant.client.api.query.QueryResult;
import com.codehack.contact.vo.AdminContact;
import com.codehack.contact.vo.AdminContactData;
import com.codehack.contact.vo.ContactTraced;
import com.codehack.contact.vo.Coordinates;
import com.codehack.contact.vo.FloorLayout;
import com.codehack.contact.vo.GridViolations;
import com.codehack.contact.vo.Route;
import com.codehack.contact.vo.SafeRoute;
import com.codehack.contact.vo.SevireZone;
import com.codehack.contact.vo.TracedData;
import com.codehack.contact.vo.Zone;
/**
 * Service end points to interact with database, plot the floor lay out image, and send notification.
 * 
 * @author PRASENJIT
 *
 */
@RestController
public class GetContactAndNotifySvc {
	@Autowired
	public Config config;

	/**
	 * This service end point is used to send the notification to admin poc after
	 * detecting the breach, for a respective location. This service find the admin
	 * contact for a location and then notified with the message type selected and
	 * the vendor type preferred. Notification of type mentioned will be send by
	 * respective vendor mentioned in input parameter. To send notification need to
	 * set it 'Y'.
	 * 
	 * @param locationCd
	 * @param vendor
	 * @param notificationType
	 * @param messageToSend
	 * @param sendFlag
	 * @return
	 */
	@GetMapping("/NotifyToAdminContact")
	public String notifyToAdminContact(@RequestParam(value = "locationCd", defaultValue = "") String locationCd,
			@RequestParam(value = "vendor", defaultValue = "TextLocal") String vendor,
			@RequestParam(value = "notificationType", defaultValue = "SMS") String notificationType,
			@RequestParam(value = "messageToSend", defaultValue = "") String messageToSend,
			@RequestParam(value = "sendFlag", defaultValue = "N") String sendFlag) {
		System.out.println("Hi..notifyToAdminContact is up!!" + locationCd + " " + vendor + " " + notificationType);
		String sendStatus = "";
		String contactNb = getAdminContact(locationCd);
		if ("Y".equals(sendFlag)) {
			try {
				sendNotification(contactNb, vendor, notificationType, messageToSend);
			} catch (Exception e) {
				sendStatus = "Failed to send notifiction..";
				e.printStackTrace();
			}
		}
		sendStatus = "Notifiction send Successfull to:" + contactNb;
		return sendStatus;
	}

	private String getAdminContact(String locationCd) {
		Database db = getDb();
		String contactNb = "";
		QueryResult<AdminContactData> adminContactData = db.query(
				new QueryBuilder(and(gt("_id", locationCd), eq("type", config.getAdminContactDocType()))).build(),
				AdminContactData.class);
		for (AdminContactData acd : adminContactData.getDocs()) {
			for (AdminContact ac : acd.getAdminContact()) {
				if (locationCd.equals(ac.getLocationId())) {
					contactNb = ac.getContactNb();
				}
			}
		}
		return contactNb;
	}

	/**
	 * This service end point identify the presence of vulnerable carrier in a
	 * location and send the notification to admin poc after detecting such person
	 * in that location. 
	 * 1.Get contact numbers traced in a location, from
	 * CotactTraced DB store, which will be populated by ETL job. Based on
	 * assumption that ETL job will capture this information from different sources
	 * from government and vendors, as per future agreement. 
	 * 2. Get risk zones
	 * having severe affect of Covid 19, from Sevirezone DB, graded between 1 to 5
	 * based on severity. This table also will be populated by ETL job, which will
	 * analyze the data from different sources li Kaggle etc. 
	 * 3.Check any such
	 * contact identified, who visited recently to severity zones. This service find
	 * the admin contact for a location and then notified with the message type
	 * selected and the vendor type preferred. Notification of type mentioned will
	 * be send by respective vendor mentioned in input parameter. To send
	 * notification need to set it 'Y'.
	 * 
	 * @param locationId
	 * @param vendor
	 * @param notificationType
	 * @param sendFlag
	 * @return
	 */
	@GetMapping("/TraceVulnarableCarrier")
	public List<String> traceVulnarableCarrier(@RequestParam(value = "locationId", defaultValue = "") String locationId,
			@RequestParam(value = "vendor", defaultValue = "TextLocal") String vendor,
			@RequestParam(value = "notificationType", defaultValue = "SMS") String notificationType,
			@RequestParam(value = "sendFlag", defaultValue = "N") String sendFlag) {
		System.out.println("Hi..traceVulnarableCarrier is up!!" + locationId);
		List<String> identifiedContacts = new ArrayList<String>();
		List<TracedData> allTracedData = new ArrayList<TracedData>();
		Map<String, Zone> allSevireZoneData = new HashMap<String, Zone>();
		// Get contact numbers traced in a location, from CotactTraced DB store, which
		// will be populated by ETL job.
		// Based on assumption that ETL job will capture this information from different
		// sources from government and vendors, as per
		// future agreement.
		Database db = getDb();
		String contactTracedDocId = config.getContactTracedDocId();
		QueryResult<ContactTraced> contactTracedData = db
				.query(new QueryBuilder(eq("_id", "300")).build(), ContactTraced.class);
//		QueryResult<ContactTraced> contactTracedData = db
//				.query(new QueryBuilder(eq("_id", "300")).build(), ContactTraced.class);
		for (ContactTraced ctd : contactTracedData.getDocs()) {
			allTracedData.addAll(ctd.getTracedData());
		}

		// Get risk zones having severe affect of Covid 19, from Sevirezone DB, graded
		// between 1 to 5 based on severity. This table
		// also will be populated by ETL job, which will analyze the data from different
		// sources li Kaggle etc.
//		QueryResult<SevireZone> sevireZoneData = db
//				.query(new QueryBuilder(and(eq("_id", config.getSevireZoneDocId()))).build(), SevireZone.class);
		
		QueryResult<SevireZone> sevireZoneData = db
				.query(new QueryBuilder(eq("_id", "200")).build(), SevireZone.class);
		for (SevireZone sz : sevireZoneData.getDocs()) {
			for (Zone zone : sz.getSevirezoneData()) {
				allSevireZoneData.put(String.valueOf(zone.getSid()), zone);
			}
		}
		// Check any such contact identified, who visited recently to severity zones.
		for (TracedData td : allTracedData) {
			if (allSevireZoneData.containsKey(td.getContactId())) {
				identifiedContacts.add(String.valueOf(td.getContactId()));
			}
		}

		// Alert to security personnel.
		String messageToSend = "Identified risky contacts in your location:";
		for (String contact : identifiedContacts) {
			messageToSend = messageToSend + contact + ":";
		}
		// TODO - open to send sms
		String sendStatus = "";
		String contactNb = getAdminContact(locationId);
		if ("Y".equals(sendFlag)) {
			try {
				sendNotification(contactNb, vendor, notificationType, messageToSend);
			} catch (Exception e) {
				sendStatus = "Failed to send notification..";
				e.printStackTrace();
			}
		}
		sendStatus = "Notifiction send Successfully.contactNb:" + contactNb + " Message:" + messageToSend;
		System.out.println("sendStatus::" + sendStatus);

		// Return List of such contacts.
		return identifiedContacts;
	}

	/**
	 * This service end point is used to send the notification SMS to the given
	 * contact number, who is getting into the severity zone or containment zone.
	 * This service find all the possible routes around this containment zone, so
	 * that the individual can decide its safe route for destination. Notification
	 * of type mentioned will be send by respective vendor. To send notification
	 * need to set it 'Y'.
	 * 
	 * @param contactNb
	 * @param vendor
	 * @param notificationType
	 * @param sevearityZoneId
	 * @param sendFlag
	 * @return
	 */
	@GetMapping("/NotifyForSafeRoute")
	public String notifyForSafeRoute(@RequestParam(value = "contact", defaultValue = "") String contactNb,
			@RequestParam(value = "vendor", defaultValue = "TextLocal") String vendor,
			@RequestParam(value = "notificationType", defaultValue = "SMS") String notificationType,
			@RequestParam(value = "sevearityZoneId", defaultValue = "") String sevearityZoneId,
			@RequestParam(value = "sendFlag", defaultValue = "N") String sendFlag) {
		System.out.println("Hi..notifyForSafeRoute is up!!" + contactNb + " " + vendor + " " + notificationType);
		String sendStatus = "";
		String messageToSend = getAlternetRouteFromDb(sevearityZoneId);
		if ("Y".equals(sendFlag)) {
			try {
				sendNotification(contactNb, vendor, notificationType, messageToSend);
			} catch (Exception e) {
				sendStatus = "Failed to send notifiction..";
				e.printStackTrace();
			}
		}
		sendStatus = "Notifiction send Successfully.sevearityZoneId:" + sevearityZoneId + " Message:" + messageToSend;
		System.out.println("sendStatus::" + sendStatus);
		return sendStatus;
	}

	private String getAlternetRouteFromDb(String sevearityZoneId) {
		Database db = getDb();
		String suggestedRoute = "";
		QueryResult<SafeRoute> safeRoute = db.query(
				new QueryBuilder(and(gt("_id", "1"), eq("type", config.getAlternateRouteDocType()))).build(),
				SafeRoute.class);
		for (SafeRoute sf : safeRoute.getDocs()) {
			for (Route rt : sf.getSafeRouteList()) {
				if (sevearityZoneId.equals(rt.getSid())) {
					suggestedRoute = rt.getAletrnateRoute();
				}
			}
		}
		return suggestedRoute;
	}

	private void sendNotification(String contactNb, String vendor, String notificationType, String messageToSend)
			throws Exception {
		if ("TextLocal".equals(vendor) && "SMS".equals(notificationType)) {
			sendSMSUsingTextLocal(contactNb, vendor, notificationType, messageToSend);
		} else {
			System.out.println("Not supported now");
		}
	}
/**
 * This service end point is used to plot the floor layout of a particular location with the breached incident of a date.
 * 
 * @param location
 * @param forDate
 * @return
 * @throws IOException
 */
	@GetMapping(value = "/GetPlottedLayout", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getPlottedLayout(@RequestParam(value = "location", defaultValue = "") String location,
			@RequestParam(value = "forDate") String forDate) throws IOException {
		byte[] in = markViolationLayoutWithDot(location, forDate);
		return in;
	}

	@SuppressWarnings({ "deprecation", "deprecation" })
	private byte[] markViolationLayoutWithDot(String location, String forDate) throws IOException {
		BufferedImage plottedLayout = null;

		// 1. Get image
		List<Document> allDocs = null;

		String s = "";
		InputStream isLayOutImage = null;
		BufferedImage layOutImage = null;
		byte[] imageInByte = null;
		try {
			Database db = getDb();

			QueryResult<FloorLayout> layOuts = db.query(new QueryBuilder(and(gt("_id", "1"),
					eq("type", config.getFloorLayoutDocType()), eq("locId", Integer.parseInt(location)))).build(),
					FloorLayout.class);
			FloorLayout floor = (FloorLayout) layOuts.getDocs().get(0);

			isLayOutImage = db.getAttachment(floor.get_id(), floor.getImgName());

			layOutImage = ImageIO.read(isLayOutImage);

			// Picked up violations

			QueryResult<GridViolations> violations = db.query(
					new QueryBuilder(and(gt("_id", "1"), eq("type", config.getGridViolationDocType()),
							eq("locId", Integer.parseInt(location)), regex("strTime", forDate))).build(),
					GridViolations.class);
			Rectangle myOffice = null;
			int startX = 0;
			int startY = 0;
			int endX = 0;
			int endY = 0;
			Graphics2D g2d = null;
			g2d = layOutImage.createGraphics();
			for (GridViolations mgv : violations.getDocs()) {
				// 2. plot image
				for (Coordinates cord : mgv.getvCoord()) {
					startX = ((cord.getStartX() + cord.getEndX()) / 2) - 5;
					startY = ((cord.getStartY() + cord.getEndY()) / 2) - 5;
					endX = ((cord.getStartX() + cord.getEndX()) / 2) + 5;
					endY = ((cord.getStartY() + cord.getEndY()) / 2) + 5;
					String vId = String.valueOf(mgv.getvId());

					if (vId.contains(config.getSocialDistanceViolationId())) {
						myOffice = new Rectangle(cord.getEndX(), cord.getEndY(), 10, 10);
					} else {
						// myOffice = new Rectangle(startX, startY, 10, 10);
						// TODO - enrich logic for better result
						myOffice = new Rectangle(startX, startY + 600, 10, 10);
					}
					if (mgv.getvDesc().equals("Social Distancing Violations")) {
						g2d.setColor(Color.RED);
					} else {
						g2d.setColor(Color.RED);
					}
					g2d.draw(myOffice);
				}
			}
			// 3. Return image
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(layOutImage, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
		} catch (Exception e) {
			System.out.println("Exception thrown : " + e.getMessage());
		} finally {
			isLayOutImage.close();
		}
		return imageInByte;
	}
/**
 *This service end point pick up the name of those breach screen shot files from DB, for a particular location and date, and return as list of string.
 * 
 * @param location
 * @param forDate
 * @return
 * @throws IOException
 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/getPlottedLayoutSnippets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getPlottedLayoutSnippets(@RequestParam(value = "location", defaultValue = "") String location,
			@RequestParam(value = "forDate") String forDate) throws IOException {
		List<String> la = getPlottedLayoutSnippetsNames(location, forDate);

		return la;
	}

	@SuppressWarnings({ "deprecation", "deprecation" })
	private List<String> getPlottedLayoutSnippetsNames(String location, String forDate) throws IOException {

		List<String> layoutWithSnippetName = new ArrayList<String>();

		// 1. Get image
		List<Document> allDocs = null;

		try {
			Database db = getDb();

			// Picked up violations
			String type = "Grid Violations";

			QueryResult<GridViolations> violations = db.query(new QueryBuilder(and(gt("_id", "1"),
					eq("type", "Grid Violations"), eq("locId", Integer.parseInt(location)), regex("strTime", forDate)// 07-26-2020
			)).build(), GridViolations.class);

			for (GridViolations mgv : violations.getDocs()) {
				layoutWithSnippetName.add(config.getObjectStoreUrl() + mgv.getImgName());
			}
		} catch (Exception e) {
			System.out.println("Exception thrown : " + e.getMessage());
		} finally {
		}
		return layoutWithSnippetName;
	}

	private Database getDb() {
		CloudantClient cloudantClient = null;

		URL vcapUrl = null;
		try {
			vcapUrl = new URL(config.getCloudantDbUrl());
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		ClientBuilder clientBuilder = ClientBuilder.url(vcapUrl);
		clientBuilder = clientBuilder.iamApiKey(config.getCloudantDbKey());
		cloudantClient = clientBuilder.build();
		Database db = cloudantClient.database("dbppmmgriddata", false);
		return db;
	}

	private String sendSMSUsingTextLocal(String contactNb, String vendor, String notificationType, String messageToSend)
			throws Exception {
		try {
			// Construct data
			String apiKey = "apikey=" + config.getApiKeyTextLocalSms();
			String message = "&message=" + messageToSend;
			String sender = "&sender=" + "TXTLCL";
			String numbers = "&numbers=" + contactNb;

			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL(config.getUrlTextLocalSms()).openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();

			return stringBuffer.toString();
		} catch (Exception e) {
			System.out.println("Error SMS " + e.getMessage());
			throw e;
		}

	}

}