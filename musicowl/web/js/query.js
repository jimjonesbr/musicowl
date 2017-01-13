

var endpoint = "http://linkeddata.uni-muenster.de:7200/repositories/wwu";
//var endpoint = "http://localhost:7200/repositories/wwu";

namedGraph = "http://www.uni-muenster.de/musik";

var arrayCheckboxes = [];
var maxVal=0;
var minVal=0;
var loadedMaps=0;
var totalMaps=0;
var wktBBOX="";
var target;
var spinner;

function executeQuery(offset) {

	if(notestack.length!=0){

		showSpin();

	 	queryOffset = offset

		var sparqlQuery = $.sparql(endpoint)
		.prefix("mso","http://linkeddata.uni-muenster.de/ontology/musicscore#")
		.prefix("chord","http://purl.org/ontology/chord/")
		.prefix("note","http://purl.org/ontology/chord/note/")
		.prefix("dc","http://purl.org/dc/elements/1.1/")
		.prefix("rdfs","http://www.w3.org/1999/02/22-rdf-syntax-ns#")
		.prefix("mo","http://purl.org/ontology/mo/")
		.prefix("foaf","http://xmlns.com/foaf/0.1/")
				  .select(["?scoreNode","?scoreTitle", "?creator","?creatorNode", "?movemenTitle", "?measure", "?thumbnail","?partID","?partName","?voiceID","?staffID"])
							.where("?scoreNode","foaf:thumbnail","?thumbnail")
							.where("?scoreNode","dc:title","?scoreTitle")
							.where("?scoreNode","mo:movement","?movementNode")
					  	.where("?movementNode","dc:title","?movemenTitle")
					  	.where("?scoreNode","dc:creator","?creatorNode")
					  	.where("?creatorNode","foaf:name","?creator")
					  	.where("?movementNode","mso:hasScorePart","?part")
					  	.where("?movementNode","dc:title","?movement")
					  	.where("?part","mso:hasMeasure","?measureNode")
							.where("?part","rdfs:ID","?partID")
	            .where("?part","dc:description","?partName")
							.where("?measureNode","rdfs:ID","?measure")
	            .where("?voice","a","mso:Voice")
	            .where("?voice","rdfs:ID","?voiceID")
	            .where("?measureNode","mso:hasNoteSet","?noteset0")
	            .where("?staff","mso:hasNoteSet","?noteset0")
	            .where("?staff","a","mso:Staff")
	            .where("?staff","rdfs:ID","?staffID")
				  	.orderby("?scoreTitle").orderby("?movemenTitle").orderby("?measure")
				  .limit(queryLimit)
				  .offset(queryOffset).distinct();

		for(var i = 0; i<  notestack.length; i++) {

			if(notestack[i].pitch != null){

				sparqlQuery.where("?noteset"+i,"mso:hasNote","?note"+i)
	                 .where("?voice","mso:hasNoteSet","?noteset"+i);

				if(notestack[i].accidental != null){
						sparqlQuery.where("?note"+i,"chord:modifier","chord:"+notestack[i].accidental);
				} else {

	        sparqlQuery.filter("NOT EXISTS {?note"+i+" chord:modifier ?modifier}");
	      }

				if(!$("#chkPitch").is(':checked')){

					if(notestack[i].pitch=="rest"){
							sparqlQuery.where("?note"+i,"chord:natural","note:rest");
					} else {
							sparqlQuery.where("?note"+i,"chord:natural","note:"+notestack[i].pitch);
					}


				}

				if(!$("#chkLength").is(':checked')){

					var duration = "";
					if(notestack[i].duration=="1"){ duration = "Whole"}
					if(notestack[i].duration=="2"){ duration = "Half"}
					if(notestack[i].duration=="4"){ duration = "Quarter"}
					if(notestack[i].duration=="8"){ duration = "Eighth"}
					if(notestack[i].duration=="6"){ duration = "16th"}
					if(notestack[i].duration=="3"){ duration = "32nd"}
					if(notestack[i].duration=="5"){ duration = "64th"}

					sparqlQuery.where("?noteset"+i,"mso:hasDuration","?duration"+i)
										 .where("?duration"+i,"a","mso:"+duration);

				}

				if(notestack.length > (i+1)){
					sparqlQuery.where("?noteset"+i,"mso:nextNoteSet","?noteset"+(i+1));
				}

			}

		}

		console.log("SPARQL Encoded fired to [" + endpoint + "] \n"+  sparqlQuery.serialiseQuery());
		console.log("Sending SPARQL...");
		sparqlQueryJson(encode_utf8(sparqlQuery.serialiseQuery()), endpoint, myCallback, false);
		console.log("SPARQL executed");

	}
}


