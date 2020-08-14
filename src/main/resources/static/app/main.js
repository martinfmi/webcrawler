
function test() {
	jQuery.ajax({
		url: "/frontend/text",
		success: function(response) {
			alert(response);
		}
	})
	console.log("Variable x is: " + x);
};
var x = 10;
// setInterval(test, 3000);
setTimeout(test, 3000);
