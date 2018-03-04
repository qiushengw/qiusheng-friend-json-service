package friend.api;

public class RelationshipException extends RuntimeException{
	private static final long serialVersionUID = 0L;
	
	private String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	
	public RelationshipException(String errorCode, String errorDesc){
		super(errorDesc);
		this.errorCode = errorCode;
	}
	
	public RelationshipException(String errorCode, String errorDesc, Throwable e){
		super(errorDesc, e);
		this.errorCode = errorCode;
		
	}
	

}
