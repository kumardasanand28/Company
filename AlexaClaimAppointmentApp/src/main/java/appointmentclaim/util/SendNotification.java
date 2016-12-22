package appointmentclaim.util;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendNotification {


	static final String FROM = "kumardasanand28@gmail.com";  // Replace with your "From" address. This address must be verified.
	static final String TO = "anandkdas28@gmail.com"; // Replace with a "To" address. If your account is still in the
	// sandbox, this address must be verified.

	// Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
	static final String SMTP_USERNAME = "AKIAJRH5GPFYL76I6OKQ";  // Replace with your SMTP username.
	static final String SMTP_PASSWORD = "AsrCmGnVqWob0IBKAlZ4G1xUrNu92lNhWi3IXRHy0tMJ";  // Replace with your SMTP password.



	// Amazon SES SMTP host name. This example uses the US West (Oregon) region.
	static final String HOST = "email-smtp.us-west-2.amazonaws.com";    

	// The port you will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
	// STARTTLS to encrypt the connection.
	static final int PORT = 25;

	public static final String ACCOUNT_SID = "ACb303deb7acf88876af9d28cb6f5e16d2";
	public static final String AUTH_TOKEN = "81712db7a23fb7596b15d3cf3afd221d";	
	//+14253411621

	public void sendSMS(String text){
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message
				.creator(new PhoneNumber("+919495785149"),  // to
						new PhoneNumber("+14253411621"),  // from
						text)
				.create();
		System.out.println(message.getSid());
	}



	public void sendEmail(String text){

		// Create a Properties object to contain connection configuration information.
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtp.port", PORT); 


		// Set properties indicating that we want to use STARTTLS to encrypt the connection.
		// The SMTP session will begin on an unencrypted connection, and then the client
		// will issue a STARTTLS command to upgrade to an encrypted connection.
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		// Create a Session object to represent a mail session with the specified properties. 
		Session session = Session.getDefaultInstance(props);
		Transport transport = null;
		try
		{  	
			// Create a message with the specified information. 
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(FROM));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
			msg.setSubject("Appointment Confirmation");
			msg.setContent(text,"text/plain");

			Multipart multipart = new MimeMultipart("alternative");
			BodyPart calenderPart = buildCalendarPart();
			multipart.addBodyPart(calenderPart);
			msg.setContent(multipart);


			// Create a transport.        
			transport = session.getTransport();

			// Send the message.

			System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");

			// Connect to Amazon SES using the SMTP username and password you specified above.
			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

			// Send the email.
			transport.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Email sent!");
		}
		catch (Exception ex) {
			System.out.println("The email was not sent.");
			System.out.println("Error message: " + ex.getMessage());
		}
		finally
		{
			// Close and terminate the connection.
			try {
				transport.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        	
		}






	}


	//define somewhere the icalendar date format
	private static SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");

	private BodyPart buildCalendarPart() throws Exception {

		BodyPart calendarPart = new MimeBodyPart();

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		Date start = cal.getTime();
		cal.add(Calendar.HOUR_OF_DAY, 3);
		Date end = cal.getTime();

		//check the icalendar spec in order to build a more complicated meeting request
		String calendarContent =
				"BEGIN:VCALENDAR\n" +
						"METHOD:REQUEST\n" +
						"PRODID: BCP - Meeting\n" +
						"VERSION:2.0\n" +
						"BEGIN:VEVENT\n" +
						"DTSTAMP:" + iCalendarDateFormat.format(start) + "\n" +
						"DTSTART:" + iCalendarDateFormat.format(start)+ "\n" +
						"DTEND:"  + iCalendarDateFormat.format(end)+ "\n" +
						"SUMMARY:Appointment for your claim verification\n" +
						"UID:324\n" +
						"ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:organizer@yahoo.com\n" +
						"ORGANIZER:MAILTO:info@mycompany.com\n" +
						"LOCATION:At your Home\n" +
						"DESCRIPTION:learn some stuff\n" +
						"SEQUENCE:0\n" +
						"PRIORITY:5\n" +
						"CLASS:PUBLIC\n" +
						"STATUS:CONFIRMED\n" +
						"TRANSP:OPAQUE\n" +
						"BEGIN:VALARM\n" +
						"ACTION:DISPLAY\n" +
						"DESCRIPTION:REMINDER\n" +
						"TRIGGER;RELATED=START:-PT00H15M00S\n" +
						"END:VALARM\n" +
						"END:VEVENT\n" +
						"END:VCALENDAR";

		calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
		calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");

		return calendarPart;
	}



}
