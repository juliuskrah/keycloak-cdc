import {DOMStrings, DOMClasses, DOMEvents, DOMIds, DOMElements} from './data.js';

(()=> {
	
	//VARIABLE TO HOLD INSTANCE OF EVENT_SOURCE SSE CONNECTION
	const EVENT_SOURCE = new EventSource(STREAM_EVENTS_URL);
	
	//VARIABLE TO HOLD REFERENCE TO USERS HTML DOCUMENT AREA
	const USER_CONTAINER = document.querySelector(DOMIds.usersContainer);

	//FUNCTION TO CONVERT 'ZONED-DATE-TIME' TO 'DATE'
	let convertUTCDateToLocalDate = (eventDate) => {
		let date = new Date(eventDate);
		let newDate = new Date(date.getTime()+date.getTimezoneOffset()*60*1000),
		offset = date.getTimezoneOffset() / 60,
		hours = date.getHours();

		newDate.setHours(hours - offset);
		return newDate.toLocaleString();   
	};
	
	let createUserCard = (user) => {

		let role, id = user.id,
			email = user.email || DOMStrings.notAvailable,
			lastName = user.last_name || DOMStrings.notAvailable,
			firstName = user.first_name || DOMStrings.notAvailable,
			timeCreated = user.created_timestamp || DOMStrings.notAvailable;
		
		if (user.roles) role = user.roles[0] || DOMStrings.notAvailable;
		else role = DOMStrings.notAvailable;
		
		let userCard = `<user-card id="${id}">
			<div class="user-card--left">
			<div class="img-thumb">
			<i class="fas fa-user-circle"></i>
			</div>
			</div>
			<div class="user-card--right">
			<div class="user-card--right-info-main">
			<h3 class="username">${firstName} ${lastName}</h3>
			<span class="email">${email}</span>
			<span class="role">${role}</span>
			</div>
			<p class="user-card--right-info-extra">
			<span class="date-created">${convertUTCDateToLocalDate(timeCreated)}</span>
			</p>
			</div>
			</user-card> `;
		USER_CONTAINER.insertAdjacentHTML(DOMStrings.afterBeginOfContainer, userCard);
	};
	
	
	//AJAX REQUEST TO GET & DISPLAY LIST OF AVAIALABE USERS IN DATABASE
	$.ajax({
		url: `users`,
		success:(users)=> {

			if (users.length > 0) {
				
				$(DOMIds.noUsersCard).remove();
				users.forEach((user)=> {
					createUserCard(user);
					$(DOMIds.totalUsers).text(`${users.length}`);
				});
				$.notify(DOMStrings.usersFound, DOMStrings.notifySuccess);
			}
			else {
				$(DOMIds.totalUsers).text(0);
				
				let noUserCard = `<no-user-card id="no-users-card">
								  <div><i class="fas fa-user-slash"></i></div>
								  <div><p>No Users</p></div>
								  </no-user-card>`;
				USER_CONTAINER.insertAdjacentHTML(DOMStrings.afterBeginOfContainer, noUserCard);
				$.notify(DOMStrings.usersNotFound, DOMStrings.notifyInfo);
			}
		},
		error:()=> {
			$(DOMIds.totalUsers).text(0);
			let noUserCard = `<no-user-card id="no-users-card">
							  <div><i class="fas fa-user-slash"></i></div>
							  <div><p>Error Loading Users.</p></div>
							  </no-user-card>`;
			USER_CONTAINER.insertAdjacentHTML(DOMStrings.afterBeginOfContainer, noUserCard);
			$.notify(DOMStrings.errorLoadingUsers, DOMStrings.notifyError);
		}		
	});	 
	
	//ON SSE CONNECTION SUCCESS
	EVENT_SOURCE.onmessage = e => {
		let date = moment(new Date()); 
		const msg  = JSON.parse(e.data);
		
		switch(msg.type) {
		
			case DOMEvents.UserUpdatedEvent:
				if($('#'+ msg.source.id).length) {
					$('#'+ msg.source.id).find(DOMClasses.email).text(msg.source.email);
					$('#'+ msg.source.id).find(DOMClasses.dateCreated).text(msg.source.created_timestamp);
					$('#'+ msg.source.id).find(DOMClasses.username).text(msg.source.first_name +' '+ msg.source.last_name);
					$.notify($('#'+ msg.source.id).find(DOMClasses.username).text() + DOMStrings.userUpdated, DOMStrings.notifySuccess);
				}
				else {
					createUserCard(msg.source);
					$(DOMIds.totalUsers).text(parseInt($(DOMIds.totalUsers).text()) + 1);
					$.notify(msg.source.first_name +' '+ msg.source.last_name + DOMStrings.userAdded, DOMStrings.notifySuccess);
				}
			break;
			
			case DOMEvents.UserDeletedEvent:					
				if($('#'+ msg.source.id).length) {
					$.notify($('#'+ msg.source.id).find(DOMClasses.username).text() + DOMStrings.userDeleted, DOMStrings.notifySuccess);
					$('#'+ msg.source.id).fadeOut().remove();
				}	
				else $.notify(DOMStrings.userNotFound, DOMStrings.notifyInfo);				
			break;

			case DOMEvents.UserRemovedFromRoleEvent:				
				if($('#'+ msg.source.user_id).length) {
					$('#'+ msg.source.user_id).find(DOMClasses.role).text(DOMStrings.notAvailable);
					$.notify($('#'+ msg.source.user_id).find(DOMClasses.username).text() + DOMStrings.userRoleRemoved, DOMStrings.notifySuccess);
				}
				else $.notify(DOMStrings.userNotFound, DOMStrings.notifyInfo);				
			break;

			case DOMEvents.UserAddedToRoleEvent:
				if($('#'+ msg.source.user_id).length) {
					$('#'+ msg.source.user_id).find(DOMClasses.role).html(DOMElements.syncButton);
					$.notify($('#'+ msg.source.user_id).find(DOMClasses.username).text() + DOMStrings.userRoleUpdated, DOMStrings.notifySuccess);
				}
				else $.notify(DOMStrings.userNotFound, DOMStrings.notifyInfo);	
			break;

			case DOMEvents.RoleAddedEvent:
				$.notify(DOMStrings.roleAdded + msg.source.name.toUpperCase(), DOMStrings.notifySuccess);
			break;

			case DOMEvents.RoleUpdatedEvent:
				$.notify(msg.source.name.toUpperCase() + DOMStrings.roleUpdated + msg.source.description, DOMStrings.notifySuccess);
			break;

			case DOMEvents.RoleDeletedEvent:
				$.notify(DOMStrings.roleDeleted, DOMStrings.notifySuccess);
			break;
		}
	};

	
	//ON SSE CONNECTION ERROR
	EVENT_SOURCE.onerror = e => {
		if (e.readyState == EventSource.CLOSED) $.notify(DOMStrings.connectionClosed, DOMStrings.notifyError, {position: 'center'});
		else $.notify(`${DOMStrings.connectionError} \n ${STREAM_EVENTS_URL}`, DOMStrings.notifyError, {globalPosition: 'top center'});
	};

})();