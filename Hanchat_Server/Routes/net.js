const fs = require('fs');
const path = require('path');
const layout = require('./htmllayout');
html = new layout();

gethtmllayout = html.gethtmllayout;
getimagehtml = html.getimagehtml;
getchatbothtml = html.getchatbothtml;
gettesthtml = html.gettesthtml;


const log = function(req, res, next){
  process.stdout.write('net/');
  next();
};

module.exports = function(Connecter){
  const express = require('express');
  const net = express.Router();

  net.use(log);

  net.get('/', (req,res) =>{

    res.end(fs.readFileSync(`${__dirname}/tmp_root.html`));
  });


  net.route('/chatbot')
  .post((req, res) =>{
    const body = req.body;
    console.log('chatbot');
    console.log(body);

    const text = body.text;
    Connecter.sendtoDialogflow(text, 'net-id').then((r)=>{
      res.send(getchatbothtml(r.queryResult.fulfillmentText));
      var no = Math.floor(Math.random() * (100000));
      var name = r.queryResult.fulfillmentText;
      console.log(r);
      console.log(r.queryResult.parameters.fields);

      Connecter.query(`insert into tester values(${no}, "${name}");`,(err, rows, fields)=>{

      });
    }).catch((err)=>{
      res.send(getchatbothtml(err));
      console.log(err);
    });

  })
  .get((req, res) => {
    console.log('chatbot');
    res.end(getchatbothtml(''));
  });


  net.route('/image')
  .post((req, res) =>{
      const body = req.body;
      console.log('image');

      const image = body.image;
      Connecter.sendtoVision(image)
        .then(r =>{
          console.log(r);
          res.send(getimagehtml(r.textAnnotations[0].description));
        })
        .catch(err =>{
          console.log(err);
            res.send(getimagehtml(err));
        });
  })
  .get((req, res) =>{
    console.log('image');
    res.end(getimagehtml(''));
  });


  net.route('/test')
  .post((req, res) =>{
    console.log('test');
    const body = req.body;
    const text = body.text;
    Connecter.sendtoDialogflow(text, 'test-id').then((r)=>{
      res.send(gettesthtml(r.queryResult.fulfillmentText));
      console.log(r);
    }).catch((err)=>{
      res.send(gettesthtml(err));
      console.log(err);
    });
  })
  .get((req, res) =>{
    console.log('test');
    res.end(gettesthtml(''));
  });



  net.route('/imagetest')
  .post(Connecter.upload.single('userimage'), (req, res) =>{
    console.log('imagetest :');
    const body = req.body;
    console.log('body -');
    console.log(body);
    console.log(decodeURI(body.text));

    const file = req.file;
    console.log('file -');
    console.log(req.file);
    if(file == undefined){
      res.end('nothing');
      return;
    }

    const description = fs.readFileSync(Connecter.uploadpath + file.filename);

    var encoded = Buffer.from(description).toString('base64');

    Connecter.sendtoVision(encoded).then((r) =>{
        console.log(r);
        var result = r.textAnnotations[0].description;
        res.send(gettesthtml(result));
      })
      .catch(err =>{
        console.log(err);
        //senderrormsg(res, image);
        res.send(gettesthtml(err));
      });

  })
  .get((req, res) =>{
    console.log('imagetest');
    res.end(gettesthtml(''));
  });


  return net;
};
