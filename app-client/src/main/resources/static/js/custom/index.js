(()=> {
	const EVENT_SOURCE = new EventSource(`${CONTEXT}/notification`);
	const USER_CONTAINER = document.querySelector("#users");
	let count = 0;

	$.ajax({
		url: CONTEXT + `users`,
		success:(users)=> {
			console.log(users);
			if (users) {
				
			}
			else {
				$.notify("Hello World", "success");
			}
		},
		error:()=> {
			$.notify("Hello World", "error");
		}		
	});	 
	
	EVENT_SOURCE.onmessage = e => {
		const msg = e.data;
		let userCard = `<user-card class="user-card">
			<div class="user-card--left">
			<div class="img-thumb">
			<img src="img/user.jpg" alt="">
			</div>
			</div>
			<div class="user-card--right">
			<div class="user-card--right-info-main">
			<h3>Louis Gamor</h3>
			<span class="email">lkgamor@gmail.com</span>
			<span class="role">Admin</span>
			</div>
			<p class="user-card--right-info-extra">
			<span class="date-created">1st January, 2020</span>
			</p>
			</div>
			</user-card> `;
		USER_CONTAINER.insertAdjacentHTML('afterbegin', userCard);
		$('#count-of-users').text(`${count++}`);
	};

	EVENT_SOURCE.onopen = e => console.log('open');

	EVENT_SOURCE.onerror = e => {
		if (e.readyState == EventSource.CLOSED) {
			console.log('close');
		}
		else {
			console.log(e);
		}
	};

	EVENT_SOURCE.addEventListener('second', function(e) {
		console.log('second', e.data);
	}, false);

})();