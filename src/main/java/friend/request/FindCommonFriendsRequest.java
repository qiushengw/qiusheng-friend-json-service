package friend.request;

import java.util.List;

public class FindCommonFriendsRequest extends CommonRequest {
	
	private List<String> friends;

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

}
