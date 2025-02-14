package pt.uc.dei.proj2.beans;

import pt.uc.dei.proj2.dto.UserDto;
import pt.uc.dei.proj2.pojo.ProductPojo;
import pt.uc.dei.proj2.pojo.UserPojo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletContext;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Bean responsável por gerenciar operações relacionadas a usuários.
 * Esta classe lida com login, registro, persistência e recuperação de dados de usuários.
 */
@ApplicationScoped
public class UserBean implements Serializable {
    // Provedor de contexto do servlet
    private jakarta.inject.Provider<ServletContext> servletContext;

    // Lista estática para armazenar os objetos UserPojo
    private static ArrayList<UserPojo> userPojos = new ArrayList<>();
    private static ArrayList<ProductPojo> productPojos = new ArrayList<>();

    // Contador persistente para gerar IDs únicos de usuários
    private static int persistentCounter = 1;

    // Injeção do bean de login
    @Inject
    LoginBean loginBean;

    /**
     * Construtor da classe UserBean.
     * Inicializa o bean carregando os usuários do arquivo JSON.
     */
    public UserBean() {
        loadUsersFromJson();
    }

    /**
     * Realiza o login do usuário.
     * @param username Nome de usuário
     * @param password Senha do usuário
     * @return true se o login for bem-sucedido, false caso contrário
     */
    public boolean login(String username, String password) {
        UserPojo u = UserBean.getUser(username, password);
        if (u != null) {
            loginBean.setCurrentUser(u);
            return true;
        } else
            return false;
    }

    /**
     * Registra um novo usuário.
     * Utiliza o persistentCounter para atribuir um ID único ao novo usuário.
     * @param user Objeto UserDto contendo os dados do novo usuário
     * @return true se o registro for bem-sucedido, false caso contrário
     */
    public boolean register(UserDto user) {
        UserPojo u = UserBean.getUser(user.getUsername(), user.getPassword());
        if (u == null) {
            // Cria um novo UserPojo com o ID baseado no persistentCounter
            u = new UserPojo(user.getUsername(), user.getPassword(), user.getFirstName(),
                    user.getLastName(), user.getCellphone(), user.getEmail(),
                    user.getImage(), ++persistentCounter, user.getIdProdutos());
            UserBean.addUser(u);
            writeIntoJsonFile(); // Persiste os dados após adicionar um novo usuário
            return true;
        } else
            return false;
    }

    /**
     * Retorna o usuário atualmente logado.
     * @return UserDto do usuário logado ou null se nenhum usuário estiver logado
     */
    public UserDto getLoggeduser() {
        UserPojo u = loginBean.getCurrentUser();
        if (u != null){
            System.out.println("id:" + u.getId());
            return convertUserPojoToUserDto(u);
        }
        else return null;
    }

    /**
     * Converte um UserPojo para UserDto.
     * @param up UserPojo a ser convertido
     * @return UserDto convertido
     */
    private UserDto convertUserPojoToUserDto(UserPojo up) {
        UserDto ud = new UserDto(up.getUsername(), up.getPassword(), up.getFirstName(),
                up.getLastName(), up.getCellphone(), up.getEmail(),
                up.getImage(), up.getId(), up.getIdProdutos());
        System.out.println("converted id" +  up.getId());
        return ud;
    }

    /**
     * Escreve os dados dos usuários e o contador persistente em um arquivo JSON.
     * Esta função salva tanto a lista de usuários quanto o valor atual do contador.
     */
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
            PersistedData data = new PersistedData(userPojos, persistentCounter);

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
    public void loadUsersFromJson() {
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
                    userPojos = data.users;
                    persistentCounter = data.counter;

                    System.out.println("Usuários e contador carregados do JSON com sucesso!");
                    System.out.println(userPojos.toString());
                } else {
                    System.out.println("O arquivo JSON está vazio. Iniciando com uma lista vazia.");
                    userPojos = new ArrayList<>();
                    persistentCounter = 0;
                }

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Erro ao ler o arquivo JSON", e);
            }
        } else {
            System.out.println("Arquivo JSON não encontrado. Iniciando com uma lista vazia.");
            userPojos = new ArrayList<>();
            persistentCounter = 0;
        }
    }

    /**
     * Busca um usuário pelo nome de usuário e senha.
     * @param username Nome de usuário
     * @param password Senha do usuário
     * @return UserPojo se encontrado, null caso contrário
     */
    public static UserPojo getUser(String username, String password) {
        for (UserPojo u : userPojos) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password))
                return u;
        }
        return null;
    }

    /**
     * Adiciona um novo usuário à lista de usuários.
     * @param u UserPojo a ser adicionado
     */
    public static void addUser(UserPojo u) {
        userPojos.add(u);
    }

    /**
     * Classe interna para representar os dados persistidos.
     * Esta classe é usada para serializar e desserializar os dados para/do arquivo JSON.
     */
    public static class PersistedData implements Serializable {
        public ArrayList<UserPojo> users;
        public int counter;

        public int getCounter() {
            return counter;
        }

        public void setCounter(int counter) {
            this.counter = counter;
        }

        public PersistedData() {}

        public PersistedData(ArrayList<UserPojo> users, int counter) {
            this.users = users;
            this.counter = counter;
        }
    }
}
