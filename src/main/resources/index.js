

function addTag(tag,receipt,elem){
    $.ajax({
        type: 'PUT',
        url: '/tags/'+tag,
        data: receipt,
        success: function(data) {
//                alert('data: ' + data);
            elem.val("");
            elem.after('<div class="tag-body tagValue" receipt-id="'+receipt+'" class="">'+tag+'<div class="remove-tag " tag-name="'+tag+'"><b>X</b></div></div>');
            elem.remove();
        },
        contentType: "application/json",
        dataType: 'json'
    });
}

function removeTag(tag,receipt,elem){
    $.ajax({
        type: 'PUT',
        url: '/tags/'+tag,
        data: receipt,
        success: function() {
            elem.parent().remove();
        },
        contentType: "application/json",
        dataType: 'json'
    });
}

$(document).on("click",".remove-tag",function(){
    tagName = $(this).attr("tag-name");
    receiptId = $(this).parent().attr("receipt-id");
    console.log(receiptId);
    removeTag(tagName,receiptId,$(this))
})


$(document).on("click",".add-tag",function(e){
    receiptId = $(this).attr("receipt-id");
    receiptNode = $(this).parent();
//        $('INPUT.tag_input', receiptNode).show();
    $(this).parent().append("<input type=\"text\" class=\"tag_input\" receipt-id="+receiptId+" />");
});



$(document).on("keydown",".tag_input",function(e){
    if(e.keyCode == 13){
        tagName = $(this).val();
        receiptId = $(this).attr("receipt-id");
        addTag(tagName,receiptId,$(this));
    }
});

function clearAddReceiptInputs(){
    $("#amount").val("");
    $("#merchant").val("");
}
$("#add-receipt").on("click",function(){
    $("#add-receipt-dialogue").show();
    $("#add-receipt-error").hide();
});
$("#cancel-receipt").on("click",function(){
    clearAddReceiptInputs();
    $("#add-receipt-dialogue").hide();
    $("#add-receipt-error").hide();
});
$("#save-receipt").on("click",function(){
    merchant = $("#merchant").val();
    amount = $("#amount").val();
    console.log(merchant);
    console.log(amount);

    if(merchant.length == 0){
        console.log("here");
        $("#add-receipt-error").text("Merchant cannot be empty.");
        $("#add-receipt-error").show();
    }
    else if(amount.length == 0){
        $("#add-receipt-error").text("Amount cannot be empty.");
        $("#add-receipt-error").show();
    }else{
        //debugger;
        $.ajax({
            type: 'POST',
            url: '/receipts',
            data: '{"merchant":"'+merchant+'","amount":'+amount+'}', // or JSON.stringify ({name: 'jonas'}),
            success: function(data) {
                $("#receiptList").empty();
                getReceipts();
                clearAddReceiptInputs();
                $("#add-receipt-dialogue").hide();
            },
            error: function(status,data){
                console.log(status);
                console.log("hi"+data);
                $("#add-receipt-error").text(data);
                $("#add-receipt-error").show();
            },
            contentType: "application/json",
            dataType: 'json'
        });
    }
});
