// ProductBean.java
package pt.uc.dei.proj2.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pt.uc.dei.proj2.dto.ProductDto;
import pt.uc.dei.proj2.pojo.ProductPojo;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

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

  public void adicionarProdutoAoUtilizador(ProductDto produto) {
    ProductPojo p = new ProductPojo(produto.getIdProduto(), produto.getTitulo(), produto.getDescricao(), produto.getLocalizacao(), produto.getData(), userBean.getLoggeduser().getId(), produto.getCategoria(), produto.getPreco(), produto.getImagemProduto(), produto.getStateId());
    try{
      productPojos.add(p);
      utilityBean.writeIntoJsonFile();
    } catch(Exception e){
      e.printStackTrace();
    }
    System.out.println("produto " + p.getTitulo() + " criado");
  }

  public ArrayList<ProductPojo> getProductPojos() {
    return productPojos;
  }

  public void setProductPojos(ArrayList<ProductPojo> productPojos) {
    this.productPojos = productPojos;
  }



  private ProductDto convertProductPojoToProductDto(ProductPojo pp) {
    ProductDto pd = new ProductDto(pp.getTitulo(), pp.getDescricao(), pp.getLocalizacao(), pp.getData(), pp.getAnuncianteId(), pp.getCategoria(), pp.getPreco(), pp.getImagemProduto(), pp.getStateId());
    return pd;
  }
}
