package friend.dao;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import friend.FriendRelationship;
import friend.Relationship;

public interface FriendRelationshipRepository extends CrudRepository<FriendRelationship, Long>{
	
	@Query("select f from FriendRelationship f where f.srcEmail = ?1 and f.relationship = 'FRIEND'")
    List<FriendRelationship> findFriends(String email);
	
	@Query("select count(1) from FriendRelationship f where f.srcEmail = ?1 and desEmail=?2 and f.relationship = ?3")
	int countRelationship(String srcEmail, String desEmail, Relationship relationship);
	
	@Query("select f from FriendRelationship f where f.desEmail = ?1 and f.relationship = 'SUBSCRIBING'")
    List<FriendRelationship> findSubscribes(String email);
	
	@Query("select f from FriendRelationship f where (f.srcEmail = ?1 and f.desEmail = ?2) or (f.srcEmail = ?2 and f.desEmail = ?1)")
	List<FriendRelationship> getRelationshipBetween(String srcEmail, String desEmail);
	
	@Query("select f from FriendRelationship f where f.srcEmail = ?1 and f.desEmail = ?2")
	FriendRelationship getRelationship(String srcEmail, String desEmail);
	
}
