package com.banquito.transaction.request;

import com.banquito.transaction.Utils.Utils;
import com.banquito.transaction.request.dto.RQAccountBalance;
import com.banquito.transaction.request.dto.RSAccount;
import com.banquito.transaction.request.dto.RSGeneric;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AccountRequest {

    private static RestTemplate restTemplate = new RestTemplate();
    private static final String account = "http://localhost:9001/api/account";

    public static RSAccount getAccountData(String codeLocalAccount, String codeInternationalAccount){

        String url = account.concat("/code/{codeLocalAccount}/{codeInternationalAccount}");

        ResponseEntity<RSGeneric> response = restTemplate.getForEntity(
                url,
                RSGeneric.class,
                codeLocalAccount,
                codeInternationalAccount);

        if(response.getStatusCode().is2xxSuccessful()){
            if(Utils.hasAllAttributes(response.getBody())){
                return new ObjectMapper().convertValue(response.getBody().getData(),RSAccount.class);
            }
        }

        return null;
    }

    public static Boolean updateAccountBalance(String codeLocalAccount,
                                              String codeInternationalAccount,
                                              RQAccountBalance rqAccountBalance){
        String url = account.concat("/code/{codeLocalAccount}/{codeInternationalAccount}/balance");

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


        //Request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("presentBalance", rqAccountBalance.getPresentBalance());
        map.put("availableBalance", rqAccountBalance.getAvailableBalance());

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT,entity,Void.class,codeLocalAccount,codeInternationalAccount);

        return response.getStatusCode().is2xxSuccessful() ? true : false;
    }
}
