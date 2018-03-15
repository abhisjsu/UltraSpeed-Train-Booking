'use strict';

angular.module('crudApp').controller('BookTicketController',
    ['BookTicketService', '$scope','$location','$window',  function( BookTicketService, $scope, $location, $window) {

        var self = this;
        self.ticket={};
        $scope.val = "retTrip";
        self.passengers=new Array(getSearchDetails().noOfPassengers);
        
        $scope.number = getSearchDetails().noOfPassengers;
        $scope.getPassengerCount = function(num) {
            return new Array(num);   
        }
        
        $scope.getRetVal = function() {
            return "retTrip";   
        }       
        
        self.submit = submit;
        self.getSearchDetails = getSearchDetails;
        self.createTicket = createTicket;
        self.calculateFare = calculateFare;
        self.reset = reset;
        self.bookanotherticket = bookanotherticket;
        self.cancelTicket = cancelTicket;

        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.cancelTicketId ='';
        
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;

        function submit() {
        		alert("Submitting");
            
            console.log('Book new ticket', self.ticket);
            createTicket(self.ticket,self.passengers);   
        }

        function bookanotherticket() {
    		alert("Book Another Ticket");
        
        console.log('Book new ticket', self.ticket);
        var host = $window.location.host;
        alert(host);
        var landingUrl = "http://" + host + "/cusr/#/";
        alert(landingUrl);
        $window.location.href = landingUrl;
        }
        
        function cancelTicket() {
    		alert("Cancel Ticket");
    		alert(self.cancelTicketId);
    		BookTicketService.cancelTicket(self.cancelTicketId);
        }
        
        function createTicket(ticket,passengers) {
            BookTicketService.createTicket(ticket,passengers)
                .then(
                    function (response) {
                        console.log('Ticket is booked successfully');
                        self.successMessage = 'Ticket is booked successfully';
                        self.errorMessage='';
                        self.done = true;
                        //$scope.myForm.$setPristine();
                        alert("I am here");
                        var host = $window.location.host;
                        alert(host);
                        var landingUrl = "http://" + host + "/cusr/#/ticketSuccess";
                        alert(landingUrl);
                        $window.location.href = landingUrl;
                    },
                    function (errResponse) {
                        console.error('Error while creating Ticket');
                        self.errorMessage = 'Error while creating Ticker: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }

        function getSearchDetails(){
            return BookTicketService.getSearchDetails();
        }

        function reset(){
            self.successMessage='';
            self.errorMessage='';
            $scope.myForm.$setPristine(); //reset Form
        }
        
        function calculateFare(){
        	return BookTicketService.calculateFare();
        }

    }


    ]);