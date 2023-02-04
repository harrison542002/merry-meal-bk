package com.merry.meal.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.merry.meal.payload.ApiResponse;
import com.merry.meal.payload.UserDto;
import com.merry.meal.services.FileService;
import com.merry.meal.services.UserService;
import com.merry.meal.utils.JwtUtils;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	/*
	 * this controller is for user entity, service
	 */

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	/// Changes ||||||||||||||||| changed accept by - HASSAN
	@Autowired
	private JwtUtils jwtUtils;

	// register local user
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUserLocal(HttpServletRequest request, @Valid @RequestBody UserDto userDto) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.createUserProfile(userDto, jwtUtils.getJWTFromRequest(request)));
	}

	// uploading user profile image
	@PostMapping("/image/upload-profile-image")
	public ResponseEntity<ApiResponse> uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile image)
			throws IOException {

		// insuring the request has a file
		if (image.isEmpty()) {
			return new ResponseEntity<ApiResponse>(new ApiResponse("Request must have a file", false),
					HttpStatus.BAD_REQUEST);
		}

		// uploading the file into server
		this.userService.uploadImage(image, jwtUtils.getJWTFromRequest(request));
		return new ResponseEntity<ApiResponse>(new ApiResponse("Profile image uploaded successfully", true),
				HttpStatus.OK);

	}
	//get user info to admin dashboard
		@GetMapping("/")
		public ResponseEntity<?> getUsers(){
			return ResponseEntity.ok(userService.getUserProfiles());
	}

	// method to serve user profile image
	@GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
		InputStream resource = this.fileService.getResource(imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

}
