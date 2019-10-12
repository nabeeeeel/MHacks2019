const express = require('express');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));


app.post('/', (req, res) => {
    console.log(req);
    console.log(req.body);
    res.send("Welcome!")
});

app.listen(6969, () => {
    console.log("Listening to 'localhost:6969'");
})