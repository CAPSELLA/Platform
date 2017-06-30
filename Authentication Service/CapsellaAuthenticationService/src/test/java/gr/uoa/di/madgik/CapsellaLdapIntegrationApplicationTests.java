package gr.uoa.di.madgik;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.uoa.di.madgik.model.User;
import gr.uoa.di.madgik.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CapsellaLdapIntegrationApplicationTests {
	
	User user;

	@Before
	public void createUser(){
		User user = new User();
		user.setFirstName("Kostas");
		//user.setFullName("Kostantinos kos");
		user.setId("kostasId");
		user.setLastName("kos");
	}

	
	 @Autowired MockMvc mockMvc; 
	 
	 @MockBean UserService userServiceMock;

	 @Autowired ObjectMapper objectMapper; 
	 
	 @Test 
	 public void testCreateClientSuccessfully() throws Exception { 
//		 given(userServiceMock.createUser(user))
//		 .willReturn(user); 
//		 this.mockMvc.perform(get("/newuser")
//				 	.with(user)
//		            .accept(MediaType.APPLICATION_JSON))
//		            .andExpect(status().isOk())
//		            .andExpect(content().string("editUser"));		
		 
	}
	 
//	@Test
//	public void insertUserLdap(){
//		   try {
//			   
//		   }
//		   catch{
//			   
//		   }
//		asset
//		
//	}
	
}
