package pt.uc.dei.proj2.service;

import pt.uc.dei.proj2.beans.UserBean;
import pt.uc.dei.proj2.dto.UserDto;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/users")
public class UserService {
  @Inject
  UserBean userbean;

  @Context
  private HttpServletRequest request;
  @Inject
  private UserBean userBean;

  //R3 - Register as user
  @POST
  @Path("/register")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerUser(UserDto user) {
    if (userbean.register(user)) {
      System.out.println("id:" + UserDto.getCounter());
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
    HttpSession session = request.getSession(); // Use false para não criar uma nova sessão
    if (session != null) {
      session.invalidate();
      return Response.status(200).entity("Logout Successful!").build();
    } else {
      return Response.status(400).entity("R2: Erro: Não há usuário logado.").build();
    }
  }


  //TODO continuar os metodos
//R4 - Update user profile
//  @POST
//  @Path("/{username}")  // Caminho do método
//  @Produces(MediaType.APPLICATION_JSON)
//  public Response atualizarPerfil(@PathParam("username") String username) {
//    UserDto u = userbean.getLoggeduser();
//
//    if (u != null) { // Verifica se o usuário está logado
//      String usernameRegistado = u.getUsername();
//      if (username.equals(usernameRegistado)) {
//        return Response.status(200).entity("R4. Perfil de " + username + " atualizado").build();
//      } else {
//        return Response.status(403).entity("Erro: Não é possível atualizar o perfil de outro usuário.").build();
//      }
//    } else {
//      // O usuário não está logado, portanto, retorna erro
//      return Response.status(400).entity("R4. Não existe utilizador logado").build();
//    }
//  }


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
    if (userbean.register(user)) {
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
