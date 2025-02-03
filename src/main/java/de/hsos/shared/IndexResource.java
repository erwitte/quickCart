package de.hsos.shared;

import de.hsos.article.control.ArticleService;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@RequestScoped
@Path("/")
public class IndexResource {
    @Inject
    Template index;
    @Inject
    Template register;
    @Inject
    @RestClient
    KeycloakAPI keycloakAPI;
    @Inject
    ArticleService articleService;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response index(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("pageSize") @DefaultValue("9") int pageSize
    ) {
        List<ArticleDTO> articles = articleService.getArticles().stream().map(ArticleConverter::articleToArticleDTO).toList();
        List<ArticleDTO> pagedArticles = PagingService.getPagedArticleList(page, pageSize, articles);
        TemplateInstance indexInstance = index.data("articles", pagedArticles);
        return Response.ok(indexInstance).type(MediaType.TEXT_HTML).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("register")
    public Response register() {
        TemplateInstance registerInstance = register.instance();
        return Response.ok(registerInstance.render()).type(MediaType.TEXT_HTML).build();
    }

    @POST
    @Path("login")
    public Response login(LoginDTO loginDTO) {
        return keycloakAPI.receiveToken(
                "quarkus-be",
                "Ffw0GGjHrfTFnLb8mHFlBGLjcIL8hpfO",
                "password",
                loginDTO.username(),
                loginDTO.password()
        );
    }
}
