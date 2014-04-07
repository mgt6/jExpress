package co.uk.taycon.mark.jExpress.request;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {

    private Map<String, String> requestHeaderMap = new HashMap<>();

    public Request(List<String> requestHeaders) {
        requestHeaders.stream().forEach(header -> {
            if(header.contains(":")) {
                requestHeaderMap.put(header.split(":")[0], header.split(":")[1].trim());
            }
        });
    }

    public Map<String, String> getRequestHeaderMap() {
        return Collections.unmodifiableMap(requestHeaderMap);
    }
}
