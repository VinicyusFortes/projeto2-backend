package pt.uc.dei.proj2.service;

import pt.uc.dei.proj2.beans.ProductBean;
import pt.uc.dei.proj2.dto.ProductDto;
import pt.uc.dei.proj2.dto.UserDto;
import pt.uc.dei.proj2.beans.UserBean;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;


@Path("/products")
public class ProductService {
  @Inject
  UserBean userbean;

  @Context
  private HttpServletRequest request;
  @Inject
  private ProductBean productBean;

  //todo: terminar metodo
  //R7 - List all products
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response listarTodosProdutos() {
    return Response.status(200).entity(productBean.getAllProducts()).build();

  }



}
