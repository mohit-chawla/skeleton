package api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;

import java.math.BigDecimal;
import java.sql.Time;

import static java.util.stream.Collectors.toList;

/**
 * This is an API Object.  Its purpose is to model the JSON API that we expose.
 * This class is NOT used for storing in the Database.
 *
 * This ReceiptResponse in particular is the model of a Receipt that we expose to users of our API
 *
 * Any properties that you want exposed when this class is translated to JSON must be
 * annotated with {@link JsonProperty}
 */
public class TagsResponse {
    @JsonIgnore
    Integer id;

    @JsonProperty
    String tag;


//    @JsonProperty
//    Object[] receipt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

//    public Object[] getReceipt() {
//        return receipt;
//    }
//
//    public void setReceipt(Object[] receipt) {
//        this.receipt = receipt;
//    }

    public TagsResponse() {
        super();
    }

    public TagsResponse(TagsRecord dbRecord) {
        this.id = dbRecord.getId();
        this.tag = dbRecord.getTag();
        //this.receipt = dbRecord.getReceiptId();

        //receiptRecords.stream().map(ReceiptResponse::new).collect(toList());receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }
}
