package pt.uc.dei.proj2.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import org.eclipse.yasson.YassonConfig;
import pt.uc.dei.proj2.pojo.ProductPojo;
import pt.uc.dei.proj2.pojo.UserPojo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class UtilityBean implements Serializable {

    @Inject
    ProductBean productBean;

    // Removido o static
    private List<UserPojo> userPojos = new ArrayList<>();
    private List<ProductPojo> productPojos = new ArrayList<>();

    // Removido o static
//    private int persistentCounter = 1;

    public UtilityBean() {

    }

    public void writeIntoJsonFile() {
        JsonbConfig config = new JsonbConfig()
                .setProperty(YassonConfig.ZERO_TIME_PARSE_DEFAULTING, true);
        try (Jsonb jsonb = JsonbBuilder.create(config)) {
            String projectRoot = System.getProperty("user.home");
            Path directoryPath = Path.of(projectRoot + File.separator + "data");
            Path fPath = Path.of(directoryPath + File.separator + "utilizadores.json");
            System.out.println("Iniciando escrita no arquivo: " + fPath);
            Files.createDirectories(directoryPath);
            PersistedData data = new PersistedData(getUserPojos(), productBean.getProductPojos());
            String jsonContent = jsonb.toJson(data);
            Files.writeString(fPath, jsonContent);
            String writtenContent = Files.readString(fPath);
            if (!writtenContent.equals(jsonContent)) {
                throw new IOException("O conteúdo escrito não corresponde ao conteúdo original");
            }
            System.out.println("Arquivo JSON escrito com sucesso em: " + fPath);
            System.out.println("Conteúdo do arquivo: " + jsonContent);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao escrever o arquivo JSON: " + e.getMessage(), e);
        }
    }

    public void loadDataFromJson() {
        String projectRoot = System.getProperty("user.home");
        Path directoryPath = Path.of(projectRoot + File.separator + "data");
        Path filePath = Path.of(directoryPath + File.separator + "utilizadores.json");
        System.out.println("A carregar ficheiro de utilizadores");

        if (Files.exists(filePath)) {
            try (Jsonb jsonb = JsonbBuilder.create()) {
                String content = Files.readString(filePath);
                if (!content.trim().isEmpty()) {
                    PersistedData data = jsonb.fromJson(content, PersistedData.class);
                    this.userPojos = data.getUsers();
                    this.productPojos = data.getProducts();
                    System.out.println("Usuários e contador carregados do JSON com sucesso!");
                    System.out.println(getUserPojos().toString());
                } else {
                    System.out.println("O arquivo JSON está vazio. Iniciando com uma lista vazia.");
                    userPojos = new ArrayList<>();
                }
            } catch (Exception e) {
                throw new RuntimeException("Erro ao ler o arquivo JSON", e);
            }
        } else {
            System.out.println("Arquivo JSON não encontrado. Iniciando com uma lista vazia.");
            userPojos = new ArrayList<>();
            productPojos = new ArrayList<>();
        }
    }

    public List<UserPojo> getUserPojos() {
        return userPojos;
    }

    public void setUserPojos(List<UserPojo> userPojos) {
        this.userPojos = userPojos;
    }

    public List<ProductPojo> getProductPojos() {
        return productPojos;
    }

    public void setProductPojos(List<ProductPojo> productPojos) {
        this.productPojos = productPojos;
    }

    public JsonObject dtoToJSON(Object dto){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonObjectBuilder builder = factory.createObjectBuilder();

        try {
            Map<String, Object> properties = objectMapper.convertValue(dto, Map.class);
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                if (entry.getValue() != null) {
                    builder.add(entry.getKey(), entry.getValue().toString());
                }
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Erro ao converter DTO para JsonObject", e);
        }

        return builder.build();
    }
}
