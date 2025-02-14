package pt.uc.dei.proj2.beans;

import jakarta.enterprise.context.ApplicationScoped;
import pt.uc.dei.proj2.dto.ProductDto;
import pt.uc.dei.proj2.pojo.ProductPojo;

import java.io.Serializable;
import java.util.ArrayList;

@ApplicationScoped
public class ProductBean implements Serializable {
  private static ArrayList<ProductPojo> productPojos = new ArrayList<>();
  private static int persistentCounter = 1;


  private ProductDto convertProductPojoToProductDto(ProductPojo pp) {
    ProductDto pd = new ProductDto(pp.getIdProduto(), pp.getTitulo(), pp.getDescricao(), pp.getLocalizacao(), pp.getData(), pp.getAnuncianteId(), pp.getCategoria(), pp.getPreco(), pp.getImagemProduto(), pp.getStateId());
    return pd;
  }



}
