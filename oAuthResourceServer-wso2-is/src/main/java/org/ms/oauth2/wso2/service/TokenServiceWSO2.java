/**
 * 
 */
package org.ms.oauth2.wso2.service;

import java.rmi.RemoteException;

import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.client.HttpServerErrorException;

/**
 * @author MS
 *
 */
public class TokenServiceWSO2 implements ResourceServerTokenServices {
	@Autowired
    TokenValidatorWSO2 tokenValidatorWSO2;
	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.ResourceServerTokenServices#loadAuthentication(java.lang.String)
	 */
	@Override
	public OAuth2Authentication loadAuthentication(String accessToken)
			throws AuthenticationException, InvalidTokenException {
		 OAuth2Authentication oAuth2Authentication = null;
		try {
            TokenValidationResponse validationResponse = tokenValidatorWSO2.validateAccessToken(accessToken);
            OAuth2Request oAuth2Request = new OAuth2Request(null, null, null, true, validationResponse.getScope(), null, null, null,null);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(validationResponse.getAuthorizedUserIdentifier(), null, validationResponse.getAuthorities());
            oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
            
        } catch (ApplicationException ex) {
            throw new HttpServerErrorException(HttpStatus.UNAUTHORIZED, "User Not Authorized");
        } catch (RemoteException e) {
			throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE, "Authorization Server Down");
		}
		return  oAuth2Authentication;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.oauth2.provider.token.ResourceServerTokenServices#readAccessToken(java.lang.String)
	 */
	@Override
	public OAuth2AccessToken readAccessToken(String accessToken) {
		// TODO Auto-generated method stub
		return null;
	}

}
