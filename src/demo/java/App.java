import co.uk.taycon.mark.jExpress.Express;


public class App {

    Express app = new Express();

    public App() {

        app.get("/hello", (req, res) ->
            res.send("<html><h2>Hello World!</h2></html>")
        );

        app.get("/test", (req, res) ->
            res.send("{\n" +
                    "    \"test\": {\n" +
                    "        \"message\": \"this is a test\",\n" +
                    "        \"version\": 1\n" +
                    "    }\n" +
                    "}")
        );

        app.staticResource("/", "/Users/marktaylor/Development/java/jExpress/src/demo/resources/html/index.html");

        app.listen(9999);

        app.get("/test2", (req, res) ->
            res.send("{\n" +
                    "    \"test\": {\n" +
                    "        \"message\": \"this is a test\",\n" +
                    "        \"version\": 2\n" +
                    "    }\n" +
                    "}")
        );

    }

    public static void main(String[] args) {
        new App();
    }

}
