package pt.uc.dei.proj2.pojo;

import java.util.ArrayList;
import java.util.List;

public class UserPojo {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String cellphone;
    private List<ProductPojo> products = new ArrayList<>();

    private String email;
    private String image;
    private int id;

    public UserPojo() {
    }

    //os parametros deste construtor que de coincidir com os parametros do userdto e com qualquer request que envolva criar ou atualizar utilizador no userbean.java(register() e convertUserPojoToUserDto())
    public UserPojo(String username, String password, String firstName, String lastName, String cellphone, String email, String image, int id, List<ProductPojo> products) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cellphone = cellphone;
        this.email = email;
        this.image = image;
        this.id = id;
        this.products = products;
    }
//TODO CORRIGIR PROBLEMA DE ARRAYS LISTA PRODTOS

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ProductPojo> getProducts() {
        return products;
    }

    public void setProducts(List<ProductPojo> produtos) {
        this.products = produtos;
    }

    public void addProduct(ProductPojo pj) {
        this.products.add(pj);
    }
}

