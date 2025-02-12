package aor.paj.fgomesvfortesproj2.bean;

import aor.paj.fgomesvfortesproj2.dto.UserDto;
import aor.paj.fgomesvfortesproj2.pojo.UserPojo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@RequestScoped
public class UserBean implements Serializable {
  private jakarta.inject.Provider<ServletContext> servletContext;
  private static ArrayList<UserPojo> userPojos = new ArrayList<>();

  @Inject LoginBean loginBean;

  public boolean login(String username, String password){
    UserPojo u = UserBean.getUser(username,password);
    if(u!= null){
      loginBean.setCurrentUser(u);
      return true;
    }
    else
      return false;
  }

  public boolean register(String username, String password){
    //TODO retirar
    System.out.println("Registando utilizador: " + username);
    System.out.println("password: " + password);
    UserPojo u = UserBean.getUser(username, password);
    if (u==null){
      u= new UserPojo(username,password);
      userPojos.add(u);
      UserBean.addUser(u);
      return true;
    }else
      return false;
  }

  public UserDto getLoggeduser(){
    UserPojo u = loginBean.getCurrentUser();
    if(u!= null)
      return converUserPojoToUserDto(u);
    else return null;
  }

  private UserDto converUserPojoToUserDto(UserPojo up){
    UserDto ud = new UserDto(up.getUsername(), up.getPassword());
    return ud;
  }

  public void writeIntoJsonFile(ArrayList<UserDto> users) {
    Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
    String realPath = servletContext.get().getRealPath("/WEB-INF/data/utilizadores.json");
    Path filePath = Paths.get(realPath);
    try {
      Files.createDirectories(filePath.getParent());
      try (OutputStream os = Files.newOutputStream(filePath)) {
        jsonb.toJson(users, os);
        System.out.println("Arquivo JSON criado com sucesso");
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Erro ao escrever o arquivo JSON", e);
    }
  }


  public static UserPojo getUser(String username, String password){
    for(UserPojo u: userPojos){
      if(u.getUsername().equals(username) && u.getPassword().equals(password))
        return u;
    }
    return null;
  }

  public static void addUser(UserPojo u) {
    userPojos.add(u);
  }
}
