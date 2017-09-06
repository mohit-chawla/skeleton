skeleton    [![CircleCI](https://circleci.com/gh/mohit-chawla/skeleton.svg?style=svg)](https://circleci.com/gh/mohit-chawla/skeleton)

============
Repository for Startup Systems Design and Engineering Assignments
---

## API SPEC
The API we are building is relatively simple, and only supports operations on two
resources: `receipts` and `tags`.  The basic idea is that one should be able to create new
receipts, *categorize* them by using user-defined *tags*, and retrieve receipts *by-tag*.

Endpoints required to support this functionality with inputs and outputs are summarized below.  All endpoints must
return `200/OK` if the operation completed successfully.

### POST /receipts
**Effect:** Create a new receipt.  Every receipt should have an `UNSIGNED INT` primary key which
is returned by this endpoint. No duplicate detection or uniqueness constraints are required.  New
receipts are un-tagged.

**Request Body:**
```javascript
{
    "merchant": "foo", /* required! */
    "amount": 22.45    /* optional, must be valid decimal if provided */
}
```
**Response:**
```
33  /* <-- this is the id of the newly-created un-tagged receipt */
```

### GET /receipts
**Effect:** Return a list of all receipts in the system.  

**Request Body:**
*None*

**Response:**
```json
[
  {
    "id": 5,
    "merchant": "Gerry's Ice Cream",
    "amount": 22.50
  },
  {
    "id": 3,
    "merchant": "Starbux"
  }
]
```

### PUT /tags/{tag}
**Effect:** Categorize a receipt by tagging it.  Tags are simple short character strings that are user-defined,
(not pre-defined). To associate a receipt with two tags, this endpoint would need to be executed twice - once for each tag.  If a receipt is *already* tagged with a specific tag (say "t1"), the effect of hitting this endpoint (for "t1") will be to un-tag the receipt for the specific tag, but no other tags will be affected.

**Request Body**
```json
35  /* <-- this is the id of the receipt to tag / untag */
```

**Response**
*None* / no response is required from the server beyond a 200/OK HTTP Response code.  To do this using
the `JAX-RS` framework, simply declare your endpoint function to return `void`.  e.g.

```java
@PUT
@Path("/tags/{tag}")
public void toggleTag(@PathParam("tag") String tagName) {
    // <your code here
}

```

### GET /tags/{tag}
**Effect:** Returns a list of all receipts associated with a tag, an empty list if there are none.

**Request Body:** *None*

**Response:**
```json
[
  {
    "id": 39,
    "merchant": "Coin-Op Laundromat",
    "amount": 2.50
  }
]
```


### GET /netid
**Effect:** Returns your netid as a string for grading purposes.

**Request Body:** *None*

**Response:**
```json
mc2683
```
