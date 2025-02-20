package pt.uc.dei.proj2.service;

import jakarta.annotation.security.PermitAll;
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
import pt.uc.dei.proj2.pojo.ProductPojo;

import java.util.ArrayList;


@Path("/products")
public class ProductService {
    @Inject
    ProductBean productBean;

    @Context
    private HttpServletRequest request;

    //todo: terminar metodo
    //R7 - List all products
    @GET
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarTodosProdutos() {
        ArrayList<ProductPojo> products = productBean.getProducts();
        ArrayList<ProductDto> produtos = productBean.convertProductPojoListToProductDtoList(products);
        return Response.status(200).entity(produtos).build();
    }

}
