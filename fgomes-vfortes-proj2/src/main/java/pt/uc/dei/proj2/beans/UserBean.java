// UserBean.java
package pt.uc.dei.proj2.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pt.uc.dei.proj2.dto.ProductDto;
import pt.uc.dei.proj2.dto.UserDto;
import pt.uc.dei.proj2.pojo.ProductPojo;
import pt.uc.dei.proj2.pojo.UserPojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserBean implements Serializable {

    @Inject
    LoginBean loginBean;

    @Inject
    UtilityBean utilityBean;
    @Inject
    private ProductBean productBean;

    @PostConstruct
    public void init() {
        utilityBean.loadDataFromJson();
    }

    public boolean login(String username, String password) {
        UserPojo u = getUser(username, password);
        if (u != null) {
            loginBean.setCurrentUser(u);
            return true;
        } else
            return false;
    }

    // TODO verificar porque é que o id do user é sempre definido como 5 quando se regista um utilizador
    public boolean register(UserDto user) {
        UserPojo u = getUser(user.getUsername(), user.getPassword());
        if (u == null) {
            int newId = utilityBean.getUserPojos().size() + 1;

            u = new UserPojo(user.getUsername(), user.getPassword(), user.getFirstName(),
                    user.getLastName(), user.getCellphone(), user.getEmail(),
                    user.getImage(), newId, new ArrayList<>());
            addUser(u);
            System.out.println("New user Id:" + newId);
            utilityBean.writeIntoJsonFile(); // Persiste os dados após adicionar um novo usuário
            return true;
        } else
            return false;
    }

    public UserDto getLoggeduser() {
        UserPojo u = loginBean.getCurrentUser();
        if (u != null) {
            System.out.println("id:" + u.getId());
            return convertUserPojoToUserDto(u);
        } else return null;
    }

    private UserDto convertUserPojoToUserDto(UserPojo up) {
        List<ProductDto> productDtos = new ArrayList<>();

        //Utilizando a lista vazia de DTOs, faz a conversão singularmente de ProductDTO para ProductPojo
        //para em seguida enviar correctamente no conscrutor do UserDTO
        for (ProductPojo productPojo : up.getProductPojosList()) {
            ProductDto productDto = productBean.convertProductPojoToProductDto(productPojo);
            productDtos.add(productDto);
        }
        UserDto ud = new UserDto(up.getUsername(), up.getPassword(), up.getFirstName(),
                up.getLastName(), up.getCellphone(), up.getEmail(),
                up.getImage(), up.getId(), productDtos);
        System.out.println("converted id" + up.getId());
        return ud;
    }

    public UserPojo getUser(String username, String password) {
        for (UserPojo u : utilityBean.getUserPojos()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password))
                return u;
        }
        return null;
    }

    public UserPojo getUserByUsername(String username) {
        for (UserPojo u : utilityBean.getUserPojos()) {
            if (u.getUsername().equals(username))
                return u;
            System.out.println("retorno do metodo: " + u);
        }
        return null;
    }

    public void addUser(UserPojo u) {
        utilityBean.getUserPojos().add(u);
    }


    /**
     * Atualiza os dados de um utilizador existente.
     * <p>
     * Este mét0do procura um utilizador na lista de utilizadores com base no username
     * do utilizador fornecido. Se encontrado, substitui o utilizador existente pelos
     * novos dados e persiste as alterações no ficheiro JSON.
     *
     * @param userAtualizado O objeto UserPojo contendo os dados atualizados do utilizador.
     *                       O username deste objeto é usado para identificar o utilizador a ser atualizado.
     * @return boolean Retorna true se o utilizador foi encontrado e atualizado com sucesso,
     * false se o utilizador não foi encontrado ou se ocorreu um erro durante a atualização.
     */


    public boolean atualizarUser(UserPojo userAtualizado) {
        for (int i = 0; i < utilityBean.getUserPojos().size(); i++) {
            UserPojo u = utilityBean.getUserPojos().get(i);
            if (u.getUsername().equals(userAtualizado.getUsername())) {
                utilityBean.getUserPojos().set(i, userAtualizado);
                System.out.println("Vou tentar escrever a 1a vez...");
                utilityBean.writeIntoJsonFile(); // Persiste as alterações
                System.out.println("Escrita 1a vez com sucesso");
                return true;
            }
        }
        return false;
    }
}
