package pt.uc.dei.proj2.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
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

import static pt.uc.dei.proj2.beans.UtilityBean.persistentCounter;
import static pt.uc.dei.proj2.beans.ProductBean.productPojos;


/**
 * Escreve os dados dos usuários e o contador persistente em um arquivo JSON.
 * Esta função salva tanto a lista de usuários quanto o valor atual do contador.
 */
@ApplicationScoped
public class UtilityBean implements Serializable {

  // Lista estática para armazenar os objetos UserPojo
 /* private  ArrayList<UserPojo> userPojos = new ArrayList<>();
  private  ArrayList<ProductPojo> productPojos = new ArrayList<>();*/

  // Contador persistente para gerar IDs únicos de usuários
  /*private  int persistentCounter = 1;*/

  public UtilityBean() {
  }

  public void writeIntoJsonFile() {

    Jsonb jsonb = JsonbBuilder.create();

    String projectRoot = System.getProperty("user.home");
    Path directoryPath = Path.of(projectRoot + File.separator + "data");
    Path fPath = Path.of(directoryPath + File.separator + "utilizadores.json");

    System.out.println("Iniciando escrita no arquivo: " + fPath);

    try {
      // Garante que o diretório existe
      Files.createDirectories(directoryPath);

      // Prepara os dados para serem escritos
      PersistedData data = new PersistedData(getUserPojos(), getPersistentCounter(), productPojos);


      // Serializa os dados para uma string JSON
      String jsonContent = jsonb.toJson(data);

      // Escreve a string JSON no arquivo
      Files.writeString(fPath, jsonContent);

      // Verifica se o arquivo foi escrito corretamente
      String writtenContent = Files.readString(fPath);
      if (!writtenContent.equals(jsonContent)) {
        throw new IOException("O conteúdo escrito não corresponde ao conteúdo original");
      }

      System.out.println("Arquivo JSON escrito com sucesso em: " + fPath);
      System.out.println("Conteúdo do arquivo: " + jsonContent);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Erro ao escrever o arquivo JSON: " + e.getMessage(), e);
    }
  }

  /**
   * Carrega os usuários e o contador persistente do arquivo JSON.
   * Se o arquivo não existir ou estiver vazio, inicializa com valores padrão.
   */
  public void loadDataFromJson() {
    String projectRoot = System.getProperty("user.home");
    Path directoryPath = Path.of(projectRoot + File.separator + "data");
    Path filePath = Path.of(directoryPath + File.separator + "utilizadores.json");

    System.out.println("A carregar ficheiro de utilizadores");

    if (Files.exists(filePath)) {
      try {
        Jsonb jsonb = JsonbBuilder.create();
        String content = Files.readString(filePath);

        if (!content.trim().isEmpty()) {
          PersistedData data = jsonb.fromJson(content, PersistedData.class);
          this.userPojos = data.users;
          this.persistentCounter = data.counter;
          this.productPojos = data.products;


          System.out.println("Usuários e contador carregados do JSON com sucesso!");
          System.out.println(getUserPojos().toString());
        } else {
          System.out.println("O arquivo JSON está vazio. Iniciando com uma lista vazia.");
          getUserPojos() = new ArrayList<>();
          persistentCounter = 0;
        }

      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("Erro ao ler o arquivo JSON", e);
      }
    } else {
      System.out.println("Arquivo JSON não encontrado. Iniciando com uma lista vazia.");
      ArrayList <UserPojo> userPojos = new ArrayList<UserPojo>();
      ArrayList<ProductPojo> productPojos = new ArrayList<>();
      int persistentCounter = 1;


    }
  }

  public  ArrayList<UserPojo> getUserPojos() {
    return userPojos;
  }

  public  void setUserPojos(ArrayList<UserPojo> userPojos) {
    userPojos = userPojos;
  }


  public  int getPersistentCounter() {

    return persistentCounter;
  }

  public void setPersistentCounter(int persistentCounter) {
    persistentCounter = persistentCounter;
  }





  /**
   * Classe interna para representar os dados persistidos.
   * Esta classe é usada para serializar e desserializar os dados para/do arquivo JSON.
   */
  public class PersistedData implements Serializable {
    public ArrayList<UserPojo> users;
    public ArrayList<ProductPojo> products;
    public int counter;

    public int getCounter() {
      return counter;
    }

    public void setCounter(int counter) {
      this.counter = counter;
    }

    public ArrayList<UserPojo> getUsers() {
      return users;
    }

    public void setUsers(ArrayList<UserPojo> users) {
      this.users = users;
    }

    public ArrayList<ProductPojo> getProducts() {
      return products;
    }

    public void setProducts(ArrayList<ProductPojo> products) {
      this.products = products;
    }

    public PersistedData() {}

    public PersistedData(ArrayList<UserPojo> users, int counter, ArrayList<ProductPojo> products) {
      this.users = users;
      this.counter = counter;
      this.products = products;
    }
  }
}
