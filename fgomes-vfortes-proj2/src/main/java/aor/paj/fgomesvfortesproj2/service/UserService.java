package aor.paj.fgomesvfortesproj2.service;

import aor.paj.fgomesvfortesproj2.bean.ActivityBean;
import aor.paj.fgomesvfortesproj2.bean.UserBean;
import aor.paj.fgomesvfortesproj2.dto.UserDto;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;

@Path("/user")
public class UserService {
  @Inject
  UserBean userbean;

  @Context
  private HttpServletRequest request;

  @POST
  @Path("/register")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response registerUser(UserDto user) {
    if (userbean.register(user.getUsername(), user.getPassword())) {
      //TODO retirar
      System.out.println(user.getUsername());
      System.out.println(user.getPassword());
      return Response.status(200).entity("The new user is registered").build();
    }
    return Response.status(200).entity("There is a user with the same username!").build();
  }

  @POST
  @Path("/login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response login(UserDto user) {
    if (userbean.login(user.getUsername(), user.getPassword())) {
      return Response.status(200).entity("Login Successful!").build();
    }
    System.out.println("username" +  user.getUsername());
    System.out.println("senha" +  user.getPassword());
    return Response.status(200).entity("Wrong Username or Password !").build();
  }

  @POST
  @Path("/logout")
  public Response logout() {
    HttpSession session = request.getSession();
    session.invalidate();
    return Response.status(200).entity("Logout Successful!").build();
  }

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
}
