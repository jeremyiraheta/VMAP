var mysql = require('mysql');
var connection = mysql.createConnection({
   host: 'localhost',
   user: 'vmap',
   password: '12345',
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
  "<div style='border: 1px solid black;'><input type=text name=x placeholder='Ingresar X'>" +
  "<input type=text name=y placeholder='Ingresar Y'>" +
  "<input type=text name=z placeholder='Ingresar Z'>" +
  "</div><button>Enviar</button><hr><a href=/update>Modificar</a>" +
  "</form>" +
  "</body>" +
  "</html>");
});
app.get('/update', function (req, res) {
      var query = connection.query('SELECT p.ID_POINT ID, NOMBRE, X,Y,Z FROM LOCACIONES l inner join POINTS p on p.ID_POINT = l.ID_POINT ', function(error, result){
	  var resultado = result;
	 var html = "<html><head><title>Editar</title></head><body><a href='http://localhost:8081'>Agregar Nuevo</a><br><hr>"	
         if(resultado.length > 0){             
             for(var i=0; i< resultado.length; i++){
		     html += "<form method=post action=/updateLocacion>" +
		     "<input type=text name=nombre value='" + resultado[i].NOMBRE + "' required>" +
		     "<input type=text name=x size=4 value=" + resultado[i].X + ">" +
		     "<input type=text name=y size=4 value=" + resultado[i].Y + ">" +
		     "<input type=text name=z size=4 value=" + resultado[i].Z + ">" +
		     "<button name='id' value=" + resultado[i].ID + ">Guardar</button></form><br>"
            }            
	    html += "</body></html>";
            res.end(html);
         }else{
            res.send('No tiene datos');
         }    
      })
})
app.post('/updateLocacion',urlencodedParser, function (req, res) {
   var q = 'update POINTS set X=' + req.body.x + ', Y=' + req.body.y + ',Z=' + req.body.z + " where ID_POINT=" + req.body.id;
   var query = connection.query(q, function(error, result){
      if(error){
	 console.log("Fracaso en: " + q);
         res.end("500: Fracaso");
      }else{
	 console.log("Exito en: " +q);
         res.end("<html><body>200: Exito<br><a href=/update>Regresar</a></body></html>");
      }
   }
);   
})
app.get('/locaciones', function (req, res) {
   var query = connection.query('SELECT ID_LOCACION, NOMBRE, X,Y,Z FROM LOCACIONES l inner join POINTS p on p.ID_POINT = l.ID_POINT ', function(error, result){
      if(error){
         res.end("500: Fracaso");
      }else{
         var resultado = result;
         if(resultado.length > 0){
             var resp={};
             for(var i=0; i< resultado.length; i++){
             resp[i] = {
              ID: resultado[i].ID_LOCACION,
              NOMBRE: resultado[i].NOMBRE,              
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
  var q = "call insertLocacion('" + req.body.nombre + "'," +req.body.x + "," + req.body.y + "," +req.body.z + ");";  
   var query = connection.query(q, function(error, result){
      if(error){        
	 console.log(error);
         res.end("500: Fracaso");
      }else{
         res.end("<html><body>200: Exito<br><a href='http://localhost:8081'>Nuevo</a></body></html>");
      }
   }
);   
})
app.get('/locaciones/:id', function (req, res) {
   var id = " where ID_LOCACION = " + req.params.id;
   var q = 'SELECT ID_LOCACION, NOMBRE, X,Y,Z FROM LOCACIONES l inner join POINTS p on p.ID_POINT = l.ID_POINT ' + id;
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
   var query = connection.query('SELECT CARNET,NOMBRE,FECHA FROM PINTERES inner join LOCACIONES l l.ID_LOCACION=p.ID_LOCACION', function(error, result){
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
app.get('/pinteres/:carnet', function (req, res) {
   var carnet = " where CARNET = " + req.params.carnet;
   var query = connection.query('SELECT CARNET,NOMBRE,FECHA FROM PINTERES p' + carnet + " inner join LOCACIONES l l.ID_LOCACION=p.ID_LOCACION", function(error, result){
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
   var q = 'insert into PINTERES values (\'' + req.body.carnet + '\',' + req.body.id + ',CURDATE())';
   var query = connection.query(q, function(error, result){
      if(error){
	 console.log("Fracaso en: " + q);
         res.end("500: Fracaso");
      }else{
	 console.log("Exito en: " +q);
         res.end("200: Exito");
      }
   }
);   
})
app.post('/delpinteres',urlencodedParser, function (req, res) {
   var q = 'delete from PINTERES WHERE CARNET=\'' + req.body.carnet + '\' and ID_LOCACION=' + req.body.id;
   var query = connection.query(q, function(error, result){
      if(error){
	 console.log("Fracaso en: " + q);
         res.end("500: Fracaso");
      }else{
	 console.log("Exito en: " +q);
         res.end("200: Exito");
      }
   }
);   
})
app.get('/estudiantes', function (req, res) {
   var query = connection.query('SELECT * FROM ESTUDIANTES', function(error, result){
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
   var q = 'insert into ESTUDIANTES values (\'' + req.body.carnet + '\',\'' + req.body.nombres + '\',\'' + req.body.apellidos + '\',\'' + req.body.facultad + '\')';
   var query = connection.query(q, function(error, result){
      if(error){
	 console.log("Fracaso en: " + q);
         res.end("500: Fracaso");
      }else{
	 console.log("Exito en: " + q);
         res.end("200: Exito");
      }
   }
);   
})
app.get('/estudiantes/:carnet', function (req, res) {
  var carnet = " where CARNET = " + req.params.carnet;
   var query = connection.query('SELECT * FROM ESTUDIANTES' + carnet, function(error, result){
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
