const express = require('express')
const bodyParser = require('body-parser');
var cors = require('cors');
const app = express()
const port = 3000
var mongo = require("./db/mongo");

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
app.use(cors());

var version = "v1";
//INIT MONGODB IF not existing
//Populate DB
try{
    mongo.populateMongoDB();
}catch (e){
    console.log(e)
}


app.get('/api/' + version + '/courses', function (req,res) {
    console.log('/courses')
    res.setHeader('Content-Type', 'application/json');
    return mongo.findCoursesWithParams(req,res);
});

app.get('/api/' + version + '/courses/tags/top10', function (req,res) {
    console.log('/courses/tags')
    res.setHeader('Content-Type', 'application/json');
    return mongo.findCountOfTags(req,res);
});

// NEEDS TO BE LAST OTHERWISE THE CALLS ALWAYS POINT AT THIS
app.get('/api/' + version + '/courses/:id', function (req,res) {
    console.log('/courses/:id')
    res.setHeader('Content-Type', 'application/json');
    return mongo.findById(req,res);
});

app.delete('/api/' + version + '/courses/:id',function (req,res) {
    console.log('/courses/:id')
    res.setHeader('Content-Type', 'application/json');
    return mongo.deleteById(req,res);
});

app.put('/api/' + version + '/courses/:id', function (req,res) {
    console.log('PUT /courses/:id')
    res.setHeader('Content-Type', 'application/json');
    return mongo.updateById(req,res);
});

app.post('/api/' + version + '/courses/', function (req,res) {
    console.log('POST /courses/')
    res.setHeader('Content-Type', 'application/json');
    return mongo.addDocument(req,res);
});


const server = app.listen(port, () => console.log(`MongoDB API ready and listening at http://localhost:${port}`))
