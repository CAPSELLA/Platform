angular.module('CapsellaApp')
// Creating the Angular Controller
.controller('GenerateTokenController', function($http, $scope, AuthService) {
	
	
	var init = function() {
		$scope.isViewLoading = true;
		document.body.style.cursor='wait';
		$http.get('/capsella_authentication_service/getGroups').success(function(res) {
			
			$scope.groupNames = res;
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
			
		}).error(function(error) {
			$scope.message = error.message;
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
		});
		
		
	};
	
	init();
	
	$scope.submit = function() {
		// requesting the token by usename and passoword

		$scope.isViewLoading = true;
		document.body.style.cursor='wait';
		var head = new Headers();
		$scope.user = AuthService.user;
	    head.append('Content-Type', 'application/json');

		$http({
			url : 'generateToken',
			method : "POST",
			params : {
				name : $scope.name,
				hostname : $scope.hostname,
				groups : $scope.groups,
				user : $scope.user
			},
			headers:head
		}).success(function(res) {
			$scope.groups = null;
			$scope.name = null;
			$scope.hostname = null;
			$scope.generateToken.$setPristine();
			$scope.message = "Generate token successfull !";
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
		}).error(function(error) {
			$scope.message = error.message;
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
		});
	};
	
	
	
});