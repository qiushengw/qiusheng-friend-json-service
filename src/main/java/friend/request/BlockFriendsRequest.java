package friend.request;

public class BlockFriendsRequest extends CommonRequest {
	
	private String requestor;
	private String target;
	
	public BlockFriendsRequest(){
		
	}
	
	public BlockFriendsRequest(String requestor, String target){
		this.requestor = requestor;
		this.target = target;
	}
	
	
	public String getRequestor() {
		return requestor;
	}
	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	

}
