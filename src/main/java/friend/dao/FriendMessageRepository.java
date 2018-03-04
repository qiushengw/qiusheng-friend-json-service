package friend.dao;
import org.springframework.data.repository.CrudRepository;

import friend.FriendMessage;

public interface FriendMessageRepository extends CrudRepository<FriendMessage, Long>{
	
	
}
