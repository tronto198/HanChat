var dd = require('./WebServer.js');

const Portnumber = 55252;

dd.listen(Portnumber, () => console.log(`Server start at : ${Portnumber}`));
