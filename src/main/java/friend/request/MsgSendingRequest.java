package friend.request;

public class MsgSendingRequest extends CommonRequest {
	private String sender;
	private String text;
	
	public MsgSendingRequest(){
		
	}
	
	public MsgSendingRequest(String sender, String text){
		this.sender = sender;
		this.text = text;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	

}
