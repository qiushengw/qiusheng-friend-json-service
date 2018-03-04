package friend.api;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import friend.FriendMessage;
import friend.FriendRelationship;
import friend.Relationship;
import friend.dao.FriendMessageRepository;
import friend.dao.FriendRelationshipRepository;



public class RelationshipApiImplTest{
	
	private RelationshipApiImpl relationshipApiImpl = new RelationshipApiImpl();
	
	
	@Test
	public void test_create() {
		FriendRelationshipRepository repo = EasyMock.createMock(FriendRelationshipRepository.class);
		ReflectionTestUtils.setField(relationshipApiImpl, "repository", repo);
		FriendRelationship f = new FriendRelationship("aaa@gmail.com", "bbb@gmail.com", Relationship.FRIEND);
		EasyMock.expect(repo.save(f)).andReturn(f);
		f = new FriendRelationship("bbb@gmail.com", "aaa@gmail.com", Relationship.FRIEND);
		EasyMock.expect(repo.save(f)).andReturn(f);
		EasyMock.expect(repo.countRelationship("aaa@gmail.com", "bbb@gmail.com", Relationship.BLOCK)).andReturn(0);
		EasyMock.expect(repo.countRelationship("bbb@gmail.com", "aaa@gmail.com", Relationship.BLOCK)).andReturn(0);
		EasyMock.expect(repo.countRelationship("aaa@gmail.com", "bbb@gmail.com", Relationship.FRIEND)).andReturn(0);
		EasyMock.expect(repo.countRelationship("bbb@gmail.com", "aaa@gmail.com", Relationship.FRIEND)).andReturn(0);
		EasyMock.replay(repo);
		
		relationshipApiImpl.create("aaa@gmail.com", "bbb@gmail.com");
		
		EasyMock.verify(repo);
		
	}
	
	
	
	@Test
	public void test_create_withExistingError() {
		FriendRelationshipRepository repo = EasyMock.createMock(FriendRelationshipRepository.class);
		ReflectionTestUtils.setField(relationshipApiImpl, "repository", repo);
		EasyMock.expect(repo.countRelationship("aaa@gmail.com", "bbb@gmail.com", Relationship.BLOCK)).andReturn(1);
		EasyMock.expect(repo.countRelationship("bbb@gmail.com", "aaa@gmail.com", Relationship.BLOCK)).andReturn(1);
		EasyMock.replay(repo);
		
		
		RelationshipException exception = null;
		try{
			relationshipApiImpl.create("aaa@gmail.com", "bbb@gmail.com");
		}catch(Exception e){
			exception = (RelationshipException)e;
		}
		
		
		assertThat(exception.getErrorCode(), is("CREATE_RALTIONSHIP_BLOCK_ERROR"));
		EasyMock.verify(repo);
		
	}
	
	@Test
	public void test_getFriends(){
		FriendRelationshipRepository repo = EasyMock.createMock(FriendRelationshipRepository.class);
		ReflectionTestUtils.setField(relationshipApiImpl, "repository", repo);
		EasyMock.expect(repo.findFriends("aaa@gmail.com")).andReturn(Arrays.asList(
				new FriendRelationship[]{
						new FriendRelationship("aaa@gmail.com", "222@gmail.com", Relationship.FRIEND),
						new FriendRelationship("333@gmail.com", "aaa@gmail.com", Relationship.FRIEND)
				}
				));
		EasyMock.replay(repo);
		
		assertThat(relationshipApiImpl.getFriends("aaa@gmail.com").size(), is(2));
		
		EasyMock.verify(repo);
		
	}
	
	
	
