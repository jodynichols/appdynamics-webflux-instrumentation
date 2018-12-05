package io.spring.workshop.stockquotes;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.Random;


/**
 * IEXTradingClient fetch stocks data using the "iextrading.com" API
 * For more info see: https://iextrading.com/developer/docs/#stocks
 */
@Component
public class CustomTier {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private LinkedMultiValueMap<String, String> getIexQueryParams() {
        Set<String> symbols = new HashSet<>();
        Set<String> content_types = new HashSet<>();
        symbols.add("APPL");
        content_types.add("quote");

        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("symbols", StringUtils.join(symbols, ","));
        map.add("types", StringUtils.join(content_types, ","));
        return map;
    }
    public Mono<JsonNode> search(SearchResult req) {

        Random rand = new Random();
        int randomNum = rand.nextInt(10) + 1;

        /*
        https://jsonplaceholder.typicode.com/todos/1
         */

        return WebClient.create().get()
                .uri(builder -> builder.scheme("https")
                        .host("jsonplaceholder.typicode.com")
                        .path("/todos/"+randomNum)
                        .build()
                )
                .retrieve().bodyToMono(JsonNode.class);
    }

}
