angular.module('CapsellaApp')
// Creating the Angular Controller
.controller('registeredUsersController', function($http, $scope, AuthService) {
	var edit = false;
	$scope.buttonText = '';
	$scope.newUser = true;
	
	var init = function() {
		$scope.isViewLoading = true;
		document.body.style.cursor='wait';
		$http.get('/capsella_authentication_service/registeredUsers').success(function(res) {
			$scope.users = res;
			$scope.userForm.$setPristine();
			$scope.message='';
			$scope.deleteMessage ="";
			
			$scope.appUser = null;
			$scope.group = null;
			$scope.buttonText = 'Edit';
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
			
		}).error(function(error) {
			$scope.message = error.message;
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
		});
		
		
		$http.get('/capsella_authentication_service/getGroups').success(function(res2) {
			
			$scope.groupNames = res2;
			
			
		}).error(function(error) {
			$scope.message = error.message;
			
		});
		
		
	};
	$scope.initEdit = function(appUser) {
		edit = true;
		$scope.appUser = appUser;
		$scope.message='';
		$scope.newUser = false;
		$scope.buttonText = 'Save User';
	};
	$scope.initAddUser = function() {
		edit = false;
		$scope.appUser = null;
		$scope.userForm.$setPristine();
		$scope.message='';
		$scope.newUser = true;
		$scope.buttonText = 'Create';
	};
	$scope.deleteUser = function(appUser) {
		$scope.isViewLoading = true;
		document.body.style.cursor='wait';
		$http.post('/capsella_authentication_service/deleteUser', appUser).success(function(res) {
			$scope.deleteMessage ="User has deleted!";
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
			init();
		}).error(function(error) {
			$scope.deleteMessage = error.message;
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
		});
	};
	var editUser = function(){
		$scope.isViewLoading = true;
		document.body.style.cursor='wait';
		$http.post('/capsella_authentication_service/updateUser', $scope.appUser).success(function(res) {
			$scope.appUser = null;
			$scope.confirmPassword = null;
			$scope.userForm.$setPristine();
			$scope.message = "Editting Success";
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
			init();
		}).error(function(error) {
			$scope.message =error.message;
			$scope.isViewLoading = false;
			document.body.style.cursor='default';
		});
	};
	
	
	
	
	$scope.submit = function() {
			editUser();
		
	};
	
	init();

});
