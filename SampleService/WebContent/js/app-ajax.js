$(document).ready(function(){
	  
	$(document).on('click', '.submitform', function(event){
		var id = $(this).attr('id');
		var name = $(this).attr('name');
		$.ajax({
			type: "POST",
			url: "UserRegistrationServlet"+"?action="+id+"&name="+name, 
			data: $("#register").serialize(),
			success: function(msg){
				console.log(msg);
				$('#result').html(msg);
				$("#register").get(0).reset();
			},
			error: function(){
				$('#result').html(msg);
			}
		});
		 event.preventDefault();
	});
	
	
	$(document).on('click', '.submitformtest', function(event){
		$.ajax({
			type: "POST",
			url: "UserServlet", 
			data: $("#register").serialize(),
			success: function(msg){
				console.log(msg);
				$('#result').html(msg);
				$("#register").get(0).reset();
			},
			error: function(){
				$('#result').html(msg);
			}
		});
		 event.preventDefault();
	});
	

});