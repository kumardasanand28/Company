package appointmentclaim;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SsmlOutputSpeech;

import appointmentclaim.dao.AppointmentClaimDAO;
import appointmentclaim.util.SendNotification;

public class AppointmentClaimSpeechlet implements Speechlet {

	private static final Logger log = LoggerFactory.getLogger(AppointmentClaimSpeechlet.class);


	private AppointmentClaimDAO appDao;

	private SendNotification notify;
	/**
	 * Constant defining session attribute key for the event index.
	 */
	private static final String SESSION_APPOINTMENTS = "appointments";

	/**
	 * Constant defining session attribute key for the event text key for date of events.
	 */
	private static final String SESSION_TEXT = "text";



	private static final String NOTIFICATION_CONTENT = "notify";


	/**
	 * A Mapping of alternative ways a user will say a category to how Amazon has defined the
	 * category. Use a tree map so gets can be case insensitive.
	 */
	private static final Map<String, String> spokenNameToCategory = new TreeMap<String, String>(
			String.CASE_INSENSITIVE_ORDER);

	static {
		spokenNameToCategory.put("home", "home");
		spokenNameToCategory.put("auto", "auto");
		spokenNameToCategory.put("car", "auto");
		spokenNameToCategory.put("house", "home");
		spokenNameToCategory.put("shop", "shop");
		spokenNameToCategory.put("bike", "auto");
		spokenNameToCategory.put("vehicle", "auto");
		spokenNameToCategory.put("motorcycle", "auto");
		spokenNameToCategory.put("scooter", "auto");
		spokenNameToCategory.put("restaurant", "shop");
		spokenNameToCategory.put("yes", "yes");
		spokenNameToCategory.put("yes please", "yes");
		spokenNameToCategory.put("yep", "yes");
		spokenNameToCategory.put("yup", "yes");
		spokenNameToCategory.put("sure", "yes");
		spokenNameToCategory.put("affirmative", "yes");
		spokenNameToCategory.put("yeah", "yes");
	}


