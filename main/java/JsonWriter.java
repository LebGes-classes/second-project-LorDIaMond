import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonWriter {

    // Теперь метод принимает List<Product>
    public static void writeToJson(List<Product> products, String filePath) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(products);

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        }
    }
}