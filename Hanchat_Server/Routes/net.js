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
  `
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
  return gethtmllayout('Dialogflow test', 'Dialogflow 테스트중', `
  <form action="/net/test" method="post">
    <input type="text" name="text", placeholder="말해보세요"
    size=50>
    <input type="submit">
  </form>
  `, text);
}


const log = function(req, res, next){
  console.log('\n\nnet/');
  next();
}

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
    console.log('chatbot : \n');
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
    res.end(getchatbothtml(''));
  });


  net.route('/image')
  .post((req, res) =>{
      const body = req.body;
      console.log('image \n');

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
  })



  return net;
}
