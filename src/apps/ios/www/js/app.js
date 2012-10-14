// core application handling
var online = false;
var ready = false;
var initialized = false;
var initializing = false;

function attemptInitialization() {
	if (initialized || initializing) {
		return; // already initialized
	}
	
	if (! online || ! ready) {
		return; // not prepared to initialize yet
	}
	
	initializing = true;
	log("Attempting to initialize app");
	
	if (localStorage.token) {
		log("Found token [" + localStorage.token.substring(0, 5) + "...], using...");
	} else {
		attemptLogin("chris2@techxonline.net", "testing!");
	}
}

function attemptLogin(email, password) {
	log("Attempting to login with email [" + email + "] and password");
	
	api("user/login", {
		email: email,
		password: password
	}, function(response) {
		alert(response);
	});
}

// api management
function api(command, params, callback) {
	if (localStorage.token) {
		params.token = localStorage.token;
	}
	
	$.post("http://trenders.org/api/" + command, params, callback);
}

// device event handling
document.addEventListener("deviceready", onDeviceReady, false);
document.addEventListener("online", onDeviceOnline, false);
document.addEventListener("offline", onDeviceOffline, false);
document.addEventListener("pause", onDevicePaused, false);
document.addEventListener("resume", onDeviceResumed, false);

function onDeviceReady() {
	log("Device is ready.");
	
	ready = true;
	attemptInitialization();
}

function onDeviceOnline() {
	log("Device is online.");
	
	online = true;
	attemptInitialization();
}

function onDeviceOffline() {
	log("Device is offline.");
	
	online = false;
	attemptInitialization();
}

function onDevicePaused() {
	log("Device has been paused.");
}

function onDeviceResumed() {
	log("Device has been resumed.");
}


// check user agent for testing on PC
if (navigator.userAgent.indexOf("Chrome") >= 0) {
	// simulate normal events
	onDeviceReady();
	onDeviceOnline();
}

// logging
function log(msg) {
	var now = new Date();
	msg = "[" + dd(now.getHours()) + ":" + dd(now.getMinutes()) + ":" + dd(now.getSeconds()) + "]: " + msg;
	
	var logs = $("#log");
	logs.html(logs.html() + "<br />" + msg);
	logs.scrollTop(logs[0].scrollHeight);
}

function dd(str) {
	str = str.toString();
	
	if (str.length < 2) {
		return "0" + str;
	}
	
	return str;
}