import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonWriter {
    public static final String DEFAULT_FILE_PATH = "data/app_state.json";

    public static void writeToJson(AppState appState) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(appState);

        File file = new File(DEFAULT_FILE_PATH);
        file.getParentFile().mkdirs(); // создаем папки, если нужно

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
            System.out.println("Состояние программы сохранено в: " + DEFAULT_FILE_PATH);
        }
    }
}