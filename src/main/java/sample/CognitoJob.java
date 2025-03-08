package sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;

import java.util.Map;

@Slf4j
@Component
public class CognitoJob {

    @Autowired
    private CognitoService cognitoService;

    public void execute(String[] args) {
        log.info("----- {}#execute START -----", CognitoJob.class.getName());
        try {
            if (args == null || args.length != 2) {
                log.info("Usage: java com.example.demo.DemoApp [emailAddress] [password]");
                System.exit(1);
            }

            AuthenticationResultType authenticationResultType = cognitoService.initiateAuth(args[0], args[1]);

            log.info("----- accessToken -----");
            System.out.println(authenticationResultType.accessToken());

            Map<String, String> accessTokenClaimMap = cognitoService.getJwtClaimMap(authenticationResultType.accessToken());
            log.info("----- accessTokenClaimMap -----");
            for(String key : accessTokenClaimMap.keySet()){
                log.info("{}={}", key, accessTokenClaimMap.get(key));
            }

            log.info("----- idToken -----");
            System.out.println(authenticationResultType.idToken());

            Map<String, String> idTokenClaimMap = cognitoService.getJwtClaimMap(authenticationResultType.idToken());
            log.info("----- idTokenClaimMap -----");
            for(String key : idTokenClaimMap.keySet()){
                log.info("{}={}", key, idTokenClaimMap.get(key));
            }

            log.info("----- {}#execute END-----", CognitoJob.class.getName());
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("----- {}#execute END-----", CognitoJob.class.getName());
        }
    }


}
