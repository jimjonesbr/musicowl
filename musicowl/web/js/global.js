var notestack = [];
var queryOffset =0;
var queryLimit =1000;
var opts = {
  lines: 13, // The number of lines to draw
  length: 20, // The length of each line
  width: 10, // The line thickness
  radius: 30, // The radius of the inner circle
  corners: 1, // Corner roundness (0..1)
  rotate: 0, // The rotation offset
  direction: 1, // 1: clockwise, -1: counterclockwise
  color: '#000', // #rgb or #rrggbb or array of colors
  speed: 1, // Rounds per second
  trail: 60, // Afterglow percentage
  shadow: false, // Whether to render a shadow
  hwaccel: false, // Whether to use hardware acceleration
  className: 'spinner', // The CSS class to assign to the spinner
  zIndex: 2e9, // The z-index (defaults to 2000000000)
  top: '50%', // Top position relative to parent
  left: '50%' // Left position relative to parent
};


$(document).keyup(function(e) {
  if(e.which == 46) {

    removeLastNote();

  }
});



function toggle() {
	var ele = document.getElementById("toggleText");
	var text = document.getElementById("displayText");
	if(ele.style.display == "block") {
    		ele.style.display = "none";
		text.innerHTML = "More Options";
  	}
	else {
		ele.style.display = "block";
		text.innerHTML = "Hide More Options";
	}
}

function removeLastNote(rohtext) {

  notestack.splice(-1,1);

  $('#incipit_01').val('');

  for (var i = 0; i < notestack.length; i++) {
    $('#incipit_01').val($('#incipit_01').val() + notestack[i].pea);
  }

  update_incipit();

}


$(function(){
  $( "#chkLength" ).click(function(){
    if($("#chkPitch").is(':checked')){
      $('#chkPitch').prop('checked', false);
    }
  });
});

$(function(){
  $( "#chkPitch" ).click(function(){
    if($("#chkLength").is(':checked')){
      $('#chkLength').prop('checked', false);
    }
  });
});

function showSpin(){

  target = document.getElementById('itemsContainer');
  spinner = new Spinner(opts).spin(target);

  target.appendChild(spinner.el);

}

function hideSpin(){

  spinner.stop();

}
