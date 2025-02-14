package pt.uc.dei.proj2.beans;
import pt.uc.dei.proj2.beans.UtilityBean;
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

    // Injeção do bean de login
    @Inject
    LoginBean loginBean;

    @Inject
    UtilityBean utilityBean;

    /**
     * Construtor da classe UserBean.
     * Inicializa o bean carregando os usuários do arquivo JSON.
     */
    public UserBean() {
    }

    /**
     * Realiza o login do usuário.
     * @param username Nome de usuário
     * @param password Senha do usuário
     * @return true se o login for bem-sucedido, false caso contrário
     */
    public boolean login(String username, String password) {
        UserPojo u = getUser(username, password);
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
        UserPojo u = getUser(user.getUsername(), user.getPassword());
        if (u == null) {
            int counter = utilityBean.getPersistentCounter();
            // Cria um novo UserPojo com o ID baseado no persistentCounter
            u = new UserPojo(user.getUsername(), user.getPassword(), user.getFirstName(),
                    user.getLastName(), user.getCellphone(), user.getEmail(),
                    user.getImage(), ++counter, user.getIdProdutos());
            addUser(u);
            utilityBean.writeIntoJsonFile(); // Persiste os dados após adicionar um novo usuário
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
     * Busca um usuário pelo nome de usuário e senha.
     * @param username Nome de usuário
     * @param password Senha do usuário
     * @return UserPojo se encontrado, null caso contrário
     */
    public UserPojo getUser(String username, String password) {
        for (UserPojo u : utilityBean.getUserPojos()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password))
                return u;
        }
        return null;
    }

    /**
     * Adiciona um novo usuário à lista de usuários.
     * @param u UserPojo a ser adicionado
     */
    public void addUser(UserPojo u) {
        utilityBean.getUserPojos().add(u);
    }


}
