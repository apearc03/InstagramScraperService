package instagramService.scraper;

import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class JsonResponseBuilder {

    private final InstagramScraper scraper;
    private final JsonBuilder jsonBuilder;

    @Inject
    JsonResponseBuilder(final InstagramScraper scrape, final JsonBuilder builder){
        this.scraper = scrape;
        this.jsonBuilder = builder;
    }


    public List<JsonNode> getScrapedUsernamesJson(final String[] usernames){
        //Create a thread for each Instagram page to scrape.
        final ExecutorService executorService = Executors.newWorkStealingPool();
        final List<JsonNode> result = new ArrayList<>();
        try {
            for(String username : usernames){
                executorService.submit(()-> result.add(scrape(username)));
            }
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            result.add(jsonBuilder.executorServiceError());
        }
        return result;
    }

    private JsonNode scrape(final String username){
        try {
            final Document userPage = scraper.getDocument(scraper.buildUrl(username));
            final JsonNode userJson = scraper.extractUserJson(userPage);
            return jsonBuilder.buildJsonResponse(userJson, username);
        } catch (Exception e) {
            return jsonBuilder.buildFailedResponse(username);
        }
    }

}
