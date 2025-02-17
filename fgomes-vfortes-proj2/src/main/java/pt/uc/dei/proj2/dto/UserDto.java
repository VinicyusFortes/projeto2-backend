package pt.uc.dei.proj2.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

@XmlRootElement
public class UserDto {
    private static int counter = 0;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String cellphone;
    private String email;
    private String image;
    private int id;
    private ArrayList<String> produtos;
    private String mensagemLogin;
//TODO lista de produtos inserida no array incorreto.

    public UserDto() {
        this.id = ++counter;
    }


    public UserDto(String username, String password, String firstName, String lastName, String cellphone, String email, String image, int id, ArrayList<String> idProdutos) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellphone = cellphone;
        this.email = email;
        this.image = image;
        this.id = id;
        this.produtos = idProdutos;
    }

    @XmlElement
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @XmlElement
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        UserDto.counter = counter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<String> produtos) {
        this.produtos = produtos;
    }

    public String getMensagemLogin() {
        return mensagemLogin;
    }

    public void setMensagemLogin(String mensagemLogin) {
        this.mensagemLogin = mensagemLogin;
    }
}
