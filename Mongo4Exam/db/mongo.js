const { MongoClient, ObjectID, Logger } = require("mongodb");
const assert = require('assert');

var mongoClient = MongoClient(
     "mongodb://admin:password@mongodb/", // localhost:27017 on local // DOCKER
    // "mongodb://admin:password@localhost:27017/", //LOCALHOST
  { useUnifiedTopology: true, useNewUrlParser: true}
);

var returnObj = {
  status: "",
  message: "",
  data: {},
  links: [],
  meta: {
    origin: "MongoDB",
  },
};

var csvParser = require("./csv_converter");

mongoClient
  .connect()
  .then(() => {
    console.log("MongoDB connection fully alive");
    Logger.setLevel('error');
    Logger.filter('class', ['Db']);
    var db = mongoClient.db("coursera");
    db.command({ismaster:true}, function(err, d) {
      assert.equal(null, err);
    });
  })
  .catch((e) => {
    console.log("ERROR: " + e);
  });

async function populateMongoDB() {
  try {
    var db = mongoClient.db("coursera");
    db.createCollection( "courses", {
      validator: { $jsonSchema: {
         bsonType: "object",
         required: [ "name", "url", "level","price"],
         properties: {
            name: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            url: {
               bsonType : "string",
               description: "must be a string and match the regular expression pattern"
            },
            level: {
               bsonType: "string",
               description: "must be a string and is required"
            },
            price: {
              bsonType: "int",
              description: "must be a string"
           }
         }
      } },
      validationAction: "error"
    } )
    var collection = db.collection("courses");
    let results = csvParser.loadCsv();
    collection.insertMany(await results, function (err, resultDocuments) {
      console.log("Done populating DB");
    });
  } catch (e) {
    console.log(e)
    return e;
  }
}




async function findById(req, res) {
  try {
    const session = mongoClient.startSession();
    let id = req.params.id;
    var db = mongoClient.db("coursera");
    var collection = db.collection("courses");
    let result = await collection
      .find(
        {
          _id: id,
        },
        {}
      )
      .toArray();

    if (result.length > 0) {
      returnObj.data = result[0];
      returnObj.status = 200;
      returnObj.message = "Success";
      returnObj.links = [
        {
          href: "/api/v1/courses/" + req.params.id,
          rel: "courses",
          type: "GET",
        },
      ];
      return res.send(JSON.stringify(returnObj));
    } else {
      return res
        .status(404)
        .send(
          JSON.stringify({
            status: 404,
            data: {},
            message: "No Course with requested id",
          })
        );
    }
  } catch (e) {
    console.log(e);
    return res.status(500).send();
  }
}

async function deleteById(req, res) {
  try {
    let id = req.params.id;
    var db = mongoClient.db("coursera");
    var collection = db.collection("courses");
    let result = await collection.findOneAndDelete(
      {
        _id: id,
      },
      {}
    );
    if (result.value == null) {
      return res
        .status(404)
        .send(
          JSON.stringify({
            status: 404,
            data: {},
            message: "No Course with requested id",
          })
        );
    } else {
      returnObj.data = {};
      returnObj.status = 200;
      returnObj.message = "Successful deleted";
      returnObj.links = [
        {
          href: "/api/v1/courses/" + req.params.id,
          rel: "courses",
          type: "DELETE",
        },
      ];
      return res.status(200).send(JSON.stringify(returnObj));
    }
  } catch (e) {
    return res
      .status(500)
      .send({ status: 500, data: {}, message: "Server Error" });
  }
}

async function updateById(req, res) {
  try {
    let id = req.params.id;
    let body = req.body;
    var db = mongoClient.db("coursera");
    var collection = db.collection("courses");
    let result = await collection.findOneAndUpdate(
      {
        _id: id,
      },
      {
        $set: body,
      },
      { returnOriginal: false }
    );
    if ((await result.value) == null) {
      return res
        .status(404)
        .send(
          JSON.stringify({
            status: 404,
            data: {},
            message: "No Course with requested id",
          })
        );
    } else {
      returnObj.data = result.value;
      returnObj.status = 200;
      returnObj.message = "Successful updated";
      returnObj.links = [
        {
          href: "/api/v1/courses/" + req.params.id,
          rel: "courses",
          type: "PUT",
        },
      ];
      return res.status(200).send(JSON.stringify(returnObj));
    }
  } catch (e) {
    console.log(e);
    return res
      .status(500)
      .send({ status: 500, data: {}, message: "Server Error" });
  }
}

