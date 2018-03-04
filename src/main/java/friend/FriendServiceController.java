package friend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import friend.api.RelationshipApi;
import friend.request.BlockFriendsRequest;
import friend.request.CommonFriendRequest;
import friend.request.CreateRequest;
import friend.request.GetFriendsRequest;
import friend.request.MsgSendingRequest;
import friend.request.SubscribeFriendsRequest;
import friend.response.CommonResponse;
import friend.response.ListFriendsResponse;
import friend.response.ListRecipientsResponse;

@Controller
@RequestMapping("/friend")
public class FriendServiceController {

    @Autowired
    private RelationshipApi api;
    
    @RequestMapping(method=RequestMethod.POST, value="/create")
    public @ResponseBody CommonResponse create(@RequestBody CreateRequest payload) {
    	List<String> emailList = payload.getFriends();
    	api.create(emailList.get(0), emailList.get(1));
    	return new CommonResponse();
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/getFriends")
    public @ResponseBody CommonResponse getFriends(@RequestBody GetFriendsRequest payload) {
    	List<FriendRelationship> relationshipList = api.getFriends(payload.getEmail());
    	ListFriendsResponse response = new ListFriendsResponse();
		
		Set<String> relationshipSet = new HashSet<String>();
		for(FriendRelationship relationship : relationshipList){
			relationshipSet.add(relationship.getDesEmail());
			
		}
		relationshipSet.remove(payload.getEmail());
		
		response.setFriends(new ArrayList<String>(relationshipSet));
		
    	return response;
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/getCommonFriends")
    public @ResponseBody CommonResponse getCommonFriends(@RequestBody CommonFriendRequest payload) {
    	List<String> emailList = payload.getFriends();
    	List<String> commonFriends = api.getCommonFriends(emailList.get(0), emailList.get(1));
    	ListFriendsResponse response = new ListFriendsResponse();
		response.setFriends(new ArrayList<String>(commonFriends));
    	return response;
    }
    
    
    @RequestMapping(method=RequestMethod.POST, value="/subscribe")
    public @ResponseBody CommonResponse subscribe(@RequestBody SubscribeFriendsRequest payload) {
    	api.subscribing(payload.getRequestor(), payload.getTarget());
    	return new CommonResponse();
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/block")
    public @ResponseBody CommonResponse block(@RequestBody BlockFriendsRequest payload) {
    	api.block(payload.getRequestor(), payload.getTarget());
    	return new CommonResponse();
    }
    
    
    @RequestMapping(method=RequestMethod.POST, value="/getRecipients")
    public @ResponseBody CommonResponse block(@RequestBody MsgSendingRequest payload) {
    	List<String>  recipients = api.getUpdateList(payload.getSender(), payload.getText());
    	return new ListRecipientsResponse(recipients);
    }
    
    

}
