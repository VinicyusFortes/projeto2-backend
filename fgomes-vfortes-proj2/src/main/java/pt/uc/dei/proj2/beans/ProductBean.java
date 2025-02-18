// ProductBean.java
package pt.uc.dei.proj2.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import pt.uc.dei.proj2.dto.MessageDTO;
import pt.uc.dei.proj2.dto.ProductDto;
import pt.uc.dei.proj2.dto.UserDto;
import pt.uc.dei.proj2.pojo.ProductPojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

@ApplicationScoped
public class ProductBean implements Serializable {

    private ArrayList<ProductPojo> productPojos = new ArrayList<>();

    @Inject
    private UtilityBean utilityBean;

    @Inject
    private UserBean userBean;

    //Você pode remover o construtor sem argumentos se não for necessário
    public ProductBean() {
    }

    public MessageDTO adicionarProdutoAoUtilizador(ProductDto produto, UserDto u) {
        UserDto loggedUser = userBean.getLoggeduser();

        if (u == null || loggedUser == null || !u.getUsername().equals(loggedUser.getUsername()) || u.getId() != loggedUser.getId()) {
            String message = "O utilizador indicado não está ligado.";

            //Garante que o objecto retornado (utilizador ligado) não está como null
            Objects.requireNonNull(loggedUser);
            JsonObject loggedUserJSON = Json.createObjectBuilder()
                    .add("id", loggedUser.getId())
                    .add("username", loggedUser.getUsername()).build();

            return new MessageDTO(message, loggedUserJSON);
        } else {
            ProductPojo p = new ProductPojo(produto.getIdProduto(), produto.getTitulo(), produto.getDescricao(), produto.getLocalizacao(), produto.getData(), userBean.getLoggeduser().getId(), produto.getCategoria(), produto.getPreco(), produto.getImagemProduto(), produto.getStateId());
            String message = null;
            JsonObject createdProductJSON = null;
            try {
                productPojos.add(p);
                utilityBean.writeIntoJsonFile();
                System.out.println("produto " + p.getTitulo() + " criado");

                message = "O produto foi criado com sucesso";

                //Garante que o objecto retornado (utilizador ligado) não está como null
                Objects.requireNonNull(loggedUser);
                createdProductJSON = utilityBean.dtoToJSON(produto);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(message);
            Objects.requireNonNull(createdProductJSON);
            return new MessageDTO(message, createdProductJSON);
        }
    }

    public ArrayList<ProductPojo> getProductPojos() {
        return productPojos;
    }

    public void setProductPojos(ArrayList<ProductPojo> productPojos) {
        this.productPojos = productPojos;
    }


    public ProductDto convertProductPojoToProductDto(ProductPojo pp) {
        return new ProductDto(pp.getTitulo(), pp.getDescricao(), pp.getLocalizacao(), pp.getData(), pp.getAnuncianteId(), pp.getCategoria(), pp.getPreco(), pp.getImagemProduto(), pp.getStateId(), pp.getIdProduto());
    }
}
