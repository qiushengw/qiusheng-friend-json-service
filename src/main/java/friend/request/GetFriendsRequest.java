package friend.request;

public class GetFriendsRequest extends CommonRequest {
	private String email;
	
	public GetFriendsRequest(){
		
	}
	
	public GetFriendsRequest(String email){
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
