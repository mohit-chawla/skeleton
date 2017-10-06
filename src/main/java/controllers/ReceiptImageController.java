package controllers;

import api.ReceiptSuggestionResponse;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.hibernate.validator.constraints.NotEmpty;

import static java.lang.System.out;

@Path("/images")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptImageController {
    private final AnnotateImageRequest.Builder requestBuilder;

    public ReceiptImageController() {
        // DOCUMENT_TEXT_DETECTION is not the best or only OCR method available
        Feature ocrFeature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        this.requestBuilder = AnnotateImageRequest.newBuilder().addFeatures(ocrFeature);

    }

    /**
     * This borrows heavily from the Google Vision API Docs.  See:
     * https://cloud.google.com/vision/docs/detecting-fulltext
     *
     * YOU SHOULD MODIFY THIS METHOD TO RETURN A ReceiptSuggestionResponse:
     *
     * public class ReceiptSuggestionResponse {
     *     String merchantName;
     *     String amount;
     * }
     */
    @POST
    public ReceiptSuggestionResponse parseReceipt(@NotEmpty String base64EncodedImage) throws Exception {
        Image img = Image.newBuilder().setContent(ByteString.copyFrom(Base64.getDecoder().decode(base64EncodedImage))).build();
        AnnotateImageRequest request = this.requestBuilder.setImage(img).build();

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse responses = client.batchAnnotateImages(Collections.singletonList(request));
            AnnotateImageResponse res = responses.getResponses(0);

            String merchantName = null;
            BigDecimal amount = null;

            // Your Algo Here!!
            // Sort text annotations by bounding polygon.  Top-most non-decimal text is the merchant
            // bottom-most decimal text is the total amount
            //top-left, nonnumeric amount
            int min_v_x = 9999999;
            int min_v_y = 9999999;
            //Bottom-right, total
            int max_v_x = -1;
            int max_v_y = -1;

            EntityAnnotation topLeftAnnotation = null;
            EntityAnnotation bottomRightAnnotation = null;
            String lines[] = null;
            boolean skip = true;
            for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                if(!skip){
                    BoundingPoly boundedPoly = annotation.getBoundingPoly();
                    List<Vertex> vertices = boundedPoly.getVerticesList();
                    System.out.println("Annotation: "+annotation.getDescription());
                    if(annotation.getDescription().matches("[a-zA-Z-]*") && topLeftAnnotation==null){
                        topLeftAnnotation = annotation;
                    }
                    if(annotation.getDescription().matches("[0-9.$]*")){
                        bottomRightAnnotation = annotation;
                    }

                /*for(Vertex v : vertices){
                    //System.out.println("Testing for vertex:"+v.getX()+" "+v.getY());
                    int v_x = v.getX();
                    int v_y = v.getY();
                    if(v_x > max_v_x || v_y > max_v_y ){
                        max_v_x = v_x;
                        max_v_y = v_y;
                        System.out.println("Option for bottom right"+annotation.getDescription());
                        if(annotation.getDescription().matches("[0-9.$]*")){
                            bottomRightAnnotation = annotation;
                            out.printf("Bottom right annotation found:"+bottomRightAnnotation.getDescription());
                        }
                    }
                    if(v_x < min_v_x || v_y < min_v_y){
                        min_v_x = v_x;
                        min_v_y = v_y;
                        System.out.println(" Option for top left"+ annotation.getDescription());
                        if(annotation.getDescription().matches("[a-zA-Z]*")){
                            topLeftAnnotation = annotation;
                            out.printf("Top left annotation found: "+topLeftAnnotation.getDescription());
                        }
                    }
                }*/
                    //out.printf("Position : %s\n", annotation.getBoundingPoly());
                    //out.printf("Text: %s\n", annotation.getDescription());
                }
                else{
                    System.out.println(annotation.getDescription());
                    lines = annotation.getDescription().split("\\r?\\n");
                }
                if(topLeftAnnotation != null){
                    merchantName = topLeftAnnotation.getDescription();
                }
                if(bottomRightAnnotation!= null){
                    String amountText = bottomRightAnnotation.getDescription();
                    if(amountText.charAt(0) == '$'){
                        amountText = amountText.substring(1);
                    }
                    amount = new BigDecimal(amountText);
                }
                skip = false;

            }
            if(lines!=null && merchantName!=lines[0]){
                merchantName = lines[0];
            }
            //TextAnnotation fullTextAnnotation = res.getFullTextAnnotation();
            return new ReceiptSuggestionResponse(merchantName, amount);
        }
    }
}