async function addDocuments(req, res) {
  let courses = req.body.courses;
  await courses.forEach((courses) => {
    let newObjectId = new ObjectID().toHexString();
    courses["_id"] = newObjectId;
  })
  try {
    var db = mongoClient.db("coursera");
    var collection = db.collection("courses");
    var result = await collection.insertMany(await courses);
    if (await result.insertedCount == courses.length) {
      console.log(await result)
      returnObj.data = {};
      returnObj.status = 201;
      returnObj.message = "Successful added documents";
      returnObj.links = [
        { href: "/api/v1/courses/", rel: "courses", type: "POST" },
      ];
      return res.status(201).send()
    } else {
      console.log(await result)
      returnObj.data = {};
      returnObj.status = 201;
      returnObj.message = "Inserted some documents: " + insertedCount + "/" + courses.length;
      returnObj.links = [
        { href: "/api/v1/courses/", rel: "courses", type: "POST" },
      ];
      return res.status(201).send()
    }

  } catch (e) {
    console.log(e)
    return res.status(500).send({ status: 500, data: {}, message: "Server Error" });
  }
}

async function addDocument(req, res) {
  let body = req.body;
  if (!body.hasOwnProperty("_id")) {
    let newObjectId = new ObjectID().toHexString();
    body["_id"] = newObjectId;
  }

  var db = mongoClient.db("coursera");
  var collection = db.collection("courses");
  try {
    let cursor = await collection.insertOne(body);
    returnObj.data = await cursor.ops[0];
    returnObj.status = 201;
    returnObj.message = "Successful added document";
    returnObj.links = [
      { href: "/api/v1/courses/", rel: "courses", type: "POST" },
    ];
    return res.status(201).send(JSON.stringify(returnObj));
  } catch (e) {
    console.log(e.code);
    if (e.code == 11000) {
      return res
        .status(400)
        .send({
          status: 400,
          data: {},
          message: "Duplicate key for _id field",
        });
    } else {
      return res
        .status(500)
        .send({ status: 500, data: {}, message: "Server Error" });
    }
  }
}

async function findCoursesWithParams(req, res) {
  try {
    var mongo_operator_price = "$ne";
    var mongo_operator_tag = "$ne";
    var mongo_operator_level = "$ne";
    var price = -1;
    var tags = [];
    var level = [];

    if (req.query.price != undefined) {
      if (req.query.operator == "lessThan") {
        mongo_operator_price = "$lt";
      } else if (req.query.operator == "greaterThan") {
        mongo_operator_price = "$gt";
      } else {
        mongo_operator_price = "$eq";
      }
      price = parseInt(req.query.price);
    }

    if (req.query.tags != undefined) {
      console.log(req.query.tags);
      let arr = req.query.tags.split(",");
      arr.forEach((element) => {
        tags.push(element);
      });
      mongo_operator_tag = "$in";
      console.log(tags);
    }

    if (req.query.level != undefined) {
      let arr = req.query.level.split(",");
      arr.forEach((element) => {
        level.push(element);
      });
      mongo_operator_level = "$in";
    }
    //GET QUERY FOR TESTING IN MONGO ATLAS
    //console.log(   mongo_operator_price + ": " +  price , mongo_operator_tag + ": " + tags.toString()  ,  mongo_operator_level+ ": " + level  )
    var db = mongoClient.db("coursera");
    var collection = db.collection("courses");
    let resultCursor = await collection.find({
      $and: [
        { price: { [mongo_operator_price]: price } },
        { tags: { [mongo_operator_tag]: tags } },
        { level: { [mongo_operator_level]: level } },
      ],
    });
    console.log(await JSON.stringify(resultCursor.cursorState.cmd.query));
    console.log(await resultCursor.cursorState.cmd.query);
    let result = await resultCursor.toArray();

    returnObj.data = await result;
    returnObj.status = 200;
    returnObj.message = "Successful collecting documents";
    returnObj.links = [
      {
        href: "/api/v1/courses",
        rel: "courses",
        params: "price, tags, level",
        type: "GET",
      },
    ];

    return res.send(JSON.stringify(returnObj));
  } catch (e) {
    console.log(e);
    return res
      .status(500)
      .send({ status: 500, data: {}, message: "Server Error" });
  }
}