	@Override
	public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
		// TODO Auto-generated method stub

	}

	@Override
	public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
		log.info("AppointmentClaimSpeechlet :: onLaunch requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());
		return getWelcomeResponse(session);
	}

	@Override
	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
		log.info("AppointmentClaimSpeechlet :: onIntent requestId={}, sessionId={}", request.getRequestId(),
				session.getSessionId());

		Intent intent = request.getIntent();
		String intentName = intent.getName();


		if("GetSpecificAppointmentDetailIntent".equalsIgnoreCase(intentName)){
			return handleSpecifcAppointMentDetails(intent,session);
		}else if("AppointmentDetailsIntent".equalsIgnoreCase(intentName)){
			return handleAppointMentDetails(intent,session);
		}else if("SendNotificationEvent".equalsIgnoreCase(intentName)){
			return handleSendNotification(intent,session);
		}
		else if ("AMAZON.StopIntent".equals(intentName)){
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("Goodbye");
			return SpeechletResponse.newTellResponse(outputSpeech);

		}else if ("AMAZON.CancelIntent".equals(intentName)) {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("Goodbye");
			return SpeechletResponse.newTellResponse(outputSpeech);

		}

		return null;
	}

	private SpeechletResponse handleAppointMentDetails(Intent intent, Session session) {
		Slot categorySlot = intent.getSlot("appointmenttype");
		String type = getLookupWord(categorySlot);
		appDao = new AppointmentClaimDAO();
		System.out.println("Type -----"+type);
		Map<String,String> responseTextMap = appDao.fetchAppointMentResponseOnType(type);
		if(!responseTextMap.isEmpty() && responseTextMap.size() ==1){
			StringBuilder speechOutputBuilder = new StringBuilder();
			StringBuilder notificationBuilder = new StringBuilder();
			speechOutputBuilder.append("Thanks for asking us the information!!");
			for (Map.Entry<String, String> pair : responseTextMap.entrySet()) {
				speechOutputBuilder.append(" The status of the appointment is "+pair.getKey()+" ");
				speechOutputBuilder.append(pair.getValue());
				speechOutputBuilder.append(", ");
				notificationBuilder.append(" The status of the appointment is "+pair.getKey()+" "+pair.getValue()+". ");
				speechOutputBuilder.append("Do you need this appointment as email and SMS? Yes Or No");
			}
			String repromptText =
					"With This Appointment application, we will know the appointments scheduled for your claims";
			session.setAttribute(NOTIFICATION_CONTENT, notificationBuilder.toString());

			return newAskResponse("<speak>"+speechOutputBuilder.toString() +"</speak>", true, repromptText, false);
		}else{
			return getWelcomeResponse(session);
		}
	}

	private String getLookupWord(Slot categorySlot) {
		String lookupCategory = null;
		if (categorySlot != null && categorySlot.getValue() != null) {
			String category =
					categorySlot.getValue().toLowerCase().replaceAll("\\s", "").replaceAll("\\.", "");
			lookupCategory = spokenNameToCategory.get(category);

		}
		return lookupCategory;
	}

	private SpeechletResponse handleSendNotification(Intent intent, Session session) {
		Slot categorySlot = intent.getSlot("notify");
		String response = getLookupWord(categorySlot);
		if(response != null && response.equalsIgnoreCase("yes") && session.getAttribute(NOTIFICATION_CONTENT) != null){
			String notificationText = (String) session.getAttribute(NOTIFICATION_CONTENT);
			notify = new SendNotification();
			notify.sendSMS(notificationText);
			notify.sendEmail(notificationText);
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("We have send an email and SMS. Thanks for using the applciation. Goodbye");
			return SpeechletResponse.newTellResponse(outputSpeech);
		}else{
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText("Can you please nake it clear? Not able to get the expected information. Thanks for the co-operation");

			return SpeechletResponse.newTellResponse(outputSpeech);
		}

	}

	private SpeechletResponse handleSpecifcAppointMentDetails(Intent intent, 
			Session session) {

		Slot categorySlot = intent.getSlot("appointmentindex");
		Map<String,String> indexAppMap = (Map<String, String>) session.getAttribute(SESSION_APPOINTMENTS);
		if(indexAppMap!= null && indexAppMap.containsKey(categorySlot.getValue())){
			appDao = new AppointmentClaimDAO();
			Map<String,String> responseTextMap = appDao.fetchAppointMentResponse(indexAppMap.get(categorySlot.getValue()));
			StringBuilder speechOutputBuilder = new StringBuilder();
			StringBuilder notificationBuilder = new StringBuilder();
			speechOutputBuilder.append("Thanks for asking us the information!!");
			for (Map.Entry<String, String> pair : responseTextMap.entrySet()) {


				speechOutputBuilder.append("The status of the appointment is "+pair.getKey()+" ");
				speechOutputBuilder.append(pair.getValue());
				speechOutputBuilder.append(". ");
				notificationBuilder.append(" The status of the appointment is "+pair.getKey()+" "+pair.getValue()+". ");
				speechOutputBuilder.append("Do you need this appointment as email and SMS? Yes Or No");
			}
			String repromptText =
					"With This Appointment application, we will know the appointments scheduled for your claims";
			session.setAttribute(NOTIFICATION_CONTENT, notificationBuilder.toString());
			return newAskResponse("<speak>"+speechOutputBuilder.toString() +"</speak>", true, repromptText, false);

		}else{
			return getWelcomeResponse(session);
		}
	}

	@Override
	public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
		// TODO Auto-generated method stub

	}


	private SpeechletResponse getWelcomeResponse(Session session) {
		StringBuilder speechOutputBuilder = new StringBuilder();
		speechOutputBuilder.append("Welcome to Appointment Application!!!!!.");
		speechOutputBuilder.append("Do you need to get the appointment details of the claims?");
		appDao = new AppointmentClaimDAO();
		Map<String,String> appointClaimMap = appDao.fetchAllAppointMents();
		Map<String,String> indexAppMap = new HashMap<String,String>();
		if(appointClaimMap.size() >=2){
			speechOutputBuilder.append(" You have multiple claim appointments scheduled. ");
		}
		speechOutputBuilder.append("You have  " + "<say-as interpret-as=\"digits\">" + appointClaimMap.size() + "</say-as> appointments scheduled. ");
		int i=1;
		Iterator it = appointClaimMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			indexAppMap.put(Integer.toString(i), (String) pair.getKey());
			speechOutputBuilder.append(i);
			speechOutputBuilder.append(". ");
			i++;
			speechOutputBuilder.append("Claim Number is ");
			speechOutputBuilder.append(pair.getKey());
			speechOutputBuilder.append(",");
			speechOutputBuilder.append("<break time=\"0.2s\" />");
			speechOutputBuilder.append(" for the Claim Type of ");
			speechOutputBuilder.append(pair.getValue());
			speechOutputBuilder.append(". ");
			speechOutputBuilder.append("<break time=\"0.2s\" />");
		}
		speechOutputBuilder.append("Please tell us the item number for getting the appointment details");
		session.setAttribute(SESSION_APPOINTMENTS, indexAppMap);
		String repromptText =
				"With This Appointment application, we will know the appointments scheduled for your claims";

		return newAskResponse("<speak>"+speechOutputBuilder.toString() +"</speak>", true, repromptText, false);
	}





	/**
	 * Wrapper for creating the Ask response from the input strings.
	 * 
	 * @param stringOutput
	 *            the output to be spoken
	 * @param isOutputSsml
	 *            whether the output text is of type SSML
	 * @param repromptText
	 *            the reprompt for if the user doesn't reply or is misunderstood.
	 * @param isRepromptSsml
	 *            whether the reprompt text is of type SSML
	 * @return SpeechletResponse the speechlet response
	 */
	private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml,
			String repromptText, boolean isRepromptSsml) {
		OutputSpeech outputSpeech, repromptOutputSpeech;
		if (isOutputSsml) {
			outputSpeech = new SsmlOutputSpeech();
			((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
		} else {
			outputSpeech = new PlainTextOutputSpeech();
			((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
		}

		if (isRepromptSsml) {
			repromptOutputSpeech = new SsmlOutputSpeech();
			((SsmlOutputSpeech) repromptOutputSpeech).setSsml(repromptText);
		} else {
			repromptOutputSpeech = new PlainTextOutputSpeech();
			((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
		}
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromptOutputSpeech);
		return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	}

}