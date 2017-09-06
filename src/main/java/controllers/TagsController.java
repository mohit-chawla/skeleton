package controllers;

import api.CreateReceiptRequest;
import api.ReceiptResponse;
import api.TagsResponse;
import dao.ReceiptDao;
import dao.TagsDao;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/tags")
@Consumes(MediaType.WILDCARD)
@Produces(MediaType.APPLICATION_JSON)
public class TagsController {

    final ReceiptDao receipts;
    final TagsDao tags;

    public TagsController(ReceiptDao receipts, TagsDao tags) {
        this.receipts = receipts;
        this.tags = tags;
    }

    /**
     * Controller to toggle tag associated with a particular receipt
     * @param tag
     * @param id
     */
    @PUT
    @Path("{tag}")
    public void toggleTag(@PathParam("tag") String tag, @NotNull int id) {
        System.out.println("Trying to add tag:"+tag+"for receipt_id:"+id);
        TagsRecord existing = tags.getTag(id,tag);
        if(existing == null){
            tags.insert(tag,id);
        }else{
            System.out.println("Deleting existing tag-record");
            tags.removeTag(existing);
        }
        return;
    }

    /**
     * Controller to get receipts tagged with a particular tag
     * @param tag
     * @return
     *
     */
    @GET
    @Path("{tag}")
    public List<ReceiptResponse> getReceipts(@PathParam("tag") String tag) {
        List<TagsRecord> taggedReceiptRecords = tags.getTaggedReciepts(tag);
        List<ReceiptResponse> taggedReceipts;
        List<ReceiptsRecord> receiptsRecords = new ArrayList<ReceiptsRecord>();
        for(TagsRecord tagsRecord : taggedReceiptRecords){
            receiptsRecords.add(receipts.getReceiptById(tagsRecord.getReceiptId()));
        }
        taggedReceipts = receiptsRecords.stream().map(ReceiptResponse::new).collect(toList());
        return taggedReceipts;
    }

}
