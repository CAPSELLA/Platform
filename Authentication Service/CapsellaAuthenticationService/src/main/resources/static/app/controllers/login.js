angular.module('CapsellaApp')
// Creating the Angular Controller


.controller('LoginController', function($http, $scope, $state, AuthService, $rootScope) {
	

	
	// method for login
	$scope.login = function() {
		// requesting the token by usename and passoword

		$scope.isViewLoading = true;
		document.body.style.cursor='wait';
		var head = new Headers();
	    head.append('Content-Type', 'application/json');

		$http({
			url : 'authenticate',
			method : "POST",
			params : {
				username : $scope.username,
				password : $scope.password
			},
			headers:head
		}).success(function(res) {
			$scope.password = null;
			// checking if the token is available in the response
			if (res.token) {
				$scope.message = '';
				// setting the Authorization Bearer token with JWT token
				$http.defaults.headers.common['Authorization'] = 'Bearer ' + res.token;

				// setting the user in AuthService
				AuthService.user = res.user;
				AuthService.set("userToken", 'Bearer ' + res.token);  
				AuthService.set("username", res.user.username);  
				AuthService.setJSON("user2", res.user);  
			//	$rootScope.$broadcast('LoginSuccessful');
				
				// going to the home page
				$scope.isViewLoading = false;
				document.body.style.cursor='default';
				$state.go('home');
			} else {
				// if the token is not present in the response then the
				// authentication was not successful. Setting the error message.
				$scope.message = 'Authetication Failed !';
				$scope.isViewLoading = false;
				document.body.style.cursor='default';
			}
		}).error(function(error) {
			// if authentication was not successful. Setting the error message.
			$scope.message = 'Authetication Failed !';
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
		});
	};
});
