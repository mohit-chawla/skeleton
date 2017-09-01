package controllers;

import api.CreateReceiptRequest;
import api.ReceiptResponse;
import dao.ReceiptDao;
import generated.tables.records.ReceiptsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags")
@Consumes(MediaType.WILDCARD)
@Produces(MediaType.WILDCARD)
public class TagsController {
    @PUT
    @Path("{tag}")
    public int createTag(@PathParam("tag") String tag, @NotNull int id) {
        System.out.println("Trying to add tag:"+tag+"for id:"+id);
        return 1;
    }
}
