var exec = require('cordova/exec');
var className = "DinPay";
var DinPay = {
    pay: function (success, err, param) {
        exec(success, err, className, "pay", [param]);
    },
    payWithXML: function (success, err, param) {
        exec(success, err, className, "payWithXML", [param]);
    }
}
module.exports = DinPay;