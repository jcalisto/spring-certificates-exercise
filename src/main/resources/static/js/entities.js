
function onEntityTypeChange(){
	var type = document.getElementById("entityType").value;
	updateCompanyNameVisibility(type);
}

function updateCompanyNameVisibility(type){
	let companyNameDiv = document.getElementById('companyName');
	if(type === 'Singular'){
			companyNameDiv.style.display = 'inline';
	} else {
		companyNameDiv.style.display = 'none';
	}
}