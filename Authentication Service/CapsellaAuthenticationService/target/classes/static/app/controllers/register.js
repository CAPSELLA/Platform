angular.module('CapsellaApp')
// Creating the Angular Controller
.controller('RegisterController', function($http, $scope, AuthService) {
	
	
	
	var init = function() {
		$scope.isViewLoading = true;
		document.body.style.cursor='wait';
		$http.get('/capsella_authentication_service-dev/getGroups').success(function(res) {
			
			$scope.groupNames = res;
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
			
		}).error(function(error) {
			$scope.message = error.message;
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
		});
		
		
	};
	
	
	$scope.submit = function() {
		$scope.isViewLoading = true;
		document.body.style.cursor='wait';
		$http.post('/capsella_authentication_service-dev/register', $scope.user).success(function(res) {
			$scope.user = null;
			$scope.confirmPassword = null;
			$scope.register.$setPristine();
			$scope.message = "Registration successfull !";
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
		}).error(function(error) {
			$scope.message = error.message;
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
		});
	};
	
	init();
});
