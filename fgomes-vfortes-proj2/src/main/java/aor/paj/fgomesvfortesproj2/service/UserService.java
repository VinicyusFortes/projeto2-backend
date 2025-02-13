package aor.paj.fgomesvfortesproj2.service;

import aor.paj.fgomesvfortesproj2.pt.uc.dei.proj2.UserBean;
import aor.paj.fgomesvfortesproj2.dto.UserDto;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/user")
public class UserService {
  @Inject
  UserBean userbean;

  @Context
  private HttpServletRequest request;

  //R3 - Register as user
  @POST
  @Path("/register")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerUser(UserDto user) {
    if (userbean.register(user.getUsername(), user.getPassword(), user.getImage())) {
      return Response.status(200).entity("The new user is registered").build();
    }
    return Response.status(200).entity("There is a user with the same username!").build();
  }

  //R1 - Login as user
  @POST
  @Path("/login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response login(UserDto user) {
    if (userbean.login(user.getUsername(), user.getPassword())) {
      return Response.status(200).entity("Login Successful!").build();
    }
    return Response.status(200).entity("Wrong Username or Password !").build();
  }

  //R2 - Logout as user
  @POST
  @Path("/logout")
  public Response logout() {
    HttpSession session = request.getSession();
    session.invalidate();
    return Response.status(200).entity("Logout Successful!").build();
  }

  //TODO continuar os metodos
  //R4 - Update user profile

  //R5 - Get user profile

  @GET
  @Path("/getuser")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser() {
    UserDto u = userbean.getLoggeduser();
    if (u != null)
      return Response.status(200).entity(userbean.getLoggeduser()).build();
    else
      return Response.status(200).entity("there is no user logged in at the moment!").build();
  }

  //todo continuar a fazer o metodo
  //R6 - List products of a user
  @GET
  @Path("/{username}/products")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response listarProdutosUser(UserDto user) {
    if (userbean.register(user.getUsername(), user.getPassword(), user.getImage())) {
       return Response.status(200).entity("The new user is registered").build();
    }
    return Response.status(200).entity("There is a user with the same username!").build();
  }

  //R8 - Add products to user products
  @POST
  @Path("/{username}/products")
  public Response adicionarProduto() {
    return Response.status(200).entity("The new user is registered").build();
  }

  //R9 Update product of user products
  @POST
  @Path("/{username}/products/{ProductsId}")
  public Response atualizarProdutosUser() {
    return Response.status(200).entity("The new user is registered").build();
  }

  //R10 Delete product of user products
  @DELETE
  @Path("/{username}/products/{productId}")
  public Response apagarProdutoUser() {
    return Response.status(200).entity("The new user is registered").build();
  }

}
