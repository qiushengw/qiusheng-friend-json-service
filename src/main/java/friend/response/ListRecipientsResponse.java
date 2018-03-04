package friend.response;

import java.util.ArrayList;
import java.util.List;

public class ListRecipientsResponse extends CommonResponse{
	
	private List<String> recipients;
	
	public ListRecipientsResponse(){
		
	}
	
	public ListRecipientsResponse(List<String> recipients){
		this.recipients = recipients;
	}

	public List<String> getRecipients() {
		if(recipients==null) recipients = new ArrayList<String>();
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}
	
}
