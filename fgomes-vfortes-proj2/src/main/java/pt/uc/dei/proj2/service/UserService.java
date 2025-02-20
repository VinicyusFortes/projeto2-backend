package pt.uc.dei.proj2.service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
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
import java.time.LocalDate;
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
        return Response.status(401).entity("R1. username e/ou password errados!").build();
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
            return Response.status(201).entity("R3. The new user is registered").build();
        }
        return Response.status(401).entity("R3. There is a user with the same username!").build();
    }

    //R4 - Update user profile
    @PUT
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarPerfil(@PathParam("username") String username, JsonObject dadosAtualizacao) {
        // Regista o username recebido para fins de depuração
        System.out.println("username " + username);

        // Procura o utilizador pelo username na base de dados
        UserPojo u = userbean.getUserByUsername(username);

        // Verifica se o utilizador foi encontrado
        if (u != null) {
            // Atualiza os campos do utilizador se estiverem presentes no JSON recebido

            if (dadosAtualizacao.containsKey("firstName")) {
                u.setFirstName(dadosAtualizacao.getString("firstName"));
            }

            if (dadosAtualizacao.containsKey("lastName")) {
                u.setLastName(dadosAtualizacao.getString("lastName"));
            }

            if (dadosAtualizacao.containsKey("cellphone")) {
                u.setCellphone(dadosAtualizacao.getString("cellphone"));
            }

            if (dadosAtualizacao.containsKey("email")) {
                u.setEmail(dadosAtualizacao.getString("email"));
            }

            if (dadosAtualizacao.containsKey("image")) {
                u.setImage(dadosAtualizacao.getString("image"));
            }

            // Tenta guardar as alterações na base de dados
            boolean atualizado = userbean.atualizarUser(u);

            if (atualizado) {
                // Se a atualização for bem-sucedida, cria um JSON com os dados atualizados
                JsonObject perfilAtualizado = Json.createObjectBuilder()
                        .add("firstName", u.getFirstName())
                        .add("lastName", u.getLastName())
                        .add("cellphone", u.getCellphone())
                        .add("email", u.getEmail())
                        .add("image", u.getImage())
                        .build();

                // Devolve o perfil atualizado com estado 200 (OK)
                return Response.status(200).entity(perfilAtualizado).build();
            } else {
                // Se houver falha na atualização, devolve estado 500 (Erro Interno do Servidor)
                return Response.status(500).entity("R4. Falha ao atualizar o perfil de " + username).build();
            }
        } else {
            // Se o utilizador não for encontrado, devolve estado 400 (Pedido Inválido)
            return Response.status(400).entity("R4. Perfil de " + username + " não encontrado.").build();
        }
    }


    //R5 - Get user profile
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("username") String username) {
        System.out.println(username);
        UserPojo u = userbean.getUserByUsername(username);
        if (u != null) {

            JsonObject perfil = Json.createObjectBuilder()
                    .add("firstName", u.getFirstName())
                    .add("lastName", u.getLastName())
                    .add("username", u.getUsername())
                    .add("cellphone", u.getCellphone())
                    .add("email", u.getEmail())
                    .add("image", u.getImage()).build();


            return Response.status(200).entity(perfil).build();
        } else
            return Response.status(400).entity("R5. there is no user logged in at the moment!").build();
    }

    //R6 - List products of a user
    @GET
    @Path("/{username}/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarProdutosUser(@PathParam("username") String username) {
        ArrayList<ProductPojo> products = userbean.getProductsOfUsername(username);
        ArrayList<ProductDto> produtos = productBean.convertProductPojoListToProductDtoList(products);
        return Response.status(200).entity(produtos).build();

    }


    //R8 - Add products to user products
    @POST
    @Path("/{username}/products")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adicionarProduto(@PathParam("username") String username, ProductDto produto) {
        UserDto u = userbean.getLoggeduser();
        produto.setData(LocalDate.now());

        if (u != null && u.getUsername().equals(username.toLowerCase())) {
            List<UserPojo> userPojos = utilityBean.getUserPojos();
            int highestId = 1;

            for (UserPojo userPojo : userPojos) {
                for (ProductPojo productPojo : userPojo.getProducts()) {
                    int idProduto = productPojo.getIdProduto();
                    if (idProduto >= highestId) {
                        highestId = ++idProduto;
                    }
                }
            }

            produto.setIdProduto(highestId);
            produto.setAnuncianteId(u.getId());
            MessageDTO messageDTO = productBean.adicionarProdutoAoUtilizador(produto, u);

            return Response.status(200).entity(messageDTO).build();
        } else {
            return Response.status(400).entity("R8. sem utilzador logado").build();
        }
    }


    //R9 Update product of user products
    @POST
    @Path("/{username}/products/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarProdutosUser(@PathParam("username") String username, @PathParam("productId") String productId) {
        return Response.status(200).entity("R9. produto do " + username + " foi atualizado").build();
    }


    //R10 Delete product of user products
    @DELETE
    @Path("/{username}/products/{productId}")
    public Response apagarProdutoUser(@PathParam("username") String username, @PathParam("productId") String productId) {
        System.out.println("username " + username);
        System.out.println("productId " + productId);
        return Response.status(200).entity("R10. artigo " + productId + " deletado").build();
    }

    //Retorna todos os utilizadores
    @GET
    @Path("/allusers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<UserDto> users = userbean.getAllUsers();
        return Response.status(200).entity(users).build();
    }
}
