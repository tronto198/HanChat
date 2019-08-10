function senderrormsg(res, received){
  let Msg;
  if(received == undefined){
    Msg = "null";
  }
  else{
    if(received.length < 25){
      Msg = received;
    }
    else{
      Msg = received.substring(0, 22) + "...";
    }
  }
  res.status(501).send(`Error!   your msg : ${Msg} `);
}

const log = function (req, res, next){
  console.log('\n\napptest/');
  next();
}


const multer = require('multer');
const storage = multer.diskStorage({
  destination: function(req, file, cb){
    cb(null, 'upload/')
  },
  filename: function(req, file, cb){
    cb(null, file.originalname)
  }
});
const upload = multer({storage : storage});



module.exports = function(Connecter){
  const express = require('express');
  const apptest = express.Router();

  apptest.use(log);

  apptest.post('/chatbot', (req, res) =>{
    const body = req.body;
    console.log('chatbot : \n');
    console.log(body);

    const text = body.text;
    if(text == "" || text == undefined){
      console.log("bye");
      res.send("nothing");
      return;
    }
    Connecter.sendtoDialogflow(text, 'apptest-id').then((r)=>{
      res.send(r.queryResult.fulfillmentText);

    }).catch((err)=>{
      senderrormsg(res, text);
      console.log(err);
    });

  });

  apptest.post('/image', (req, res) =>{
      const body = req.body;
      console.log('image : \n');
      console.log(body);


      const fs = require('fs');

      const image = body.image;
      fs.writeFile('received.txt', image, (err)=>{
        if(err) return;
        console.log('finish');
      })

    Connecter.sendtoVision(image).then((r) =>{
        console.log(r);
        res.send(r.textAnnotations[0].description);
      })
      .catch(err =>{
        senderrormsg(res, image);
        console.log(err);
      });
  });


  apptest.post('/login', (req, res) =>{
    const body = req.body;
    console.log('login : \n');
    console.log(body);
    res.json(body);
  })


  apptest.post('/test', upload.single('userimage'), function(req, res){
    console.log('test :');
    const body = req.body;
    console.log('body -');
    console.log(body);
    console.log(body.text);
    console.log(decodeURI(body.text));

    const file = req.file;
    console.log('file -');
    console.log(req.file);

    var encoded = Buffer.from(file).toString('base64');

    Connecter.sendtoVision(encoded).then((r) =>{
        console.log(r);
        res.send(r.textAnnotations[0].description);
      })
      .catch(err =>{
        console.log(err);
        senderrormsg(res, image);
      });


  });


  return apptest;
}
