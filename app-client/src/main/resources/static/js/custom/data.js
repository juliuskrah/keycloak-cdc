let DOMStrings = {
	notifyInfo : `info`,
	notifyError: `error`,
	notAvailable : `N/A`,
	notifySuccess : `success`,
	userAdded : ` has been added!`,
	userUpdated : ` has been updated!`,
	userDeleted : ` has been deleted!`,
	afterBeginOfContainer : `afterbegin`,
	userNotFound : `User does not exist!`,
	roleDeleted : `Role has been deleted`,
	usersFound : `User(s) Found in Database!`,
	errorLoadingUsers : `Could not load users`,
	roleAdded : `A new role has been added = `,
	userRoleRemoved : `'s role has been removed!`,
	userRoleUpdated : `'s role has been updated!`,
	newEvent : `A new event has been registered!`,
	usersNotFound : `No Users Found in Database!`,
	roleUpdated : ` role has been updated with description = `,
	connectionError : `Error connecting to the provided streaming URL`,
	connectionClosed: `Connection to Streaming Source has been closed!`,
}

let DOMClasses = {
	role : `.role`,
	username : `.username`,
	dateCreated : `.date-created`
}

let DOMEvents = {
	RoleAddedEvent : `RoleAddedEvent`,
	RoleUpdatedEvent : `RoleUpdatedEvent`,
	RoleDeletedEvent : `RoleDeletedEvent`,
	UserUpdatedEvent : `UserUpdatedEvent`,
	UserDeletedEvent : `UserDeletedEvent`,
	UserAddedToRoleEvent : `UserAddedToRoleEvent`,
	UserRemovedFromRoleEvent : `UserRemovedFromRoleEvent`,
}

let DOMIds = {
	usersContainer: `#users`,
	totalUsers : `#count-of-users`,
	noUsersCard: `#no-users-card`
}

let DOMElements = {
	syncButton: `<i class="fas fa-sync-alt cursor-pointer" title="This user's role has been updated. Click to Sync" onclick="location.reload(true);"></i>`
}

export {DOMStrings, DOMClasses, DOMEvents, DOMIds, DOMElements};