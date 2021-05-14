function createCustomer() {
    console.log("called");
    var formObj = gatherFormInfo();
    axios.post('http://final-prep-sreid-final.apps.okd4.infoteach.ca/api/customers/addCustomer'
                + '?customerNumber=' + Math.floor((Math.random() * 10000) + 1000)
                + '&customerName=' + formObj.customer_name
                + '&contactLastName=' + formObj.contact_last_name
                + '&contactFirstName=' + formObj.contact_first_name
                + '&phone=' + formObj.phone_num
                + '&addressLine1=' + formObj.address_line_1
                + '&addressLine2=asdffdsd&city=' + formObj.address_line_2
                + '&state=' + formObj.state
                + '&postalCode=' + formObj.postal_code
                + '&country=' + formObj.country
                + '&salesRepEmployeeNumber=' + 1111
                + '&creditLimit=' + 5000)
			.then( function (response) {
        console.log(response);
    });
}


function gatherFormInfo() {
	
    var companyName = document.getElementById('companyName').value;
    var contactLastName = document.getElementById('contactLastName').value;
    var contactFirstName = document.getElementById('contactFirstName').value;
    var phoneNum = document.getElementById('phoneNum').value;
    var addressLine1 = document.getElementById('addressLine1').value;
    var AddressLine2 = document.getElementById('AddressLine2').value;
    var city = document.getElementById('city').value;
    var state = document.getElementById('state').value;
    var postalCode = document.getElementById('postalCode').value;
    var country = document.getElementById('country').value;

    return { "customer_name": companyName, 
             "contact_last_name": contactLastName,
             "contact_first_name": contactFirstName,
             "phone_num": phoneNum,
             "address_line_1": addressLine1,
             "address_line_2": AddressLine2,
             "city": city,
             "state": state,
             "postal_code": postalCode,
             "country": country };
}