function encode_utf8(rohtext) {
     // dient der Normalisierung des Zeilenumbruchs
     rohtext = rohtext.replace(/\r\n/g,"\n");
     var utftext = "";
     for(var n=0; n<rohtext.length; n++)
         {
         // ermitteln des Unicodes des  aktuellen Zeichens
         var c=rohtext.charCodeAt(n);
         // alle Zeichen von 0-127 => 1byte
         if (c<128)
             utftext += String.fromCharCode(c);
         // alle Zeichen von 127 bis 2047 => 2byte
         else if((c>127) && (c<2048)) {
             utftext += String.fromCharCode((c>>6)|192);
             utftext += String.fromCharCode((c&63)|128);}
         // alle Zeichen von 2048 bis 66536 => 3byte
         else {
             utftext += String.fromCharCode((c>>12)|224);
             utftext += String.fromCharCode(((c>>6)&63)|128);
             utftext += String.fromCharCode((c&63)|128);}
         }
     return utftext;
 }

function sparqlQueryJson(queryStr, endpoint, callback, isDebug) {

	var querypart = "query=" + escape(queryStr);

	//** Get our HTTP request object.
	var xmlhttp = null;

	if(window.XMLHttpRequest) {
		xmlhttp = new XMLHttpRequest();
	} else if(window.ActiveXObject) {
		//** Code for older versions of IE, like IE6 and before.
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	} else {
		alert('Perhaps your browser does not support XMLHttpRequests?');
}


	//** Set up a POST with JSON result format.
	xmlhttp.open('POST', endpoint, true); // GET can have caching probs, so POST
	xmlhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
	xmlhttp.setRequestHeader("Accept", "application/sparql-results+json");


	//** Set up callback to get the response asynchronously.
	xmlhttp.onreadystatechange = function() {

	if(xmlhttp.readyState == 4) {
		if(xmlhttp.status == 200) {
			   //** Do something with the results
			   if(isDebug) alert(xmlhttp.responseText);
				   callback(xmlhttp.responseText);
			 } else {
				   //** Some kind of error occurred.
				   alert("Sparql query error: " + xmlhttp.status + " " + xmlhttp.responseText);
	 	}
	}
};

//** Send the query to the endpoint.
xmlhttp.send(querypart);

};


