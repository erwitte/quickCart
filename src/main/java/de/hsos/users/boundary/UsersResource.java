package de.hsos.users.boundary;

import de.hsos.shared.CreateUserDTO;
import de.hsos.shared.KeycloakAPI;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/users")
@RequestScoped
@Authenticated
public class UsersResource {
    @Inject
    Template authenticate;
    @Inject
    @RestClient
    KeycloakAPI keycloakAPI;

    @POST
    @Path("/e")
    @PermitAll
    public Response getUsers(CreateUserDTO newUser) {
        //CreateUserDTO a = new CreateUserDTO("ellich", "pw", "firstName", "lastName", "aasdeasdafdffa@afa.de");
        //a.setAddress("street", "city", "state", "zip");
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0el85eEgwR3pDVEkwcHJQSk05WDRLU20wNFh2QkJ1Sjl2N2EzbC13b3BRIn0.eyJleHAiOjE3Mzc3Mjg2MTcsImlhdCI6MTczNzcyODMxNywianRpIjoiNTNiZmRjZmUtMWI5MC00NjdhLTk0ZDgtNzliNDlkMTZkZWE0IiwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo4MDgxL3JlYWxtcy9xdWlja2NhcnQtcmVhbG0iLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsImFjY291bnQiXSwic3ViIjoiNTc3NmMxZTgtMzY5Ni00ZWEwLTg2YTEtNTVhNmEyNjlmNTI2IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicXVhcmt1cy1iZSIsInNpZCI6ImEyMmU0NmUxLTQwNzYtNDc5Mi05YTY3LTRiYTljNmQyOTUwZCIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1xdWlja2NhcnQtcmVhbG0iLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwidXNlciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJlYWxtLW1hbmFnZW1lbnQiOnsicm9sZXMiOlsibWFuYWdlLXVzZXJzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJhZGRyZXNzIjp7InN0cmVldF9hZGRyZXNzIjoiZXN0In0sImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoic2FnYXIga3VtYXIiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzYWdhciIsImdpdmVuX25hbWUiOiJzYWdhciIsImZhbWlseV9uYW1lIjoia3VtYXIiLCJlbWFpbCI6InNhZ2FyQGRlbW8uY29tIn0.XyFqM8ZJkywlpgxr22ETm8j_UsJVsropvQj-3q8ktkTdNoUL_9XEoOqZ3wK5ghVgs4A3mL4gEn2aRuKtodhrUgc0H5XfAhPZQz6Js6SF4M8ET7OicedbP52IDjvmSqiHrlTAPB-yEdWusxP_iUj1lRIgrvwygnHwxUXYmY55Uwiwucbp9dGccs5tU2Nj7uLb33yXyJ8d06QUUtj1WIC0x_j5ci5ejwNHc321lVbYez-v47scwvQFcQC4i_fVbfsgdh9hJQGnpgzp5R-fF07S704OE2cxdTUV-BSqkbEPBQDXvm-7ofc6YQ5lBS01azOIzXo7G8D74liyoxsEx9YM2w";
        keycloakAPI.createUser(newUser, "Bearer " + token);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    public Response authenticate(){
        TemplateInstance renderedTemplate = authenticate.instance();
        return Response.ok(renderedTemplate.render()).type(MediaType.TEXT_HTML).build();
    }
}