	@Test
	public void test_getCommonFriends() {
		FriendRelationshipRepository repo = EasyMock.createMock(FriendRelationshipRepository.class);
		ReflectionTestUtils.setField(relationshipApiImpl, "repository", repo);

		EasyMock.expect(repo.findFriends("aaa@gmail.com")).andReturn(Arrays.asList(
				new FriendRelationship[]{
						new FriendRelationship("aaa@gmail.com", "222@gmail.com", Relationship.FRIEND)
				}
				));
		
		EasyMock.expect(repo.findFriends("bbb@gmail.com")).andReturn(Arrays.asList(
				new FriendRelationship[]{
						new FriendRelationship("bbb@gmail.com", "222@gmail.com", Relationship.FRIEND)
				}
				));
	
		
		EasyMock.replay(repo);
		List<String> commonFriends = relationshipApiImpl.getCommonFriends("aaa@gmail.com", "bbb@gmail.com");
		
		assertThat(commonFriends.contains("222@gmail.com"),is(true));
		EasyMock.verify(repo);
		
	}
	
	
	@Test
	public void test_subscribing() {
		FriendRelationshipRepository repo = EasyMock.createMock(FriendRelationshipRepository.class);
		ReflectionTestUtils.setField(relationshipApiImpl, "repository", repo);
		FriendRelationship f = new FriendRelationship("aaa@gmail.com", "bbb@gmail.com", Relationship.SUBSCRIBING);
		EasyMock.expect(repo.save(f)).andReturn(f);
		EasyMock.expect(repo.countRelationship("aaa@gmail.com", "bbb@gmail.com", Relationship.SUBSCRIBING)).andReturn(0);
		EasyMock.replay(repo);
		
		FriendRelationship newRelationship = relationshipApiImpl.subscribing("aaa@gmail.com", "bbb@gmail.com");
		
		assertThat(newRelationship.getDesEmail(), is("bbb@gmail.com"));
		assertThat(newRelationship.getRelationship(), is(Relationship.SUBSCRIBING));
		
		
		EasyMock.verify(repo);
		
	}
	
	@Test
	public void test_subscribing_withError(){
		FriendRelationshipRepository repo = EasyMock.createMock(FriendRelationshipRepository.class);
		ReflectionTestUtils.setField(relationshipApiImpl, "repository", repo);
		EasyMock.expect(repo.countRelationship("aaa@gmail.com", "bbb@gmail.com", Relationship.SUBSCRIBING)).andReturn(1);
		EasyMock.replay(repo);
		
		RelationshipException exception = null;
		try{
			relationshipApiImpl.subscribing("aaa@gmail.com", "bbb@gmail.com");
		}catch(Exception e){
			exception = (RelationshipException)e;
		}
		
		assertThat(exception.getErrorCode(), is("SUBSCRIBE_RALTIONSHIP_EXISTING_ERROR"));
		EasyMock.verify(repo);
		
	}
	
	@Test
	public void test_block() {
		FriendRelationshipRepository repo = EasyMock.createMock(FriendRelationshipRepository.class);
		ReflectionTestUtils.setField(relationshipApiImpl, "repository", repo);
		FriendRelationship f = new FriendRelationship("aaa@gmail.com", "bbb@gmail.com", Relationship.BLOCK);
		EasyMock.expect(repo.save(f)).andReturn(f);
		EasyMock.expect(repo.getRelationship("aaa@gmail.com", "bbb@gmail.com")).andReturn(null);
		EasyMock.replay(repo);
		
		relationshipApiImpl.block("aaa@gmail.com", "bbb@gmail.com");
		
		
		EasyMock.verify(repo);
		
	}
	
	
	@Test
	public void test_getUpdateList(){
		FriendRelationshipRepository repo = EasyMock.createMock(FriendRelationshipRepository.class);
		
		FriendMessageRepository msgRepo = EasyMock.createMock(FriendMessageRepository.class);
		
		ReflectionTestUtils.setField(relationshipApiImpl, "repository", repo);
		ReflectionTestUtils.setField(relationshipApiImpl, "friendMessageRepository", msgRepo);
		
		EasyMock.expect(repo.findFriends("bbb@gmail.com")).andReturn(Arrays.asList(
				new FriendRelationship[]{
						new FriendRelationship("bbb@gmail.com", "222@gmail.com", Relationship.FRIEND)
				}
				));
		
		EasyMock.expect(repo.findSubscribes("bbb@gmail.com")).andReturn(Arrays.asList(
				new FriendRelationship[]{
						new FriendRelationship("555@gmail.com", "bbb@gmail.com", Relationship.SUBSCRIBING),
						new FriendRelationship("666@gmail.com", "bbb@gmail.com", Relationship.SUBSCRIBING)
				}
				));
		
		List<FriendMessage> msgList = new ArrayList<FriendMessage>();
		Capture<List<FriendMessage>> captured = new Capture<List<FriendMessage>>();
		EasyMock.expect(msgRepo.save(EasyMock.capture(captured))).andReturn(msgList);
		EasyMock.replay(repo,msgRepo);
		
		List<String> friends = relationshipApiImpl.getUpdateList("bbb@gmail.com", "Hello World! kate@example.com");
		
		assertThat(friends.size(), is(4));
		
		EasyMock.verify(repo);
		
		
		
	}
	
	

}