//** Define a callback function to receive the SPARQL JSON result.
function myCallback(str) {

	console.log("#DEBUG query.js -> main query executed.");
  $('#displayTotal').text('');
	//** Convert result to JSON
	var jsonObj = eval('(' + str + ')');

	console.log(jsonObj);

	if (queryOffset == 0 || queryOffset == null){

		$("#result").html("");
		$("#result").append('<ul id="itemsContainer" style="list-style-type:none"></ul>');

	}

	if(jsonObj.results.bindings.length == 0){
		$("#result").html("No score found for the given melody.");
		$("#result").append('<ul id="itemsContainer" style="list-style-type:none"></ul>');
	}


  var list = [];
	var record = new Object();

	if(jsonObj.results.bindings.length>0){
			$('#displayTotal').text("Total records found: "+jsonObj.results.bindings.length);
	}









	for(var i = 0; i<  jsonObj.results.bindings.length; i++) {

		if (typeof jsonObj.results.bindings[i].scoreNode !== 'undefined') {

			var measure = '';
			var scoreTitle = '';
			var movemenTitle = '';
			var measure = '';
			var scoreURL = '';
			var thumbnail = '';
			var creator = '';
			var creatorURL = '';
			var partID = '';
      var staffID = '';
      var partName = '';
      var voice = '';

			if (typeof jsonObj.results.bindings[i].measure !== 'undefined') {
				measure = jsonObj.results.bindings[i].measure.value;
//				record.measure = jsonObj.results.bindings[i].measure.value;

			}
			if (typeof jsonObj.results.bindings[i].scoreTitle !== 'undefined') {
				scoreTitle = jsonObj.results.bindings[i].scoreTitle.value;
//				record.scoreTitle = jsonObj.results.bindings[i].scoreTitle.value;
			}

			if (typeof jsonObj.results.bindings[i].scoreNode !== 'undefined') {
				scoreURL = jsonObj.results.bindings[i].scoreNode.value;
//				record.scoreNode = jsonObj.results.bindings[i].scoreNode.value;
			}

			if (typeof jsonObj.results.bindings[i].creator !== 'undefined') {
				creator = jsonObj.results.bindings[i].creator.value;
//				record.creator = jsonObj.results.bindings[i].creator.value;
			}

			if (typeof jsonObj.results.bindings[i].movemenTitle !== 'undefined') {
				movemenTitle = jsonObj.results.bindings[i].movemenTitle.value;
//				record.movemenTitle = jsonObj.results.bindings[i].movemenTitle.value;
			}

			if (typeof jsonObj.results.bindings[i].thumbnail !== 'undefined') {
				thumbnail = jsonObj.results.bindings[i].thumbnail.value;
//				record.thumbnail = jsonObj.results.bindings[i].thumbnail.value;
			}

			if (typeof jsonObj.results.bindings[i].creatorNode !== 'undefined') {
				creatorNode = jsonObj.results.bindings[i].creatorNode.value;
//				record.creatorNode = jsonObj.results.bindings[i].creatorNode.value;
			}

			if (typeof jsonObj.results.bindings[i].partID !== 'undefined') {
				partID = jsonObj.results.bindings[i].partID.value;
//				record.partID = jsonObj.results.bindings[i].partID.value;
			}

      if (typeof jsonObj.results.bindings[i].voiceID !== 'undefined') {
        voiceID = jsonObj.results.bindings[i].voiceID.value;
//				record.voiceID = jsonObj.results.bindings[i].voiceID.value;
      }

      if (typeof jsonObj.results.bindings[i].partName !== 'undefined') {
        partName = jsonObj.results.bindings[i].partName.value;
//				record.partName = jsonObj.results.bindings[i].partName.value;
      }

      if (typeof jsonObj.results.bindings[i].staffID !== 'undefined') {
        staffID = jsonObj.results.bindings[i].staffID.value;
//				record.staffID = jsonObj.results.bindings[i].staffID.value;
      }


//			list.push(record);

			var id = scoreURL.replace('https://sammlungen.ulb.uni-muenster.de/id/','');

			$("#result ul").append('<li><a target="_blank" href=' + scoreURL +'><img style="float:left; width: 15%; height: auto;" src="' +
											thumbnail + '" alt="Kein Bild vorhanden" width="90" height="90" ></a><a target="_blank" href=' + scoreURL
								      +'>' + scoreTitle + '</a><br><b>Movement:</b> '+movemenTitle+'<br><b>Composer:</b> <span class="composer"'+i+'><a target="_blank" href=' + creatorNode
								      +'>'+creator+'</a></span><br><b>Starting measure:</b> '+measure+'<br><b>Part / Instrument:</b> '+partName+'<br><b>Voice:</b> '+
								      voiceID +'<br><b>Staff:</b> '+staffID+'</p>'+
											'<a target="_blank" href="http://www.verovio.org/mei-viewer.xhtml?file=http://linkeddata.uni-muenster.de/musicportal/web/xml/'+ id +'.xml&amp;musicxml=true"><button type="button" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-circle-arrow-right" aria-hidden="true"></span> Play</button></a>');



	}

	}






	hideSpin();
}
