const dialogflow = require('./Dialogflow_Connecter.js');
const textdetect = require('./TextDetector.js');
const http = require('http');
const fs = require('fs');
const url = require('url');
const qs = require('querystring');


const ProjectId = 'newagent-fxhlqn';
const Dialogflow_keyfilePath = `${__dirname}/Data/newagent-fxhlqn-7a42dc304eee.json`;
const TextDetector_keyfilePath = `${__dirname}/Data/NewAgent-TextDetection.json`;


function getPostRequest(req, callback){

}

var app = http.createServer(function(req, res){
  var _url = req.url;
  var pathname = url.parse(_url, true).pathname;
  console.log(pathname);


  if(pathname == '/'){
    //_url = '/test.html';
    res.writeHead(200);
    res.end(    `
          <!doctype html>
          <html>
            <head>
              <title>test_Chatbot</title>
              <meta charset="utf-8">
            </head>
            <body>
              <p>
                <h1>테스트중</h1>
              </p>
              <p>
                <h3>주소 뒤에 /chatbot_test?text=(text)<h3>
              </p>
            </body>
          </html>
        `);
  }
  else if (pathname == '/connect'){
    var body = '';

    req.on('data', () => {
      body += data;

      if(body.length > 1e6)
      req.connection.destroy();
    });

    req.on('end', () => {
      var post = qs.parse(body);
      //본문

    });
  }
  else if(pathname == '/chatbat_request'){
      const post = getPostRequest(req);

      const text = post.text;
  }
  else if(pathname == '/chatbot_test'){
    var queryData = url.parse(_url, true).query;
    var text = queryData.text;
    console.log(`\ntext = ${text}`);
    var Dialogflowapi = new dialogflow(ProjectId, Dialogflow_keyfilePath);
    Dialogflowapi.sendToDialogflow(text, 'test-id').then((r)=>{
      /*
      console.log(r[0]);
      console.log(typeof r[0]);
      rr = JSON.stringify(r[0]);
      console.log(rr);
      console.log(typeof rr);
      res.writeHead(200);
      res.end(rr, 'utf-8');
*/
      res.writeHead(200);
      res.end(`
        <!doctype html>
        <html>
          <head>
            <meta charset="utf-8">
            <title>Result</title>
          </head>
          <body>
            <p>
              ${r[0].queryResult.fulfillmentText}
            </p>
          </body>
        </html>
      `);

    }).catch((err)=>{
      res.writeHead(200);
      res.end(`${error} : ${err.meesage}`);

      console.log(err);
    });


    //console.log(response);
  }
  else if(pathname === '/image'){
    var body = '';
    req.on('data', (data) => {
      body += data;

      if(body.length > 1e6){

        req.connection.destroy();
        console.log(4444);
      }
    });

    req.on('end', () => {
      var post = qs.parse(body);
      //본문
      console.log(typeof post);
      console.log(post);
      var image = post.image;
      image = 'dddd';

      const Visionapi = new textdetect(TextDetector_keyfilePath);
      Visionapi.sendtoVision(image).then(text =>{
      console.log(text);
        res.writeHead(200);
        res.end(text);
      }).catch(err => {
        res.writeHead(200);
        res.end(err);
        console.log(333);
        console.log(err);
      });
    });

  }
  else{
    res.writeHead(404);
    res.end('404 not found');
  }

});
app.listen(55252);