async function getDistinctLevels(req, res) {
  try {
    var db = mongoClient.db("coursera");
    var collection = db.collection("courses");
    let result = await collection.distinct("level");
    returnObj.data = { levels: await result };
    returnObj.status = 200;
    returnObj.message = "Successful collected distinct levels";
    returnObj.links = [
      {
        href: "/api/v1/courses/levels",
        rel: "courses",
        type: "GET",
      },
    ];
    return res.status(200).send(JSON.stringify(returnObj));
  } catch (e) {
    return res
      .status(500)
      .send({ status: 500, data: {}, message: "Server Error" });
  }
}

async function getDistinctTags(req, res) {
  try {
    var db = mongoClient.db("coursera");
    var collection = db.collection("courses");
    let result = await collection.distinct("tags");
    console.log(result)
    returnObj.data = { levels: await result };
    returnObj.status = 200;
    returnObj.message = "Successful collected distinct tags";
    returnObj.links = [
      {
        href: "/api/v1/courses/tags",
        rel: "courses",
        type: "GET",
      },
    ];
    return res.status(200).send(JSON.stringify(returnObj));
  } catch (e) {
    return res
      .status(500)
      .send({ status: 500, data: {}, message: "Server Error" });
  }
}


async function getLogs(req,res){
  var db = mongoClient.db("coursera");
  var result = await db.executeDbAdminCommand( { getLog: "global" } )
  console.log(result)
  return res.send(JSON.stringify({logs: await result}))
}


module.exports = {
  populateMongoDB: populateMongoDB,
  findById: findById,
  deleteById: deleteById,
  updateById: updateById,
  addDocument: addDocument,
  findCoursesWithParams: findCoursesWithParams,
  getDistinctLevels: getDistinctLevels,
  getDistinctTags: getDistinctTags,
  addDocuments: addDocuments,
  getLogs: getLogs
};






// DEPRECATED --> See findCoursesWithParams() ;-)
/*
async function findAll(req, res) {
    try{
        var db = mongoClient.db("coursera");
        var collection = db.collection("courses");
        let result = await collection.find();
        returnObj.data = await result.toArray();
        returnObj.status = 201;
        returnObj.message = "Successful added document"
        returnObj.links = [{href: "/api/v1/courses/",
                            rel: "courses",
                            type: "GET"
                            }]
        return res.send(JSON.stringify(returnObj));
    }catch(e){
        return res.status(500).send({message: "Server Error"})
    }
}

async function findCoursesWithPrice(req, res) {
    //console.log('/courses?price')
    let comparePrice;
    let comparator;
    if (req.query.price.includes("<")) {
        comparePrice = req.query.price.replace("<", "").replace(",", ".");
        comparator = false;
    } else if (req.query.price.includes(">")) {
        comparePrice = req.query.price.replace(">", "").replace(",", ".");
        comparator = true;
    }

    var comparePriceFloat = parseFloat(comparePrice);
    var db = mongoClient.db("coursera");
    var collection = db.collection("courses");
    let resultCursor;
    try {
        if (comparator) {
            resultCursor = await collection.find({ price: { $gt: comparePriceFloat } })
        } else {
            resultCursor = await collection.find({ price: { $lt: comparePriceFloat } })
        }
        let result = await resultCursor.toArray();
        result.sort((a, b) => {
            return b.price - a.price;
        })
        return res.send(JSON.stringify(await result));
    } catch (e) {
        return res.status(500).send({ message: "Error try again" });
    }
}

async function findCoursesWithTag(req, res) {
    let tag = req.query.tag;
    var db = mongoClient.db("coursera");
    var collection = db.collection("courses");
    let result = await collection.find({ Tags: tag });
    let resu = await result.toArray();
    console.log(await resu.length)

    return res.send(JSON.stringify(await resu));
}

async function findCoursesWithLevel(req, res) {
    let level = req.query.level;
    var db = mongoClient.db("coursera");
    var collection = db.collection("courses");
    let result = await collection.find({ Level: level });
    let resu = await result.toArray();
    console.log(await resu.length)
    return res.send(JSON.stringify(await resu));
}

async function findCountOfTags(req, res) {
    var db = mongoClient.db("coursera");
    var collection = db.collection("courses");
    let result = await collection.mapReduce(
        function () {
            for (let i = 0; i < this.Tags.length; i++) {
                var tag = this.Tags[i];
                emit(tag, 1);
            }
        },
        function (key, values) {
            var count = 0;
            for (let i = 0; i < values.length; i++) {
                count += values[i];
            }
            return count;
        },
        { out: { inline: 1 } }
    );
    result.sort((a, b) => {
        return b.value - a.value;
    })
    return res.send(JSON.stringify(await result.slice(0, 10)));
}
*/
