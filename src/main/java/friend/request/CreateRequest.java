package friend.request;

import java.util.List;

public class CreateRequest extends CommonRequest {
	
	private List<String> friends;
	
	public CreateRequest(){
		
	}
	
	public CreateRequest(List<String> friends){
		this.friends = friends;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

}
