package instagramService.user;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Component
public class JsonBuilder {

    @Inject
    public JsonBuilder(){

    }

    public Map<String, Object> buildJsonResponse(final JsonNode userInfo){
        //TODO
        Map<String, Object> m = new HashMap<>();
        m.put("worked", true);
        return m;
    }


    public Map<String,Object> buildFailedResponse() {
        //TODO
        Map<String, Object> m = new HashMap<>();
        m.put("worked", false);
        return m;
    }
}