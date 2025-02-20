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
import pt.uc.dei.proj2.pojo.UserPojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ProductBean implements Serializable {

    private ArrayList<ProductPojo> productPojos = new ArrayList<>(); //FIXME: Delete

    @Inject
    private UtilityBean utilityBean;

    @Inject
    private UserBean userBean;

    public ProductBean() {
    }

    public MessageDTO adicionarProdutoAoUtilizador(ProductDto produto, UserDto u) {
        UserDto loggedUser = userBean.getLoggeduser();
        ProductPojo pj = new ProductPojo(
                produto.getIdProduto(),
                produto.getTitulo(),
                produto.getDescricao(),
                produto.getLocalizacao(),
                produto.getData(),
                produto.getAnuncianteId(),
                produto.getCategoria(),
                produto.getPreco(),
                produto.getImagemProduto(),
                produto.getStateId()
        );

        if (u == null || loggedUser == null || !u.getUsername().equals(loggedUser.getUsername()) || u.getId() != loggedUser.getId()) {
            String message = "O utilizador indicado não está ligado.";

            //Garante que o objecto retornado (utilizador ligado) não está como null
            Objects.requireNonNull(loggedUser);
            JsonObject loggedUserJSON = Json.createObjectBuilder()
                    .add("id", loggedUser.getId())
                    .add("username", loggedUser.getUsername()).build();

            return new MessageDTO(message, loggedUserJSON);
        } else {

            // Fetch user from presistedData
            for (UserPojo userPojo: utilityBean.getUserPojos()) {
                if (userPojo.getId() == u.getId()) {
                    userPojo.addProduct(pj);
                    utilityBean.writeIntoJsonFile();

                    JsonObject createdProductJSON = utilityBean.dtoToJSON(produto);
                    Objects.requireNonNull(createdProductJSON);
                    return new MessageDTO("O produto foi criado com sucesso", createdProductJSON);
                }
            }

            return new MessageDTO("Nao foi possível criar o produto", null);
        }
    }

    public ArrayList<ProductPojo> getProducts() {
        ArrayList<ProductPojo> products = new ArrayList<>();
        for (UserPojo userPojo: utilityBean.getUserPojos())
            for (ProductPojo p: userPojo.getProducts())
                products.add(p);

        return products;
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

    public ArrayList<ProductDto> convertProductPojoListToProductDtoList(List<ProductPojo> productPojos) {
        ArrayList<ProductDto> productDtos = new ArrayList<>();

        for (ProductPojo pp : productPojos) {
            ProductDto dto = convertProductPojoToProductDto(pp);
            productDtos.add(dto);
        }

        return productDtos;
    }

}
