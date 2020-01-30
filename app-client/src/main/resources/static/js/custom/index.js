import {DOMStrings, DOMEndpoints, DOMIds} from './data.js';

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
	
	//AJAX REQUEST TO GET & DISPLAY LIST OF AVAIALABE USERS IN DATABASE
	$.ajax({
		url: `users`,
		success:(users)=> {

			if (users.length > 0) {
				
				$('#no-users-card').remove();
				users.forEach((user)=> {
					
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
						<h3>${firstName} ${lastName}</h3>
						<span class="email">${email}</span>
						<span class="role">${role}</span>
						</div>
						<p class="user-card--right-info-extra">
						<span class="date-created">${convertUTCDateToLocalDate(timeCreated)}</span>
						</p>
						</div>
						</user-card> `;
					USER_CONTAINER.insertAdjacentHTML(DOMStrings.afterBeginOfContainer, userCard);
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
			<div><p>Error Loading Users.<br><br> Verify that the URL is valid</p></div>
			</no-user-card>`;
			USER_CONTAINER.insertAdjacentHTML(DOMStrings.afterBeginOfContainer, noUserCard);
			$.notify(DOMStrings.errorLoadingUsers, DOMStrings.notifyError);
		}		
	});	 
	
	//ON SSE CONNECTION SUCCESS
	EVENT_SOURCE.onmessage = e => {
		const msg = e.data;
		let date = moment(new Date()); 
		console.log('MESSAGE -> ', msg)
	};

	
	//ON SSE CONNECTION ERROR
	EVENT_SOURCE.onerror = e => {
		if (e.readyState == EventSource.CLOSED) {
			$.notify(DOMStrings.connectionClosed, DOMStrings.notifyError, {position: 'center'});
		}
		else {
			console.log(e)
			$.notify(`${DOMStrings.connectionError} \n ${STREAM_EVENTS_URL}`,
					DOMStrings.notifyError, 
					{globalPosition: 'top center'}
			);
		}
	};

})();