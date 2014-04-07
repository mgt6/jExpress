package co.uk.taycon.mark.jExpress;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.BiConsumer;

public class StaticRequest {

    public static BiConsumer<Request, Response> getStatic(String filePath) {
        return (req, res) -> res.send(200, readFile(filePath, Charset.defaultCharset()));
    }

    private static String readFile(String path, Charset encoding) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoding.decode(ByteBuffer.wrap(encoded)).toString();
    }

}
