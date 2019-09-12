const fs = require('fs');

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
  res.end(`Error!   your msg : ${Msg} `);
}

const log = function (req, res, next){
  process.stdout.write('apptest/');
  next();
};
/*
const path = require('path');
const uploadpath = path.join(__dirname, '..', 'upload/');
const multer = require('multer');
const storage = multer.diskStorage({
  destination: function(req, file, cb){
    cb(null, uploadpath);
  },
  filename: function(req, file, cb){
    cb(null, file.originalname);
  }
});
const upload = multer({storage : storage});
*/



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
      res.send("send nothing");
      return;
    }
    Connecter.sendtoDialogflow(text, 'apptest-id').then((r)=>{
      res.send(r.queryResult.fulfillmentText);
      var no = Math.floor(Math.random() * (100000));
      var name = r.queryResult.fulfillmentText;
      Connecter.query(`insert into tester values(${no}, "${name}");`,(err, rows, fields)=>{

      });
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
      });

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
    console.log('login : ');
    console.log(body);
    res.json(body);
  });


  apptest.post('/test', Connecter.upload.single('userimage'), function(req, res){
    console.log('test :');
    const body = req.body;
    console.log('body -');
    console.log(body);
    console.log(decodeURI(body.text));

    const file = req.file;
    console.log('file -');
    console.log(req.file);

    if(file == undefined){
      res.end('file not found');
      return;
    }

    const filepath = Connecter.uploadpath + file.filename;
    console.log('filepath : ' + filepath);
    const filedata = fs.readFileSync(filepath);


    var encodeddata = Buffer.from(filedata).toString('base64');

    Connecter.sendtoVision(encodeddata).then((r) =>{
        console.log(r);
        if(r.textAnnotations == null){
          res.end('글을 인식하지 못했습니다.');
        }
        else{
          res.send(r.textAnnotations[0].description);
        }

      })
      .catch(err =>{
        console.log(err);
        senderrormsg(res, error);
      });


  });


  return apptest;
};
