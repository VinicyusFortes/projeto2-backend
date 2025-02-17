// UserBean.java
package pt.uc.dei.proj2.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pt.uc.dei.proj2.dto.UserDto;
import pt.uc.dei.proj2.pojo.UserPojo;

import java.io.Serializable;

@ApplicationScoped
public class UserBean implements Serializable {

    @Inject
    LoginBean loginBean;

    @Inject
    UtilityBean utilityBean;

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

//            int counter = utilityBean.getPersistentCounter();
            u = new UserPojo(user.getUsername(), user.getPassword(), user.getFirstName(),
                    user.getLastName(), user.getCellphone(), user.getEmail(),
                    user.getImage(), newId, user.getProdutos());
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
        UserDto ud = new UserDto(up.getUsername(), up.getPassword(), up.getFirstName(),
                up.getLastName(), up.getCellphone(), up.getEmail(),
                up.getImage(), up.getId(), up.getIdProdutos());
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

    public void addUser(UserPojo u) {
        utilityBean.getUserPojos().add(u);
    }
}
