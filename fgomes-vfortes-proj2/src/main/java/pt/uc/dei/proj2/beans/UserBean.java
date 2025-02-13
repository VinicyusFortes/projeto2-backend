package pt.uc.dei.proj2.beans;

import pt.uc.dei.proj2.dto.UserDto;
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

@ApplicationScoped
public class UserBean implements Serializable {
    private jakarta.inject.Provider<ServletContext> servletContext;
    private static ArrayList<UserPojo> userPojos = new ArrayList<>();

    public UserBean() {
        loadUsersFromJson();
    }

    @Inject
    LoginBean loginBean;

    public boolean login(String username, String password) {
        UserPojo u = UserBean.getUser(username, password);
        if (u != null) {
            loginBean.setCurrentUser(u);
            return true;
        } else
            return false;
    }

    public boolean register(UserDto user) {

        UserPojo u = UserBean.getUser(user.getUsername(), user.getPassword());
        if (u == null) {
            int counter = UserDto.getCounter();
            u = new UserPojo(user.getUsername(), user.getPassword(),user.getFirstName(), user.getLastName(), user.getCellphone(), user.getEmail(), user.getImage(), counter);
            UserBean.addUser(u);
            writeIntoJsonFile();
            return true;
        } else
            return false;
    }

    public UserDto getLoggeduser() {
        UserPojo u = loginBean.getCurrentUser();
        System.out.println("id:" + u.getId());
        if (u != null)
            return convertUserPojoToUserDto(u);
        else return null;
    }

    private UserDto convertUserPojoToUserDto(UserPojo up) {
        UserDto ud = new UserDto(up.getUsername(), up.getPassword(),up.getFirstName(), up.getLastName(), up.getCellphone(), up.getEmail(), up.getImage(), UserDto.getCounter());
        System.out.println("converted id" +  up.getId());
        return ud;
    }


    public void writeIntoJsonFile() {
        // Cria o objeto Jsonb para serializar o objeto em JSON
        Jsonb jsonb = JsonbBuilder.create();

        // Defina o caminho da pasta 'data' dentro do seu projeto
        String projectRoot = System.getProperty("user.home");
        Path directoryPath = Path.of(projectRoot + File.separator + "data");

        Path fPath = Path.of(directoryPath + File.separator + "utilizadores.json");
        System.out.println("banana: " + fPath);
        System.out.println(projectRoot);


        // Crie a pasta 'data' caso ela não exista
        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);  // Cria a pasta 'data'
                System.out.println("Pasta 'data' criada com sucesso!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar a pasta 'data'", e);
        }


        // Escreva o arquivo JSON
        try (OutputStream os = Files.newOutputStream(fPath)) {
            jsonb.toJson(userPojos, os);  // Serializa o ArrayList<UserDto> em JSON e grava no arquivo
            System.out.println("Arquivo JSON criado com sucesso em: " + fPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao escrever o arquivo JSON", e);
        }
    }

    public static UserPojo getUser(String username, String password) {
        for (UserPojo u : userPojos) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password))
                return u;
        }
        return null;
    }

    public static void addUser(UserPojo u) {
        userPojos.add(u);
    }

    public void loadUsersFromJson() {
        String projectRoot = System.getProperty("user.home");
        Path directoryPath = Path.of(projectRoot + File.separator + "data");

        Path filePath = Path.of(directoryPath + File.separator + "utilizadores.json");

        System.out.println("A carregar ficheiro de utilizadores");

        if (Files.exists(filePath)) {
            try {
                Jsonb jsonb = JsonbBuilder.create();
                String content = Files.readString(filePath);

                // Converte o JSON de volta para a lista de usuários
                userPojos = jsonb.fromJson(content, new ArrayList<UserPojo>() {
                }.getClass().getGenericSuperclass());

                System.out.println("Usuários carregados do JSON com sucesso!");

                System.out.println(userPojos.toString());

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Erro ao ler o arquivo JSON", e);
            }
        }
    }

}
