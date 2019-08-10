const express = require('express');
const bodyParser = require('body-parser');
const fs = require('fs');



const conn = require('./Connecter/Connecter.js');
const Connecter = new conn();


const app = express();
app.use('/upload', express.static('upload'));

app.use(bodyParser.json({limit: '10mb', extended: true}));
app.use(bodyParser.urlencoded({limit: '10mb', extended: true}));

app.use('/apptest', require('./Routes/apptest.js')(Connecter));
app.use('/net', require('./Routes/net.js')(Connecter));


app.all('/', (req,res) =>{
  res.redirect('/net');
});


app.post('/test', (req, res) =>{
  const body = req.body;

  const image = body.image;
  Connecter.sendtoVision(image).then((r) =>{
      console.log(r);
      res.send(r.textAnnotations[0].description);
    })
    .catch(err =>{
      console.log(err);
        res.send(err);
    });

});


//throw new Error('test error');

module.exports = app;
