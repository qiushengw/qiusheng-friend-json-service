/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package friend;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import friend.ServerStarter;
import friend.request.BlockFriendsRequest;
import friend.request.CommonFriendRequest;
import friend.request.CommonRequest;
import friend.request.CreateRequest;
import friend.request.GetFriendsRequest;
import friend.request.MsgSendingRequest;
import friend.request.BlockFriendsRequest;
import friend.request.SubscribeFriendsRequest;
import friend.response.CommonResponse;
import friend.response.ListFriendsResponse;
import friend.response.ListRecipientsResponse;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * Basic integration tests for friend relationship service application.
 *
 * @author qiushengw
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerStarter.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class FriendServiceControllerTest {

	@LocalServerPort
	private int port;

	@Value("${local.management.port}")
	private int mgt;

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	
	@Test
	public void test_create_getFriends_getCommonFriends() throws Exception {
		
		//Create Friends
		String resourceUrl = "http://localhost:" + this.port + "/friend/create";
		HttpEntity<? extends CommonRequest> request = new HttpEntity<>(new CreateRequest(Arrays.asList(new String[]{"111@gmail.com","2222.gmail.com"})));
		CommonResponse res = this.testRestTemplate.postForObject(resourceUrl, request, CommonResponse.class);
		then(res.getSuccess()).isEqualTo(true);
		
		//Get Friends
		resourceUrl = "http://localhost:" + this.port + "/friend/getFriends";
		request = new HttpEntity<>(new GetFriendsRequest("111@gmail.com"));
		ListFriendsResponse listFriendsResponse = this.testRestTemplate.postForObject(resourceUrl, request, ListFriendsResponse.class);
		then(listFriendsResponse.getSuccess()).isEqualTo(true);
		then(listFriendsResponse.getFriends().size()).isEqualTo(1);
		
		//Create new Friends
		resourceUrl = "http://localhost:" + this.port + "/friend/create";
		request = new HttpEntity<>(new CreateRequest(Arrays.asList(new String[]{"333@gmail.com","2222.gmail.com"})));
		this.testRestTemplate.postForObject(resourceUrl, request, CommonResponse.class);
		
		//Get Common Friends
		resourceUrl = "http://localhost:" + this.port + "/friend/getCommonFriends";
		request = new HttpEntity<>(new CommonFriendRequest(Arrays.asList(new String[]{"111@gmail.com","333@gmail.com"})));
		listFriendsResponse = this.testRestTemplate.postForObject(resourceUrl, request, ListFriendsResponse.class);
		then(listFriendsResponse.getSuccess()).isEqualTo(true);
		then(listFriendsResponse.getFriends().get(0)).isEqualTo("2222.gmail.com");
		
		
	}
	
	
	@Test
	public void test_subscribe_block_getRecipients() throws Exception {
		
		//Subscribe Friends
		String resourceUrl = "http://localhost:" + this.port + "/friend/subscribe";
		HttpEntity<? extends CommonRequest> request = new HttpEntity<>(new SubscribeFriendsRequest("peck@gmail.com", "wilson@gmail.com"));
		CommonResponse res = this.testRestTemplate.postForObject(resourceUrl, request, CommonResponse.class);
		then(res.getSuccess()).isEqualTo(true);
		
		//Create new Friends
		resourceUrl = "http://localhost:" + this.port + "/friend/create";
		request = new HttpEntity<>(new CreateRequest(Arrays.asList(new String[]{"wilson@gmail.com","jack@gmail.com"})));
		this.testRestTemplate.postForObject(resourceUrl, request, CommonResponse.class);
		
		//Create new Friends
		resourceUrl = "http://localhost:" + this.port + "/friend/create";
		request = new HttpEntity<>(new CreateRequest(Arrays.asList(new String[]{"wilson@gmail.com","pandak@gmail.com"})));
		this.testRestTemplate.postForObject(resourceUrl, request, CommonResponse.class);
				
		
		//Block Friends
		resourceUrl = "http://localhost:" + this.port + "/friend/block";
		request = new HttpEntity<>(new BlockFriendsRequest("wilson@gmail.com", "jack@gmail.com"));
		res = this.testRestTemplate.postForObject(resourceUrl, request, CommonResponse.class);
		then(res.getSuccess()).isEqualTo(true);
		
		//Get recipients 
		resourceUrl = "http://localhost:" + this.port + "/friend/getRecipients";
		request = new HttpEntity<>(new MsgSendingRequest("wilson@gmail.com", "Hello World! kate@example.com"));
		ListRecipientsResponse listRecipientsResponse = this.testRestTemplate.postForObject(resourceUrl, request, ListRecipientsResponse.class);
		then(listRecipientsResponse.getSuccess()).isEqualTo(true);
		then(listRecipientsResponse.getRecipients().size()).isEqualTo(3);
		
		//Will throw exception if create friend connection on blocked relationship
		resourceUrl = "http://localhost:" + this.port + "/friend/create";
		request = new HttpEntity<>(new CreateRequest(Arrays.asList(new String[]{"wilson@gmail.com","jack@gmail.com"})));
		String stringRes = this.testRestTemplate.postForObject(resourceUrl, request, String.class);
		then(stringRes.contains("Relationship between wilson@gmail.com and jack@gmail.com is BLOCKED")).isEqualTo(true);
		
	}

}
