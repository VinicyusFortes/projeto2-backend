package pt.uc.dei.proj2.service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.json.JSONObject;
import pt.uc.dei.proj2.beans.ProductBean;
import pt.uc.dei.proj2.beans.UserBean;
import pt.uc.dei.proj2.beans.UtilityBean;
import pt.uc.dei.proj2.dto.MessageDTO;
import pt.uc.dei.proj2.dto.ProductDto;
import pt.uc.dei.proj2.dto.UserDto;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.uc.dei.proj2.pojo.ProductPojo;
import pt.uc.dei.proj2.pojo.UserPojo;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


@Path("/users")
public class UserService {
    @Inject
    UserBean userbean;

    @Context
    private HttpServletRequest request;
    @Inject
    private UtilityBean utilityBean;
    @Inject
    private ProductBean productBean;


    //R1 - Login as user
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserDto user) {
        if (userbean.login(user.getUsername(), user.getPassword())) {
            String message = "R1. Login feito!";
            UserDto u = userbean.getLoggeduser();
            JsonObject loggedUser = Json.createObjectBuilder()
                    .add("id", u.getId())
                    .add("username", u.getUsername()).build();

            MessageDTO messageDTO = new MessageDTO(message, loggedUser);

            return Response.status(200).entity(messageDTO).build();
        }
        return Response.status(200).entity("R1. username e/ou password errados!").build();
    }

    //R2 - Logout as user
    @POST
    @Path("/logout")
    public Response logout() {
        HttpSession session = request.getSession(); // Use false para não criar uma nova sessão
        if (session != null) {
            session.invalidate();
            return Response.status(200).entity("R2: Logout Successful!").build();
        } else {
            return Response.status(400).entity("R2: Erro: Não há usuário logado.").build();
        }
    }

    //R3 - Register as user
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(UserDto user) {
        if (userbean.register(user)) {
            return Response.status(200).entity("R3. The new user is registered").build();
        }
        return Response.status(200).entity("R3. There is a user with the same username!").build();
    }


    //TODO continuar os metodos
//R4 - Update user profile
    @PUT
    @Path("/{username}")  // Caminho do método
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarPerfil(@PathParam("username") String username) {
        System.out.println("username " + username);
        return Response.status(200).entity("R4. Perfil de " + username + " atualizado").build();
    }


    //R5 - Get user profile
    @GET
    @Path("/getuser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser() {
        UserDto u = userbean.getLoggeduser();
        if (u != null)
            return Response.status(200).entity(userbean.getLoggeduser()).build();
        else
            return Response.status(200).entity("R5. there is no user logged in at the moment!").build();
    }

    @GET
    @Path("/allusers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        utilityBean.loadDataFromJson();
        return Response.status(200).entity("zabszbasidjeasd").build();
    }

    //todo continuar a fazer o metodo
    //R6 - List products of a user
    @GET
    @Path("/{username}/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarProdutosUser(@PathParam("username") String username) {
        UserDto u = userbean.getLoggeduser();
        if (u != null) {

            return Response.status(200).entity("R6. listando os produtos do user" + username).build();
        }
        return Response.status(200).entity("R6. nao há produtos para este user").build();
    }


    //todo: terminar o metodo
    //R8 - Add products to user products
    @POST
    @Path("/{username}/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response adicionarProduto(@PathParam("username") String username, ProductDto produto) {
        UserDto u = userbean.getLoggeduser();

        if (u != null && u.getUsername().equals(username.toLowerCase())) {
            List<UserPojo> userPojos = utilityBean.getUserPojos();
            List<ProductPojo> productPojos = new ArrayList<>();
            int highestId = 1;

            for (UserPojo userPojo : userPojos) {
                for (ProductPojo productPojo : userPojo.getProductPojosList()) {
                    productPojos.add(productPojo);
                    int idProduto = productPojo.getIdProduto();
                    if (idProduto >= highestId) {
                        highestId = ++idProduto;
                    }
                }
            }

            produto.setIdProduto(highestId);
            utilityBean.setProductPojos(productPojos);
            MessageDTO messageDTO = productBean.adicionarProdutoAoUtilizador(produto, u);

            return Response.status(200).entity(messageDTO).build();
        } else {
            return Response.status(400).entity("R8. sem utilzador logado").build();
        }
    }


    //todo: terminar o metodo
    //R9 Update product of user products
    @POST
    @Path("/{username}/products/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarProdutosUser(@PathParam("username") String username, @PathParam("productId") String productId) {
        return Response.status(200).entity("R9. produto do " + username + " foi atualizado").build();
    }

    //todo: terminar o metodo
    //R10 Delete product of user products
    @DELETE
    @Path("/{username}/products/{productId}")
    public Response apagarProdutoUser(@PathParam("username") String username, @PathParam("productId") String productId) {
        System.out.println("username " + username);
        System.out.println("productId " + productId);
        return Response.status(200).entity("R10. artigo " + productId + " deletado").build();
    }

}
