package pt.uc.dei.proj2.beans;

import jakarta.enterprise.context.ApplicationScoped;
import pt.uc.dei.proj2.pojo.ProductPojo;

import java.io.Serializable;
import java.util.ArrayList;

@ApplicationScoped
public class ProductBean implements Serializable {
  private static ArrayList<ProductPojo> productPojos = new ArrayList<>();
  private static int persistentCounter = 1;

}
