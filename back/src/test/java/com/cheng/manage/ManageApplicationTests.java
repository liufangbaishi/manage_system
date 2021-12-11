package com.cheng.manage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class ManageApplicationTests {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRest() {
        String str = "地震发生时";
        ResponseEntity<String> entity = restTemplate.getForEntity("http://www.syiban.com/search/index/init.html?modelid=1&q=" + str, String.class);
        int statusCodeValue = entity.getStatusCodeValue();
        System.out.println(statusCodeValue);
        String entityBody = entity.getBody();

        if (entityBody != null) {
            int startIndex = entityBody.indexOf("<div class=\"yzm-news-right\">");
            int endIndex = entityBody.indexOf("<div class=\"yzm-news-tags\">");
            String answer = entityBody.substring(startIndex, endIndex);
            answer = answer.replace("\n", "");
            answer = answer.replace("\t", "");

            int leftIndex = 0;
            int rightIndex = 0;
            while(true) {
                leftIndex = answer.indexOf("<");
                rightIndex = answer.indexOf(">");
                if (leftIndex == -1 && rightIndex == -1) {
                    break;
                }
                String temp = answer.substring(leftIndex, rightIndex+1);
                answer = answer.replace(temp, "").trim();
            }
            System.out.println(answer);
        }
    }

}
