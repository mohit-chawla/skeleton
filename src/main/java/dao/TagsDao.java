package dao;

import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class TagsDao {
    DSLContext dsl;

    public TagsDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String tagName, int receiptId) {
        TagsRecord tagsRecord = dsl
                .insertInto(TAGS,TAGS.TAG, TAGS.RECEIPT_ID)
                .values(tagName, receiptId)
                .returning(TAGS.ID)
                .fetchOne();

        checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");

        return tagsRecord.getId();
    }

    public TagsRecord getTag(int receiptId, String tagName){
        System.out.println("Getting tag for receiptId:"+receiptId+"tagName: "+tagName);
        return dsl.selectFrom(TAGS).where(TAGS.RECEIPT_ID.eq(receiptId)).and(TAGS.TAG.eq(tagName)).fetchOne();
    }
    public List<TagsRecord> getTaggedReciepts(String tag){
        return dsl.selectFrom(TAGS).where(TAGS.TAG.eq(tag)).fetch();
    }
    public int removeTag(TagsRecord tagRecord){
        return tagRecord.delete();
    }

    public List<TagsRecord> getAllTags() {
        return dsl.selectFrom(TAGS).fetch();
    }
}
