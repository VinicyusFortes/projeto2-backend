package pt.uc.dei.proj2.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import pt.uc.dei.proj2.pojo.ProductPojo;
import pt.uc.dei.proj2.pojo.UserPojo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

@ApplicationScoped
public class UtilityBean implements Serializable {

    @Inject
    ProductBean productBean;

    // Removido o static
    private ArrayList<UserPojo> userPojos = new ArrayList<>();
    private ArrayList<ProductPojo> productPojos = new ArrayList<>();

    // Removido o static
    private int persistentCounter = 1;

    public UtilityBean() {

    }

    public void writeIntoJsonFile() {
        try (Jsonb jsonb = JsonbBuilder.create()) {
            String projectRoot = System.getProperty("user.home");
            Path directoryPath = Path.of(projectRoot + File.separator + "data");
            Path fPath = Path.of(directoryPath + File.separator + "utilizadores.json");
            System.out.println("Iniciando escrita no arquivo: " + fPath);

            Files.createDirectories(directoryPath);
            PersistedData data = new PersistedData(getUserPojos(), getPersistentCounter(), productBean.getProductPojos());
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
                    this.persistentCounter = data.getCounter();
                    this.productPojos = data.getProducts();
                    System.out.println("Usuários e contador carregados do JSON com sucesso!");
                    System.out.println(getUserPojos().toString());
                } else {
                    System.out.println("O arquivo JSON está vazio. Iniciando com uma lista vazia.");
                    userPojos = new ArrayList<>();
                    persistentCounter = 0;
                }
            } catch (Exception e) {
                throw new RuntimeException("Erro ao ler o arquivo JSON", e);
            }
        } else {
            System.out.println("Arquivo JSON não encontrado. Iniciando com uma lista vazia.");
            userPojos = new ArrayList<>();
            productPojos = new ArrayList<>();
            persistentCounter = 1;
        }
    }

    public ArrayList<UserPojo> getUserPojos() {
        return userPojos;
    }

    public void setUserPojos(ArrayList<UserPojo> userPojos) {
        this.userPojos = userPojos;
    }

    public int getPersistentCounter() {
        return persistentCounter;
    }

    public void setPersistentCounter(int persistentCounter) {
        this.persistentCounter = persistentCounter;
    }

    public ArrayList<ProductPojo> getProductPojos() {
        return productPojos;
    }

    public void setProductPojos(ArrayList<ProductPojo> productPojos) {
        this.productPojos = productPojos;
    }
}
