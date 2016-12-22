package appointmentclaim;

import javax.servlet.annotation.WebServlet;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

/**
 * Servlet implementation class HistoryBuffServlet
 */
@WebServlet("/appointmentclaim")
public class AppointmentClaimServlet extends SpeechletServlet  {
	private static final long serialVersionUID = 1L;
	
	
	public AppointmentClaimServlet(){
		this.setSpeechlet(new AppointmentClaimSpeechlet());
	}
       
   
}
