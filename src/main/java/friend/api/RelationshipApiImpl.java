package friend.api;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import friend.FriendMessage;
import friend.FriendRelationship;
import friend.Relationship;
import friend.dao.FriendMessageRepository;
import friend.dao.FriendRelationshipRepository;

public class RelationshipApiImpl implements RelationshipApi{
	
	@Autowired
	private FriendRelationshipRepository repository;
	
	@Autowired
	private FriendMessageRepository friendMessageRepository; 
	
	@Override
	public void create(String srcEmail, String desEmail) {
		int cntBlock1 = this.repository.countRelationship(srcEmail, desEmail, Relationship.BLOCK);
		int cntBlock2 = this.repository.countRelationship(desEmail, srcEmail, Relationship.BLOCK);
		
		if((cntBlock1 + cntBlock2) > 0){
			throw new RelationshipException("CREATE_RALTIONSHIP_BLOCK_ERROR", "Relationship between "+srcEmail+" and "+desEmail+" is BLOCKED");
		}
		
		int cntFriend1 = this.repository.countRelationship(srcEmail, desEmail, Relationship.FRIEND);
		if(cntFriend1==0){
			repository.save(new FriendRelationship(srcEmail, desEmail, Relationship.FRIEND));
		}
		
		int cntFriend2 = this.repository.countRelationship(desEmail, srcEmail, Relationship.FRIEND);
		if(cntFriend2==0){
			repository.save(new FriendRelationship(desEmail, srcEmail, Relationship.FRIEND));
		}
		
	}

	@Override
	public List<FriendRelationship> getFriends(String email) {
		List<FriendRelationship> relationshipList = repository.findFriends(email);
		return relationshipList;
	}

	@Override
	public List<String> getCommonFriends(String email1, String email2) {
		List<FriendRelationship> email1Friends = this.repository.findFriends(email1);
		List<FriendRelationship> email2Friends = this.repository.findFriends(email2);
		
		Set<String> relationshipSet = new HashSet<String>();
		for(FriendRelationship relationship : email1Friends){
			relationshipSet.add(relationship.getDesEmail());
		}
		
		Set<String> common = new HashSet<String>();
		for(FriendRelationship relationship : email2Friends){
			if(relationshipSet.contains(relationship.getDesEmail())){
				common.add(relationship.getDesEmail());
			}
		}
		
		return new ArrayList<String>(common);
	}

	@Override
	public FriendRelationship subscribing(String source, String target) {
		int cnt = this.repository.countRelationship(source, target, Relationship.SUBSCRIBING);
		if(cnt>0){
			throw new RelationshipException("SUBSCRIBE_RALTIONSHIP_EXISTING_ERROR", source + " is subscribing " + target + " already.");
		}
		
		FriendRelationship entity = new FriendRelationship(source, target, Relationship.SUBSCRIBING);
		FriendRelationship newEntity = repository.save(entity);
		
		return newEntity;
		
	}

	@Override
	public void block(String source, String target)  {
		FriendRelationship relationship = this.repository.getRelationship(source, target);
		if(relationship==null){
			this.repository.save(new FriendRelationship(source, target, Relationship.BLOCK));
		}else{
			relationship.setRelationship(Relationship.BLOCK);
			this.repository.save(relationship);
		}
		
	}

	@Override
	public List<String> getUpdateList(String sender, String msg) {
		Pattern msgPattern = Pattern.compile("(.*)(\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\\b)", Pattern.CASE_INSENSITIVE);
		Matcher m = msgPattern.matcher(msg);
		
		Set<String> recipients = new HashSet<String>();
		String content = "";
		if(m.find()){
			String mentionedEmail = m.group(2);
			recipients.add(mentionedEmail);
			content=m.group(1);
		}
		
		List<FriendRelationship> friends = this.repository.findFriends(sender);
		List<FriendRelationship> subscribe = this.repository.findSubscribes(sender);
		
		
		for(FriendRelationship relationship : friends){
			recipients.add(relationship.getDesEmail());
		}
		
		for(FriendRelationship relationship : subscribe){
			recipients.add(relationship.getSrcEmail());
		}
		
		List<FriendMessage> msgList = new ArrayList<FriendMessage>();
		for(String recipient : recipients){
			FriendMessage friendMsg = new FriendMessage();
			friendMsg.setRecipient(recipient);
			friendMsg.setMsg(content);
			friendMsg.setSender(sender);
			msgList.add(friendMsg);
		}
		
		friendMessageRepository.save(msgList);
		
		return new ArrayList<String>(recipients);
	}

}
