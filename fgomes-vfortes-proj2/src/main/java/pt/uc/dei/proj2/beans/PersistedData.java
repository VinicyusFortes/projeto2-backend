package pt.uc.dei.proj2.beans;

import jakarta.enterprise.context.ApplicationScoped;
import pt.uc.dei.proj2.pojo.ProductPojo;
import pt.uc.dei.proj2.pojo.UserPojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PersistedData implements Serializable {
    // ALTERAÇÃO: Campos alterados de public para private
    private List<UserPojo> users;
    private List<ProductPojo> products; //FIXME: remove
    private int counter;

    // ALTERAÇÃO: Construtor padrão movido para o topo da classe
    public PersistedData() {
        // Construtor padrão necessário para CDI
    }

    // Sem alterações no construtor com parâmetros
    public PersistedData(List<UserPojo> users, List<ProductPojo> products) {
        this.users = users;
        /*
        for(ProductPojo productPojo : products){
            int id = productPojo.getAnuncianteId();
            for(UserPojo userPojo : users){
                if(userPojo.getId() == id){
                    userPojo.getProductPojosList().add(productPojo);
                    break;
                }
            }
        }*/
    }

    // Sem alterações nos getters e setters
    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public List<UserPojo> getUsers() {
        return users;
    }



    public void setUsers(ArrayList<UserPojo> users) {
        this.users = users;
    }

    public List<ProductPojo> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductPojo> products) {
        this.products = products;
    }
}
