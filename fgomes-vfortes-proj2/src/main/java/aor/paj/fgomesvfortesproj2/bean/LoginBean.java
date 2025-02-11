package aor.paj.fgomesvfortesproj2.bean;

import aor.paj.fgomesvfortesproj2.pojo.UserPojo;
import jakarta.enterprise.context.SessionScoped;

import java.io.Serializable;

@SessionScoped
public class LoginBean implements Serializable {
  UserPojo currentUserPojo;

  public LoginBean(){

  }
  public LoginBean(UserPojo currentUserPojo) {
    this.currentUserPojo = currentUserPojo;
  }

  public UserPojo getCurrentUser() {
    return currentUserPojo;
  }

  public void setCurrentUser(UserPojo currentUserPojo) {
    this.currentUserPojo = currentUserPojo;
  }
}
