package friend;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.h2.util.StringUtils;

@Entity
public class FriendRelationship {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String srcEmail;
	private String desEmail;
	
	@Enumerated(EnumType.STRING)
	private Relationship relationship;
	
	public FriendRelationship(){
		
	}
	
	public FriendRelationship(String srcEmail, String desEmail, Relationship relationship){
		this.srcEmail = srcEmail;
		this.desEmail = desEmail;
		this.relationship = relationship;	
	}
	
	public boolean equals(Object o){
		if(o instanceof FriendRelationship){
			FriendRelationship f = (FriendRelationship)o;
			
			return StringUtils.equals(this.getSrcEmail(), f.getSrcEmail()) && 
					StringUtils.equals(this.getDesEmail(), f.getDesEmail()) &&
					this.getRelationship()==f.getRelationship();
		}
		
		
		return false;
	}
	
	public int hashCode(){
		return new HashCodeBuilder(17, 37).
	       append(srcEmail).
	       append(desEmail).
	       append(relationship.toString()).
	       toHashCode();
	}
	
	
	public String getDesEmail() {
		return desEmail;
	}
	public void setDesEmail(String desEmail) {
		this.desEmail = desEmail;
	}
	public Relationship getRelationship() {
		return relationship;
	}
	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getSrcEmail() {
		return srcEmail;
	}

	public void setSrcEmail(String srcEmail) {
		this.srcEmail = srcEmail;
	}
	

}
