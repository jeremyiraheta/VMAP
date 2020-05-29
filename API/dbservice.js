var mysql = require('mysql');
var connection = mysql.createConnection({
   host: 'localhost',
   user: 'dbuser',
   password: 'brandom',
   database: 'vmap',
   port: 3306
});
connection.connect(function(error){
   if(error){
      console.log('Conexion fallida.');
      throw error
   }else{
      console.log('Conexion correcta.');
   }
});

var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({ extended: false })
app.get('/', function(req,res){
  res.end("<html>" +
  "<head><title>Gestion Simple</title></head>" +
  "<body>" +
  "<h2>Ingresa Locacion</h2>" +
  "<form style='display:block;width:200px;' action='/sendlocacion' method=post>" +
  "<input type=text name=nombre placeholder='Ingresar Nombre' required>" +
  "<input type=text name=descripcion placeholder='Ingresar Descripcion' required>" +
  "<input type=text name=facultad placeholder='Ingresar Facultad' required>" +
  "<input type=text name=horarios placeholder='Ingresar Horario' required>" +
  "<div style='border: 1px solid black;'><input type=text name=x placeholder='Ingresar X'>" +
  "<input type=text name=y placeholder='Ingresar Y'>" +
  "<input type=text name=z placeholder='Ingresar Z'>" +
  "</div><button>Enviar</button>" +
  "</form>" +
  "</body>" +
  "</html>");
});
app.get('/locaciones', function (req, res) {
   var query = connection.query('SELECT ID_LOCACION, NOMBRE, DESCRIPCION, FACULTAD, HORARIOS, X,Y,Z FROM locaciones l inner join points p on p.ID_POINT = l.ID_POINT ', function(error, result){
      if(error){
         res.end("500: Fracaso");
      }else{
         var resultado = result;
         if(resultado.length > 0){
             var resp={};
             for(var i=0; i< resultado.length; i++){
             resp[i] = {
              ID: resultado[i].ID_LOCATION,
              NOMBRE: resultado[i].NOMBRE,
              DESCRIPCION: resultado[i].DESCRIPCION,
              FACULTAD: resultado[i].FACULTAD,
              HORARIOS: resultado[i].HORARIOS,
              X: resultado[i].X,
              Y: resultado[i].Y,
              Z: resultado[i].Z
            }            
            }
            resp['size'] = i;
            res.send(JSON.stringify(resp));
         }else{
            res.send('No tiene datos');
         }
      }
   }
);
})
app.post('/sendlocacion',urlencodedParser, function (req, res) {
  var q = "call insertLocacion('" + req.body.nombre + "','" + req.body.descripcion + "','" + req.body.facultad + "','" + req.body.horarios + "'," +req.body.x + "," + req.body.y + "," +req.body.z + ");";  
   var query = connection.query(q, function(error, result){
      if(error){        
         res.end("500: Fracaso");
      }else{
         res.end("200: Exito");
      }
   }
);   
})
app.get('/locaciones/:id', function (req, res) {
   var id = " where ID_LOCACION = " + req.params.id;
   var q = 'SELECT ID_LOCACION, NOMBRE, DESCRIPCION, FACULTAD, HORARIOS, X,Y,Z FROM locaciones l inner join points p on p.ID_POINT = l.ID_POINT ' + id;
   var query = connection.query(q, function(error, result){
      if(error){
         res.end("500: Fracaso");
      }else{
         var resultado = result;
         if(resultado.length > 0){
             var resp={};
             for(var i=0; i< resultado.length; i++){
             resp[i] = {
              ID: resultado[i].ID_LOCATION,
              NOMBRE: resultado[i].NOMBRE,
              DESCRIPCION: resultado[i].DESCRIPCION,
              FACULTAD: resultado[i].FACULTAD,
              HORARIOS: resultado[i].HORARIOS,
              X: resultado[i].X,
              Y: resultado[i].Y,
              Z: resultado[i].Z
            }            
            }
            resp['size'] = i;
            res.send(JSON.stringify(resp));
         }else{
            res.send('No tiene datos');
         }
      }
   }
);
})
app.get('/pinteres', function (req, res) {
   var query = connection.query('SELECT * FROM pinteres', function(error, result){
      if(error){
         res.end("500: Fracaso");
      }else{
         var resultado = result;
         if(resultado.length > 0){
             var resp={};
             for(var i=0; i< resultado.length; i++){
             resp[i] = {
              CARNET: resultado[i].CARNET,
              ID_LOCACION: resultado[i].ID_LOCACION,
              FECHA: resultado[i].FECHA
            }            
            }
            resp['size'] = i;
            res.send(JSON.stringify(resp));
         }else{
            res.send('No tiene datos');
         }
      }
   }
);
})
app.post('/sendpinteres',urlencodedParser, function (req, res) {
   var query = connection.query('insert into pinteres values (\'' + req.body.carnet + '\',' + req.body.id + ',CURDATE())', function(error, result){
      if(error){
         res.end("500: Fracaso");
      }else{
         res.end("200: Exito");
      }
   }
);   
})
app.get('/estudiantes', function (req, res) {
   var query = connection.query('SELECT * FROM estudiantes', function(error, result){
      if(error){
         res.end("500: Fracaso");
      }else{
         var resultado = result;
         if(resultado.length > 0){
             var resp={};
             for(var i=0; i< resultado.length; i++){
             resp[i] = {
              CARNET: resultado[i].CARNET,
              NOMBRES: resultado[i].NOMBRES,
              APELLIDOS: resultado[i].APELLIDOS,
              CARRERA: resultado[i].CARRERA              
            }            
            }
            resp['size'] = i;
            res.send(JSON.stringify(resp));
         }else{
            res.send('No tiene datos');
         }
      }
   }
);
})
app.post('/sendestudiante',urlencodedParser, function (req, res) {
   var query = connection.query('insert into estudiantes values (\'' + req.body.carnet + '\',\'' + req.body.nombres + '\',\'' + req.body.apellidos + '\',\'' + req.body.facultad + '\')', function(error, result){
      if(error){
         res.end("500: Fracaso");
      }else{
         res.end("200: Exito");
      }
   }
);   
})
app.get('/estudiantes/:carnet', function (req, res) {
  var carnet = " where CARNET = " + req.params.carnet;
   var query = connection.query('SELECT * FROM estudiantes' + carnet, function(error, result){
      if(error){
         res.end("500: Fracaso");
      }else{
         var resultado = result;
         if(resultado.length > 0){
             var resp={};
             for(var i=0; i< resultado.length; i++){
             resp[i] = {
              CARNET: resultado[i].CARNET,
              NOMBRES: resultado[i].NOMBRES,
              APELLIDOS: resultado[i].APELLIDOS,
              CARRERA: resultado[i].CARRERA              
            }            
            }
            resp['size'] = i;
            res.send(JSON.stringify(resp));
         }else{
            res.send('No tiene datos');
         }
      }
   }
);
})

var server = app.listen(8081, function () {
   var host = server.address().address
   var port = server.address().port
   
   console.log("Servidor Escuchando en http://%s:%s", host, port)
})