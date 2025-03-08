package sample;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CognitoService {

    @Value("${cognito.clientId}")
    private String clientId;

    @Value("${cognito.clientSecret}")
    private String clientSecret;

    public AuthenticationResultType initiateAuth(String userName, String password) {
        try (CognitoIdentityProviderClient identityProviderClient =
                 CognitoIdentityProviderClient.builder().region(Region.AP_NORTHEAST_1).build()
        ) {
            Map<String, String> authParameters = new HashMap<>();
            authParameters.put("USERNAME", userName);
            authParameters.put("PASSWORD", password);
            authParameters.put("SECRET_HASH", calculateSecretHash(userName));

            InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                                                                 .clientId(clientId)
                                                                 .authParameters(authParameters)
                                                                 .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                                                                 .build();
            InitiateAuthResponse response = identityProviderClient.initiateAuth(authRequest);
            return response.authenticationResult();
        }
    }

    public Map<String, String>  getJwtClaimMap(String token) {
        Map<String, String> resultMap = new HashMap<>();

        DecodedJWT jwt = JWT.decode(token);

        for (Map.Entry<String, Claim> entry : jwt.getClaims().entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue().toString());
        }

        return resultMap;
    }

    private String calculateSecretHash(String userName) {
        final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
        SecretKeySpec signingKey = new SecretKeySpec(this.clientSecret.getBytes(StandardCharsets.UTF_8),
                                                     HMAC_SHA256_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(signingKey);
            mac.update(userName.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(this.clientId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Error while calculating ");
        }
    }

}
