// ProductBean.java
package pt.uc.dei.proj2.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import pt.uc.dei.proj2.dto.ProductDto;
import pt.uc.dei.proj2.pojo.ProductPojo;

import java.io.Serializable;
import java.util.ArrayList;

@ApplicationScoped
public class ProductBean implements Serializable {

  private ArrayList<ProductPojo> productPojos = new ArrayList<>();
  private int persistentCounter = 1;

  @Inject
  private UtilityBean utilityBean;

  //Você pode remover o construtor sem argumentos se não for necessário
  public ProductBean() {
  }

  public boolean adicionarProdutoAoArray(ProductDto produto) {
    ProductPojo p = new ProductPojo(produto.getIdProduto(), produto.getTitulo(), produto.getDescricao(), produto.getLocalizacao(), produto.getData(), produto.getAnuncianteId(), produto.getCategoria(), produto.getPreco(), produto.getImagemProduto(), produto.getStateId());
    System.out.println("tentativa de criar produto");
    productPojos.add(p);
    System.out.println("vou tentar escrever no ficheiro");
    utilityBean.writeIntoJsonFile();
    System.out.println("produto " + p.getTitulo() + " criado");
    return true;
  }

  public ArrayList<ProductPojo> getProductPojos() {
    return productPojos;
  }

  public void setProductPojos(ArrayList<ProductPojo> productPojos) {
    this.productPojos = productPojos;
  }

  public int getPersistentCounter() {
    return persistentCounter;
  }

  public void setPersistentCounter(int persistentCounter) {
    this.persistentCounter = persistentCounter;
  }

  private ProductDto convertProductPojoToProductDto(ProductPojo pp) {
    ProductDto pd = new ProductDto(pp.getIdProduto(), pp.getTitulo(), pp.getDescricao(), pp.getLocalizacao(), pp.getData(), pp.getAnuncianteId(), pp.getCategoria(), pp.getPreco(), pp.getImagemProduto(), pp.getStateId());
    return pd;
  }
}
