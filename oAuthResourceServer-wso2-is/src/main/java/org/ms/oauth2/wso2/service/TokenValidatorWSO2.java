/**
 * 
 */
package org.ms.oauth2.wso2.service;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.wso2.carbon.identity.oauth2.stub.OAuth2TokenValidationService;
import org.wso2.carbon.identity.oauth2.stub.OAuth2TokenValidationServiceStub;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO_OAuth2AccessToken;
import org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationResponseDTO;
import org.wso2.carbon.utils.CarbonUtils;

/**
 * @author MS
 *
 */
@Component
public class TokenValidatorWSO2 {
	private static final Logger logger = Logger.getLogger(TokenValidatorWSO2.class);

    //@Value("${server_url}")
    private String serverUrl="https://localhost:9443/";

    //@Value("${validation_service_name}")
    private String validationServiceName="OAuth2TokenValidationService";

    //@Value("${comsumer_key}")
    private String consumerKey = "Bik3aPn991GhUkjt_4QnppmiJrca";

    //@Value("${admin_username}")
    private String adminUsername = "admin2";

    //@Value("${admin_password}")
    private String adminPassword = "msadmin";

    private OAuth2TokenValidationServiceStub stub;

    private static final int TIMEOUT_IN_MILLIS = 15 * 60 * 1000;

    public TokenValidationResponse validateAccessToken(String accessToken) throws ApplicationException, RemoteException {
        logger.debug("validateAccessToken(String) - start");

        if(stub == null) {
            initializeValidationService();
        }

        OAuth2TokenValidationRequestDTO  oauthRequest;
        TokenValidationResponse validationResponse = null;
        OAuth2TokenValidationRequestDTO_OAuth2AccessToken oAuth2AccessToken;

            oauthRequest = new OAuth2TokenValidationRequestDTO();
            oAuth2AccessToken = new OAuth2TokenValidationRequestDTO_OAuth2AccessToken();
            oAuth2AccessToken.setIdentifier(accessToken);
            oAuth2AccessToken.setTokenType("bearer");
            oauthRequest.setAccessToken(oAuth2AccessToken);
            OAuth2TokenValidationResponseDTO response = stub.validate(oauthRequest);
            
            OAuth2TokenValidationService serv;
            
            if(!response.getValid()) {
                throw new ApplicationException("Invalid access token",null);
            }

            validationResponse = new TokenValidationResponse();
            validationResponse.setAuthorizedUserIdentifier(response.getAuthorizedUser());
            validationResponse.setJwtToken(response.getAuthorizationContextToken().getTokenString());
            //
            String [] scopes = {"XYZ","ADMIN"};
            validationResponse.setScope(new LinkedHashSet<String>(Arrays.asList(scopes)));
            List<GrantedAuthority> authList = AuthorityUtils.createAuthorityList(scopes);
            validationResponse.setAuthorities(authList);
            validationResponse.setValid(response.getValid());


        logger.debug("validateAccessToken(String) - end");
        return validationResponse;
    }

    private void initializeValidationService() throws ApplicationException {
        try {
            //String serviceURL = serverUrl + validationServiceName;
            stub = new OAuth2TokenValidationServiceStub();
            CarbonUtils.setBasicAccessSecurityHeaders(adminUsername, adminPassword, true, stub._getServiceClient());
            ServiceClient client = stub._getServiceClient();
            Options options = client.getOptions();
            options.setTimeOutInMilliSeconds(TIMEOUT_IN_MILLIS);
            options.setProperty(HTTPConstants.SO_TIMEOUT, TIMEOUT_IN_MILLIS);
            options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, TIMEOUT_IN_MILLIS);
            options.setCallTransportCleanup(true);
            options.setManageSession(true);
        } catch(AxisFault ex) {
        }
    }
}
