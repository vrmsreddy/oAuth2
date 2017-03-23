package org.ms.oauth2.wso2.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
	//@PostAuthorize("#oauth2.hasRole('USER')")
	@PreAuthorize("hasAnyAuthority('USER,XYZ')")
	@RequestMapping("/protected")
	public String protectd(HttpServletRequest req) {

       // OAuth2Utils.logJWTClaims(req.getParameter("access_token"));
		return "this is a protected OAuth2 resource";
	}

	@RequestMapping("/unprotected")
	public String unprotectd() {
		return "this is not protected";
	}
	
}
