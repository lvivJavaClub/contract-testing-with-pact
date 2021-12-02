package com.example.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.LambdaDsl;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

//to publish use command: mvn pact:publish
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "dateProvider", port = "1234")
public class PactAgeConsumerTest {
    @Pact(provider="dateProvider", consumer = "ageConsumer")
    public RequestResponsePact validDateFromProvider(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");

        return builder
                .given("valid date received from provider")
                .uponReceiving("valid date from provider")
                .method("GET")
                .queryMatchingDate("date", "2001-02-03")
                .path("/provider/validDate")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body(LambdaDsl.newJsonBody((object) -> {
                    object.numberType("year", 2000);
                    object.numberType("month", 2);
//                    object.stringType("month", "Feb");
                    object.numberType("day", 3);
                    object.booleanType("isValidDate", true);
                }).build())
                .toPact();
    }

    @Test
    @ExtendWith(PactConsumerTestExt.class)
    @PactTestFor(providerName = "dateProvider")
    public void testValidDateFromProvider(MockServer mockServer) throws IOException {
        HttpResponse httpResponse = Request.Get(mockServer.getUrl() + "/provider/validDate?date=2001-02-03")
                .execute().returnResponse();

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(200);
        assertThat(JsonPath.read(httpResponse.getEntity().getContent(), "$.isValidDate").toString()).isEqualTo("true");
    }
}
