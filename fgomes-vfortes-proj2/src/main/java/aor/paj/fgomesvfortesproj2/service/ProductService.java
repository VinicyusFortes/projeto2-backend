package aor.paj.fgomesvfortesproj2.service;

import aor.paj.fgomesvfortesproj2.dto.UserDto;
import aor.paj.fgomesvfortesproj2.pt.uc.dei.proj2.UserBean;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/products")
public class ProductService {
  @Inject
  UserBean userbean;

  @Context
  private HttpServletRequest request;


  //R7 - List all products
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  public Response listarTodosProdutos(UserDto user) {
    if (userbean.register(user.getUsername(), user.getPassword(), user.getImage())) {
      return Response.status(200).entity("The new user is registered").build();
    }
    return Response.status(200).entity("There is a user with the same username!").build();
  }



}
