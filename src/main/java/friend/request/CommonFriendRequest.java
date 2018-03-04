package friend.request;

import java.util.List;

public class CommonFriendRequest extends CommonRequest {
	
	private List<String> friends;
	
	public CommonFriendRequest(){
		
	}
	
	public CommonFriendRequest(List<String> friends){
		this.friends = friends;
	}


	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

}
