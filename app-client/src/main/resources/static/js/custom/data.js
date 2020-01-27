let DOMStrings = {
	notifyInfo : `info`,
	notifyError: `error`,
	notAvailable : `N/A`,
	notifySuccess : `success`,
	afterBeginOfContainer : `afterbegin`,
	usersFound : `User(s) Found in Database!`,
	errorLoadingUsers : `Could not load users`,
	usersNotFound : `No Users Found in Database!`,
	connectionError : `Error connecting to the provided streaming URL`,
	connectionClosed: `Connection to Streaming Source has been closed!`,
}

let DOMEndpoints = {
	streamingUrl : `http://127.0.0.1:8082/api/users/stream`	
}

let DOMIds = {
	usersContainer: `#users`,
	totalUsers : `#count-of-users`
}

export {DOMStrings, DOMEndpoints, DOMIds};