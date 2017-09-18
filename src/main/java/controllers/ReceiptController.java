package controllers;

import api.CreateReceiptRequest;
import api.ReceiptResponse;
import dao.ReceiptDao;
import dao.TagsDao;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/receipts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptController {
    final ReceiptDao receipts;
    final TagsDao tags;

    public ReceiptController(ReceiptDao receipts, TagsDao tags) {
        this.receipts = receipts;
        this.tags = tags;
    }

    @POST
    public int createReceipt(@Valid @NotNull CreateReceiptRequest receipt) {
        return receipts.insert(receipt.merchant, receipt.amount);
    }

    @GET
    public List<ReceiptResponse> getReceipts() {
        List<ReceiptsRecord> receiptRecords = receipts.getAllReceipts();
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }

    @Path("/{id}/tags")
    @GET
    public List<String> getTagsForReceiptController(@PathParam("id") int receiptId){
        System.out.println("Getting tags for receipt"+receiptId);
        List<TagsRecord> tagsRecord = tags.getTagsForReceipt(receiptId);
        List<String> tags = new ArrayList<>();
        for(TagsRecord t : tagsRecord){
            tags.add(t.getTag());
        }
        System.out.println("Tags: "+tags);
        return tags;
    }
}
