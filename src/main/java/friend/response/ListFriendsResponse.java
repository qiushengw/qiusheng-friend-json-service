package friend.response;

import java.util.ArrayList;
import java.util.List;

public class ListFriendsResponse extends CommonResponse{
	
	private List<String> friends;

	public List<String> getFriends() {
		if(friends==null) friends = new ArrayList<String>();
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public int getCount() {
		return getFriends().size();
	}

	
}
