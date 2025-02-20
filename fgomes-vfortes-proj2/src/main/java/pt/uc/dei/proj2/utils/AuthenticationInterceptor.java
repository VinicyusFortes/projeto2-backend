package pt.uc.dei.proj2.utils;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import jakarta.inject.Inject;
import pt.uc.dei.proj2.beans.UserBean;

import java.io.IOException;
import java.lang.reflect.Method;

@Provider // Marca esta classe como um provider JAX-RS, permitindo que seja descoberta automaticamente
public class AuthenticationInterceptor implements ContainerRequestFilter {

    @Inject
    private UserBean userBean; // Injeta o UserBean para realizar a autenticação

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Method method = resourceInfo.getResourceMethod();

        // Verifica se o método está anotado com @PermitAll
        if (method.isAnnotationPresent(PermitAll.class)) {
            // Se estiver anotado com @PermitAll, permite o acesso sem autenticação
            return;
        }

        String username = requestContext.getHeaderString("Username"); // Username do header do request
        String password = requestContext.getHeaderString("Password"); // Password do header do request

        if (!userBean.login(username, password)) {  // Valida o login
            // Caso o login falhe, aborta o request e retorna uma resposta Unauthorized
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Credenciais inválidas").build());
        }
        // Se o login for bem-sucedido, a requisição continua normalmente
    }
}
