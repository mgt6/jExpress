package co.uk.taycon.mark.jExpress.route;

import java.util.Arrays;
import java.util.List;

public class Route {

    private String method;

    private String path;

    public Route(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public Route(String requestString) {
        List<String> request = Arrays.asList(requestString.split(" "));
        this.method = request.get(0);
        this.path = request.get(1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (!method.equals(route.method)) return false;
        if (!path.equals(route.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }
}
