package api;


import generated.tables.records.ReceiptsRecord;
import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class ReceiptResponseTest {

    private final Validator validator = Validators.newValidator();

    @Test
    public void testValidResponse() {
        ReceiptsRecord record = new ReceiptsRecord();
        record.setId(1);
        record.setMerchant("TESTMERCHANT");
        record.setAmount(new BigDecimal(33.44));
        record.setUploaded(Time.valueOf(LocalTime.now()));
        ReceiptResponse receipt = new ReceiptResponse(record);
        assertThat(validator.validate(receipt), empty());
    }

    @Test
    public void testMissingMerchant() {
        ReceiptsRecord record = new ReceiptsRecord();
        record.setId(1);
        //record.setMerchant("TESTMERCHANT");
        record.setAmount(new BigDecimal(33.44));
        record.setUploaded(Time.valueOf(LocalTime.now()));
        ReceiptResponse receipt = new ReceiptResponse(record);
        assertThat(validator.validate(receipt), hasSize(1));
    }

}