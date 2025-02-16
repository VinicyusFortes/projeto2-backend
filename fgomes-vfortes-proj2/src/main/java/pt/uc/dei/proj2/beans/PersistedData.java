package pt.uc.dei.proj2.beans;

import jakarta.enterprise.context.ApplicationScoped;
import pt.uc.dei.proj2.pojo.ProductPojo;
import pt.uc.dei.proj2.pojo.UserPojo;

import java.io.Serializable;
import java.util.ArrayList;

@ApplicationScoped
public class PersistedData implements Serializable {
    // ALTERAÇÃO: Campos alterados de public para private
    private ArrayList<UserPojo> users;
    private ArrayList<ProductPojo> products;
    private int counter;

    // ALTERAÇÃO: Construtor padrão movido para o topo da classe
    public PersistedData() {
        // Construtor padrão necessário para CDI
    }

    // Sem alterações no construtor com parâmetros
    public PersistedData(ArrayList<UserPojo> users, int counter, ArrayList<ProductPojo> products) {
        this.users = users;
        this.counter = counter;
        this.products = products;
    }

    // Sem alterações nos getters e setters
    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public ArrayList<UserPojo> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserPojo> users) {
        this.users = users;
    }

    public ArrayList<ProductPojo> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductPojo> products) {
        this.products = products;
    }
}
