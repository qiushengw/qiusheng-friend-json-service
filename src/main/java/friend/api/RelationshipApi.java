package friend.api;

import java.util.List;

import friend.FriendRelationship;
public interface RelationshipApi {
	
	public void create(String email1, String email2);
	
	public List<FriendRelationship> getFriends(String email);
	
	public List<String> getCommonFriends(String email1, String email2);
	
	public FriendRelationship subscribing(String source, String target);

	public void block(String source, String target);
	
	public List<String> getUpdateList(String sender, String msg);
	
}
