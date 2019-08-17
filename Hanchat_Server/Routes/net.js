const fs = require('fs');
const path = require('path');

function gethtmllayout(title, head, description, text){
  const html =`
  <!doctype html>
  <html>
    <head>
      <title> ${title} </title>
      <meta charset='utf-8'>
    </head>
    <body>
      <p>
        <h3><a href="/net/"><-</a> ${head} </h3>
      </p>
      <p>
        ${description}
      </p>
      <p>
        ${text}
      </p>
    </body>
  </html>
  `;
  return html;
}
function getchatbothtml(text){
  return gethtmllayout('Dialogflow test', 'Dialogflow 테스트중', `
  <form action="/net/chatbot" method="post">
    <input type="text" name="text", placeholder="말해보세요"
    size=50>
    <input type="submit">
  </form>
  `, text);
}
function getimagehtml(text) {
  return gethtmllayout('image test', '이미지 테스트중',`
    <form action="/net/image", method="post">
      <TEXTAREA name="image" cols=70 rows=15 placeholder="base64 encoded image"
      value=""></textarea>
      <br>
      <input type="submit">
    </form>
  ` , text);
}
function gettesthtml(text){
  return gethtmllayout('image test', '이미지 테스트중', `
  <form action="/net/imagetest" method="post" enctype="multipart/form-data">
      <input type="file" name="userimage">
      <input type="submit" value="텍스트 추출">
  </form>
  `, text);
}


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
    res.end(gettesthtml(''));
  });



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


  net.route('/imagetest')
  .post(upload.single('userimage'), (req, res) =>{
    console.log('/imagetest :');
    const body = req.body;
    console.log('body -');
    console.log(body);
    console.log(decodeURI(body.text));

    const file = req.file;
    console.log('file -');
    console.log(req.file);

    const description = fs.readFileSync(uploadpath + file.filename);

    var encoded = Buffer.from(description).toString('base64');
    console.log(encoded);

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
    res.end(gettesthtml(''));
  });


  return net;
};
