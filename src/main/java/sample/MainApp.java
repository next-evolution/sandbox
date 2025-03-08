package sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApp {

    public static void main(String[] args) {

        try(ConfigurableApplicationContext context = SpringApplication.run(MainApp.class, args)){
            CognitoJob cognitoJob = context.getBean(CognitoJob.class);
            cognitoJob.execute(args);
        }

//        try (CognitoIdentityProviderClient identityProviderClient = CognitoIdentityProviderClient.builder()
//                                                                                                 .region(Region.AP_NORTHEAST_1)
//                                                                                                 .build()
//        ) {
//            InitiateAuthResponse
//                response =
//                initiateAuth(identityProviderClient,
//                             "1fd0smhk91h1gg6t8c9pinght",
//                             "shoichi.tada@next-evolution.co.jp",
//                             "Krnetwork@2025",
//                             "ap-northeast-1_Rv8byvwuB");
//            System.out.println("----- accessToken -----");
//            System.out.println(response.authenticationResult().accessToken());
//            System.out.println("----- idToken -----");
//            System.out.println(response.authenticationResult().idToken());
//            jwtClaims(response.authenticationResult().idToken());
//        }
    }

//    public static void jwtClaims(String bearerToken) {
//        DecodedJWT jwt = JWT.decode(bearerToken);
//        Map<String, Claim> claims = jwt.getClaims();
//        System.out.println("----- claims -----");
//        for (Map.Entry<String, Claim> entry : claims.entrySet()) {
//            System.out.println(entry.getKey() + "=" + entry.getValue());
//        }
//    }

//    public static InitiateAuthResponse initiateAuth(CognitoIdentityProviderClient identityProviderClient,
//                                                    String clientId,
//                                                    String userName,
//                                                    String password,
//                                                    String userPoolId) {
//        try {
//            Map<String, String> authParameters = new HashMap<>();
//            authParameters.put("USERNAME", userName);
//            authParameters.put("PASSWORD", password);
//            authParameters.put("SECRET_HASH", "556CZRqY3iTN2DBaaJiv8KtCnMvlt7ABVyoqzsD97es=");
//
//            System.out.println(calculateSecretHash(clientId, "1k4qakn79b9j9d8vaa756qspd2jccodp7lmmv4qvllkkp1b072p7",
//                                                   userName));
//
//            InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
//                                                                 .clientId(clientId)
////                                                                 .userPoolId(userPoolId)
//                                                                 .authParameters(authParameters)
//                                                                 .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
//                                                                 .build();
//
//            InitiateAuthResponse response = identityProviderClient.initiateAuth(authRequest);
//            System.out.println(response.authenticationResult());
//            return response;
//
//        } catch (CognitoIdentityProviderException e) {
//            System.err.println(e.awsErrorDetails().errorMessage());
//            System.exit(1);
//        }
//
//        return null;
//    }

//    public static String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String userName) {
//        final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
//
//        SecretKeySpec signingKey = new SecretKeySpec(
//            userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
//            HMAC_SHA256_ALGORITHM);
//        try {
//            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
//            mac.init(signingKey);
//            mac.update(userName.getBytes(StandardCharsets.UTF_8));
//            byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
//            return Base64.getEncoder().encodeToString(rawHmac);
//        } catch (Exception e) {
//            throw new RuntimeException("Error while calculating ");
//        }
//    }

//    public static void test(){
//        Map<String, String> params = new HashMap<>();
//        params.put("USERNAME", userId);
//        params.put("SECRET_HASH", calculateSecretHash(userId));
//        params.put("PASSWORD", rawPassword);
//
//        AdminInitiateAuthRequest request = new AdminInitiateAuthRequest()
//            .withUserPoolId("YOUR_USER_POOL_ID")
//            .withClientId("YOUR_USER_POOL_APP_CLIENT_ID")
//            .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
//            .withAuthParameters(params);
//
//        AWSCognitoIdentityProvider identityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
//                                                                                             .withCredentials(credentialsProvider)
//                                                                                             .withRegion(Regions.US_WEST_2)
//                                                                                             .build();
//        AdminInitiateAuthResult result = identityProvider.adminInitiateAuth(request);
//    }

}
