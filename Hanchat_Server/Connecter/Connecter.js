const dialogflow = require('./Dialogflow_Connecter.js');
const gcpvision = require('./GCPVision_Connecter.js');
const keytoconfig = require('./KeytoConfig.js');
const path = require('path');

const Dialogflow_ProjectId = 'newagent-fxhlqn';
const Dialogflow_keyfilePath = path.join(__dirname, '..', 'Data/JSON/newagent-Dialogflow.json');
const TextDetector_keyfilePath = path.join(__dirname, '..', 'Data/JSON/NewAgent-TextDetection.json');


class Connecter {
  constructor() {
    console.log('connecting...');
    this.Dialogflowapi = new dialogflow(Dialogflow_ProjectId, keytoconfig(Dialogflow_keyfilePath));
    this.Visionapi = new gcpvision(keytoconfig(TextDetector_keyfilePath));

  }

  async sendtoVision(encodedimage){
    const [result] = await this.Visionapi.sendtoVision(encodedimage);
    return result;
  }

  async sendtoDialogflow(text, sessionId){
    const [result] = await this.Dialogflowapi.sendtoDialogflow(text, sessionId);
    return result;
  }

}

module.exports = Connecter;

/*
ddd = new Connecter('hello');

ddd.sendtoDialogflow('안녕', 'test').then(r =>{
  console.log(r);
})
.catch(err =>{
  console.log(err);
});
*/
