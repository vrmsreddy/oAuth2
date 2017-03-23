package org.ms.oauth2.wso2.util;

import org.jose4j.json.JsonUtil;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author MS
 */
public class OAuth2Utils {

	private static final JwtConsumer jwtConsumer = new JwtConsumerBuilder()
			.setSkipAllValidators().setDisableRequireSignature()
			.setSkipSignatureVerification().build();

	private static final Logger logger = LoggerFactory
			.getLogger(OAuth2Utils.class);

	public static void logJWTClaims(String accessToken) {
		JwtContext jwtContext;
		try {
			jwtContext = jwtConsumer.process(accessToken);
			logger.info(prettyPrintJson(JsonUtil.toJson(jwtContext.getJwtClaims()
					.getClaimsMap())));
		} catch (InvalidJwtException e) {
			logger.error("Invalid JWT", e);
		}
	}

	public static void logJson(String json) {
		logger.info(prettyPrintJson(json));
	}

	public static String prettyPrintJson(String flatJson) {
		return (new JSONObject(flatJson).toString(3));
	}
	
}
