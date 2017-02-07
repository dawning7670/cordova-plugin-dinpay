
    var exec = require('cordova/exec');
    var className = "DinPay";
    var dinpay = {
        start: function () {
            cordova.exec(function(){}, function(){}, className, "start", []);
        }
    }
    module.exports = dinpay;