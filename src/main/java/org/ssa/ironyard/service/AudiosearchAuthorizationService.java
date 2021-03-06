package org.ssa.ironyard.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.ssa.ironyard.model.AuthToken;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

@Service
public class AudiosearchAuthorizationService {

    private final String oauthBaseUri;
    private final String appId;
    private final String secret;
    private final String redirect;
    private final String code;

    Logger LOGGER = LogManager.getLogger(AudiosearchAuthorizationService.class);

    public AudiosearchAuthorizationService() {
	LOGGER.info("Authorization Service is loading");
	ByteSource byteSource = Resources.asByteSource(Resources.getResource("secrets.properties"));
	Properties p = new Properties();
	try (InputStream inputStream = byteSource.openStream()) {
	    p.load(inputStream);
	} catch (IOException e) {
	    throw new RuntimeException();
	}
	oauthBaseUri = "https://www.audiosear.ch/oauth/token";
	appId = p.getProperty("appId");
	secret = p.getProperty("secret");
	code = p.getProperty("code");
	redirect = "urn:ietf:wg:oauth:2.0:oob";
    }

    public AuthToken getAccessToken() {
	Map<String, String> oauthConfig = new HashMap<>();
	oauthConfig.put("grant_type", "client_credentials");
	// oauthConfig.put("code", code);
	oauthConfig.put("redirect_uri", redirect);
	oauthConfig.put("client_id", appId);
	oauthConfig.put("client_secret", secret);
	RestTemplate restTemplate = new RestTemplate();
	AuthToken a = restTemplate.postForEntity(oauthBaseUri, oauthConfig, AuthToken.class).getBody();

	return a;

    }
}
