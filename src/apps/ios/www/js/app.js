// core application handling
var online = false;
var ready = false;
var initialized = false;
var initializing = false;
var user = null;

function attemptInitialization() {
	if (initialized || initializing) {
		return; // already initialized
	}
	
	if (! online || ! ready) {
		return; // not prepared to initialize yet
	}
	
	initializing = true;
	log("Attempting to initialize app...");
	
	if (localStorage.token) {
		log("Found token [" + localStorage.token.substring(0, 5) + "...], using that...");
		
		loadUserInformation(finishInitialization);
	} else { // TEMP: login automatically
		attemptLogin("chris2@techxonline.net", "testing!", function(success) {
			log("Login success: " + success);
			finishInitialization();
		});
	}
}

function finishInitialization() {
	log("App initialized.");	
	initialized = true;
	initializing = false;
}

function loadUserInformation(callback) {
	log("Loading user information for token [" + localStorage.token.substring(0, 5) + "...]");
	
	api("user/info", {}, function(response) {
		if (response.apiCode == apiCodes.OK) {
			log("Successfully loaded user information");
			
			user = {
				// specifically and explicitly outlining the things that should be available for
				// readability and clarity
				assets: {
					pretty: response.assets.pretty,
					raw: response.assets.raw
				},
				
				balance: {
					pretty: response.balance.pretty,
					raw: response.balance.raw
				},
				
				classCode: response.classCode,
				displayName: response.displayName,
				email: response.email,
				
				totalAssets: {
					pretty: response.totalAssets.pretty,
					raw: response.totalAssets.raw
				}
			};
		} else {
			log("Failed to load user information, got api code: " + response.apiCode);
			log("Resetting user token.");
			
			delete localStorage.token;
			user = null;
		}
		
		if (callback) {
			callback(response.apiCode == apiCodes.OK); // param is whether successful or not
		}
	});
}

function attemptLogin(email, password, callback) {
	log("Attempting to login with email [" + email + "] and password...");
	
	api("user/login", {
		email: email,
		password: password
	}, function(response) {
		if (response.apiCode == apiCodes.OK) {
			log("Successfully got login token, getting user info now...");
			localStorage.token = response.token;
			
			loadUserInformation(function(success) {
				if (success) {
					log("Got user data (login complete).");
				} else {
					log("Failed to get user data (login failed).");
				}
				
				if (callback) {
					callback(success); // param is successful login or not
				}
			});
		} else {
			// failed to login for some reason
			log("Login was not successful; code received: " + response.apiCode);
			delete localStorage.token;
			
			if (callback) {
				callback(false); // param is successful login or not
			}
		}
		
		initializing = false;
	});
}

// api management
var apiCodes = {
	OK: 200,
	
	MISSING_BAD_PARAMS: 400,
	SET_PASSWORD_FIRST: 401,
	BAD_LOGIN_INFO: 402,
	LOGIN_FIRST: 403,
	NOT_AVAILABLE: 404,
	UPGRADE_APP: 406,
	HIT_RATE_LIMIT: 429,
	
	UNABLE_TO_PERFORM_ACTION: 450,
	
	SERVER_ERROR: 500
};

function api(command, params, callback) {
	if (localStorage.token) {
		params.token = localStorage.token;
	}
	
	$.ajax({
		url: "http://trenders.org/api/" + command,
		data: params,
		crossDomain: false,
		success: callback
	});
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