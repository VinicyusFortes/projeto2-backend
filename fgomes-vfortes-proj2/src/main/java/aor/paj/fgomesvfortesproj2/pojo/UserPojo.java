package aor.paj.fgomesvfortesproj2.pojo;

public class UserPojo {
  private String username;
  private String password;
  private String image;

  public UserPojo() {
  }

  public UserPojo(String username, String password, String image) {
    this.username = username;
    this.password = password;
    this.image = image;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}

