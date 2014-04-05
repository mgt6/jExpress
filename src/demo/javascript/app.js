var Express = Java.type("co.uk.taycon.mark.jExpress.Express");
var app = new Express();

app.get("/hello", function(req, res) {
    res.send("<html><h2>Hello World, from Nashorn</h2></html>");
});

app.listen(9999);

app.get("/test", function(req, res){
    res.send("<html><h2>This is a test</h2></html>");
});
