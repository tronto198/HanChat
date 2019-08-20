var app = require('./WebServer.js');
const Portnumber = 55252;

app = new app();

app.listen(Portnumber, () => console.log(`Server start at : ${Portnumber}`));
