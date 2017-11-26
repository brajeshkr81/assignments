package com.intellectdesign.assignment.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellectdesign.assignment.domain.AssignmentResponse;
import com.intellectdesign.assignment.domain.User;
import com.intellectdesign.assignment.error.ValidationError;

@RestController
public class AssignmentController {
	private static File file = new File("Repo.txt");
	static Map<String, User> userMap = new HashMap<>();
	
	@RequestMapping(value  = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody User user)
	{
		int temp =0;
		AssignmentResponse assignmentResponse = new AssignmentResponse();
		for(Entry<String, User> users: userMap.entrySet()){
			if(users.getValue().getEmail().equals(user.getEmail())){
				temp =1;
			}
		}
		if(temp==1){
			return new ResponseEntity<>("Error", HttpStatus.CONFLICT);
		}
		else{
			userMap.put(user.getId(),user);
			System.out.println(userMap.entrySet().toString());
			//ObjectMapper mapper = new ObjectMapper();
			//mapper.writeValue(file, user);
			
			assignmentResponse.setResMsg("User created successfully");
			assignmentResponse.setUserId(user.getId());
		}
		return new ResponseEntity<>(assignmentResponse, HttpStatus.OK);
	}
	@RequestMapping(value  = "/update", method = RequestMethod.POST)
	public ResponseEntity<?> update(@RequestBody User user)
	{
		AssignmentResponse assignmentResponse = new AssignmentResponse();
		
			ValidationError validationError = new ValidationError();
			List<ValidationError> list = new ArrayList<>();
			
		User existingUser = userMap.get(user.getId());
		if(!existingUser.getfName().equals(user.getfName())){
			validationError.setCode("001");
			validationError.setField("fName");
			validationError.setMessage("Updating Fname not allowed");
			list.add(validationError);
			return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
		}
		if(!existingUser.getlName().equals(user.getlName())){
			validationError.setCode("002");
			validationError.setField("lName");
			validationError.setMessage("Updating Lname not allowed");
			list.add(validationError);
			return new ResponseEntity<>(list, HttpStatus.FORBIDDEN);
		}
		
		userMap.put(user.getId(),user);
		System.out.println(userMap.entrySet().toString());	
		assignmentResponse.setResMsg("User updated successfully");
		assignmentResponse.setUserId(user.getId());
		return new ResponseEntity<>(assignmentResponse, HttpStatus.OK);
	}
	
	@RequestMapping(value  = "/delete/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable String userId)
	{
		AssignmentResponse assignmentResponse = new AssignmentResponse();	
		User existingUser = userMap.get(userId);
		existingUser.setActive(false);
		userMap.put(userId,existingUser);
		System.out.println(userMap.entrySet().toString());
		assignmentResponse.setResMsg("User deactivated successfully");
		assignmentResponse.setUserId(userId);
		return new ResponseEntity<>(assignmentResponse, HttpStatus.OK);
	}


}
