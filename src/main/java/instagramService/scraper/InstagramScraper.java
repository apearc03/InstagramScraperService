package instagramService.scraper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;

@Component
public class InstagramScraper {

    private static final String URL = "https://www.instagram.com/";
    private final ObjectMapper mapper;

    @Inject
    public InstagramScraper(final ObjectMapper map){
        this.mapper = map;
    }

    public String buildUrl(final String name) {
        return URL.concat(name);
    }

    public Document getDocument(final String url) throws Exception {
        return Jsoup.connect(url).get();
    }

    //extract user json from the data.
    public JsonNode extractUserJson(final Document document) throws Exception {
               final String userData = getUserData(document);
               return mapper.readTree(userData.substring(userData.indexOf("{")))
                        .get("entry_data")
                        .get("ProfilePage")
                        .get(0)
                        .get("graphql")
                        .get("user");
    }

    //Extract user data.
    private String getUserData(final Document document) throws IOException {
        return document.body()
                .children()
                .stream()
                .filter(element -> element.data().contains("window._sharedData") && element.data().contains("user"))
                .findFirst()
                .orElseThrow(IOException::new).data();
    }


}
